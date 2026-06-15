package com.zifang.util.it.selftest;

import com.zifang.util.aop.Advise;
import com.zifang.util.aop.Intercept;
import com.zifang.util.aop.ProxyFactory;
import com.zifang.util.bean.BeanCopier;
import com.zifang.util.cache.Cache;
import com.zifang.util.cache.CacheBuilder;
import com.zifang.util.cache.decorator.MeteredCache;
import com.zifang.util.db.lock.DbDistributedLock;
import com.zifang.util.distributes.sequence.NanoId;
import com.zifang.util.distributes.sequence.SegmentIdGenerator;
import com.zifang.util.distributes.sequence.UuidV7;
import com.zifang.util.jwt.Claims;
import com.zifang.util.jwt.Jwt;
import com.zifang.util.jwt.JwtException;
import com.zifang.util.lock.FileDistributedLock;
import com.zifang.util.ratelimit.RateLimiter;
import com.zifang.util.ratelimit.TokenBucketRateLimiter;
import com.zifang.util.resilience.Bulkhead;
import com.zifang.util.resilience.CircuitBreaker;
import com.zifang.util.resilience.Retry;
import com.zifang.util.resilience.TimeLimiter;
import com.zifang.util.trace.TraceContextHolder;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.*;

/**
 * <h2>z-util 全栈自检</h2>
 * <p>
 * 模拟一个 z-opc-like 的"订单 + 用户 + 鉴权" 微服务，<b>完全用 z-util 自研栈 + JDK 8</b>，不引任何三方中间件。
 * 验证所有自研模块协同工作（14 个 z-util 子模块 + 1 个 it 演示）。
 *
 * <h3>使用的自研能力</h3>
 * <pre>
 *   z-util-aop          @Intercept + Advise            → Controller 切面
 *   z-util-trace        TraceContextHolder             → traceId/spanId 注入
 *   z-util-cache        MemoryCache / WTinyLfuCache    → 用户 session 缓存
 *   z-util-cache.decorator MeteredCache                 → QPS 指标
 *   z-util-ratelimit    TokenBucketRateLimiter         → 接口限流
 *   z-util-resilience   CircuitBreaker / Bulkhead / Retry → 外部依赖抗雪崩
 *   z-util-jwt          Jwt HS256                      → 用户鉴权
 *   z-util-jdbc         DbDistributedLock              → 订单并发控制
 *   z-util-distribute   SegmentIdGenerator / UuidV7 / NanoId → 各种 ID
 *   z-util-bean         BeanCopier                     → DTO 转换
 *   z-util-lock         FileDistributedLock            → 任务排它执行
 * </pre>
 *
 * <h3>被替换的三方库（z-opc 实际使用）</h3>
 * <pre>
 *   org.aspectj          → z-util-aop
 *   cn.hutool            → z-util-core (已有)
 *   io.jsonwebtoken      → z-util-jwt
 *   com.alicp.jetcache   → z-util-cache
 *   org.redisson         → z-util-lock / z-util-cache 集群协议
 *   com.xuxueli (xxl-job)→ z-util-schedule (待建)
 *   io.prometheus        → z-util-monitor (已有)
 *   ognl                → z-util-expression
 *   com.jayway.jsonpath  → z-util-json (待建)
 *   com.esotericsoftware.kryo → z-util-serialize (待建)
 * </pre>
 */
public class SelfCheckTest {

    // ====================================================================
    // 数据源（用 H2 in-memory 模拟 z-opc 的 MySQL）
    // ====================================================================

    private static JdbcDataSource DS;

    @org.junit.BeforeClass
    public static void setupDb() throws Exception {
        DS = new JdbcDataSource();
        DS.setURL("jdbc:h2:mem:selftest;DB_CLOSE_DELAY=-1");
        DS.setUser("sa");
        DS.setPassword("");
        try (Connection c = DS.getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE distributed_lock (lock_key VARCHAR(128) PRIMARY KEY, token VARCHAR(64) NOT NULL, expire_at BIGINT NOT NULL)");
            s.execute("CREATE TABLE id_segment (biz_tag VARCHAR(64) PRIMARY KEY, max_id BIGINT NOT NULL DEFAULT 0, step INT NOT NULL DEFAULT 100)");
            s.execute("CREATE TABLE t_order (id BIGINT PRIMARY KEY, order_no VARCHAR(64), user_id VARCHAR(64), product_id VARCHAR(64), amount INT, idempotency_key VARCHAR(64))");
            s.execute("INSERT INTO id_segment (biz_tag, max_id, step) VALUES ('order', 0, 100)");
        }
    }

    // ====================================================================
    // 业务模型
    // ====================================================================

    public static class User { public String id; public String name; public String role; }
    public static class OrderDto { public Long id; public String orderNo; public String userId; public String productId; public Integer amount; public String idempotencyKey; }
    public static class UserVo { public String id; public String name; public String role; public String sessionToken; }
    public static class ApiResponse<T> { public int code; public String message; public T data; public String traceId; }

    // ====================================================================
    // 业务服务（纯 z-util 实现）
    // ====================================================================

    /** 用户服务：JWT 鉴权 + 限流 + 缓存。 */
    public static class UserService {
        private final String JWT_SECRET = "z-util-selftest-secret";
        private final Cache<String, User> sessionCache = new MeteredCache<>(
                CacheBuilder.<String, User>newBuilder()
                        .name("user-session")
                        .maximumSize(10_000)
                        .expireAfterWrite(Duration.ofHours(1))
                        .recordStats()
                        .build());
        private final RateLimiter rateLimiter = new TokenBucketRateLimiter(100, 50);
        private final Cache<String, String> blacklist = CacheBuilder.<String, String>newBuilder()
                .name("jwt-blacklist").maximumSize(10_000).expireAfterWrite(Duration.ofDays(7)).build();

        public String login(String username, String password) {
            // 限流：每秒 100 QPS，桶 50
            if (!rateLimiter.tryAcquire().isAllowed()) {
                throw new RuntimeException("rate limit exceeded");
            }
            // 模拟 DB 查用户
            User u = new User();
            u.id = "U" + Math.abs(username.hashCode());
            u.name = username;
            u.role = "user";
            // 签发 token
            String token = Jwt.builder()
                    .algorithm(Jwt.HS256)
                    .secret(JWT_SECRET)
                    .claims(new Claims()
                            .sub(u.id)
                            .iss("z-util-selftest")
                            .put("name", u.name)
                            .put("role", u.role)
                            .jti(UUID.randomUUID().toString())
                            .expireIn(3600))
                    .build();
            sessionCache.put(token, u);
            return token;
        }

        public User verify(String token) {
            Claims c = Jwt.parser()
                    .algorithm(Jwt.HS256)
                    .secret(JWT_SECRET)
                    .requireExp()
                    .parse(token);
            if (c.jti() != null && blacklist.get(c.jti()) != null) {
                throw new JwtException("token revoked");
            }
            User u = sessionCache.get(token);
            if (u == null) {
                // 缓存未命中但 token 有效 → 重新构造
                u = new User();
                u.id = c.sub();
                u.name = (String) c.get("name");
                u.role = (String) c.get("role");
                sessionCache.put(token, u);
            }
            return u;
        }

        public void logout(String token) {
            Claims c = Jwt.parser().algorithm(Jwt.HS256).secret(JWT_SECRET).parse(token);
            if (c.jti() != null) blacklist.put(c.jti(), "revoked");
        }

        public Cache<String, User> sessionCache() { return sessionCache; }
    }

    /** 订单服务：DB 锁 + 号段 + UUIDv7 + NanoId + BeanCopier。 */
    public static class OrderService {
        private final DataSource ds;
        private final SegmentIdGenerator idGen;
        private final NanoId nanoId = new NanoId(12);
        private final DbDistributedLock lockFactory;
        private final String LOCK_KEY_PREFIX = "order:";
        private final CircuitBreaker remoteBreaker = new CircuitBreaker(3, 1, 200);

        public OrderService(DataSource ds) {
            this.ds = ds;
            this.idGen = new SegmentIdGenerator("order", 100, new DbSegmentLoader(ds));
            this.lockFactory = null;  // 每次新建锁（key 不同）
        }

        public OrderDto create(String userId, String productId, int amount) throws Exception {
            // 1) 分布式锁：每 (user, product) 一把
            DbDistributedLock lock = new DbDistributedLock(ds, LOCK_KEY_PREFIX + userId + ":" + productId, 5000);
            String token = lock.tryLock(200_000_000L);   // 200ms
            if (token == null) throw new RuntimeException("system busy");
            try {
                // 2) 模拟熔断：调外部"库存服务"（这里本地 mock）
                String stockCheck = remoteBreaker.call(() -> {
                    if (Math.random() < 0.05) throw new RuntimeException("remote stock fail");
                    return "ok";
                });
                if (!"ok".equals(stockCheck)) throw new RuntimeException("stock service unavailable");

                // 3) 分配 ID
                long id = idGen.nextId();
                String orderNo = "O" + nanoId.next();
                String idempotencyKey = UuidV7.next();

                // 4) 写 DB
                try (Connection c = ds.getConnection();
                     PreparedStatement ps = c.prepareStatement(
                             "INSERT INTO t_order (id, order_no, user_id, product_id, amount, idempotency_key) VALUES (?, ?, ?, ?, ?, ?)")) {
                    ps.setLong(1, id);
                    ps.setString(2, orderNo);
                    ps.setString(3, userId);
                    ps.setString(4, productId);
                    ps.setInt(5, amount);
                    ps.setString(6, idempotencyKey);
                    ps.executeUpdate();
                }

                // 5) 重试 + 超时保护示例（演示用，未启用）
                // String result = Retry.builder().maxAttempts(3).build()
                //         .call(() -> TimeLimiter.doWithTimeout(200, () -> remoteCall()));
                OrderDto dto = new OrderDto();
                dto.id = id;
                dto.orderNo = orderNo;
                dto.userId = userId;
                dto.productId = productId;
                dto.amount = amount;
                dto.idempotencyKey = idempotencyKey;
                return dto;
            } finally {
                lock.unlock(token);
            }
        }
    }

    /** 号段加载器（实际连 MySQL）。 */
    static class DbSegmentLoader implements SegmentIdGenerator.SegmentLoader {
        private final DataSource ds;
        DbSegmentLoader(DataSource ds) { this.ds = ds; }
        @Override
        public long[] loadSegment(String bizTag, int step) {
            try (Connection c = ds.getConnection()) {
                c.setAutoCommit(false);
                try (PreparedStatement upd = c.prepareStatement("UPDATE id_segment SET max_id = max_id + ? WHERE biz_tag = ?")) {
                    upd.setInt(1, step); upd.setString(2, bizTag); upd.executeUpdate();
                }
                long[] seg = new long[2];
                try (PreparedStatement sel = c.prepareStatement("SELECT max_id FROM id_segment WHERE biz_tag = ?")) {
                    sel.setString(1, bizTag);
                    try (ResultSet rs = sel.executeQuery()) {
                        if (rs.next()) { seg[1] = rs.getLong(1); seg[0] = seg[1] - step + 1; }
                    }
                }
                c.commit();
                return seg;
            } catch (Exception e) { throw new RuntimeException(e); }
        }
    }

    // ====================================================================
    // 切面：traceId 自动注入 + 业务日志
    // ====================================================================

    public interface OrderController {
        ApiResponse<OrderDto> create(String token, String productId, int amount) throws Exception;
        ApiResponse<List<OrderDto>> list(String token) throws Exception;
    }

    public static class OrderControllerImpl implements OrderController {
        private final UserService userService;
        private final OrderService orderService;
        private final List<OrderDto> cache = new ArrayList<>();

        public OrderControllerImpl(UserService u, OrderService o) {
            this.userService = u;
            this.orderService = o;
        }

        @Override
        @Intercept({ LogAdvise.class, AuthAdvise.class })
        public ApiResponse<OrderDto> create(String token, String productId, int amount) throws Exception {
            // Auth 切面已 verify；这里直接拿 user
            User u = userService.verify(token);
            OrderDto order = orderService.create(u.id, productId, amount);
            cache.add(order);
            return ok(order);
        }

        @Override
        @Intercept({ LogAdvise.class, AuthAdvise.class })
        public ApiResponse<List<OrderDto>> list(String token) throws Exception {
            User u = userService.verify(token);
            return ok(filterBy(cache, u.id));
        }

        private List<OrderDto> filterBy(List<OrderDto> all, String userId) {
            List<OrderDto> r = new ArrayList<>();
            for (OrderDto o : all) if (userId.equals(o.userId)) r.add(o);
            return r;
        }

        private <T> ApiResponse<T> ok(T data) {
            ApiResponse<T> r = new ApiResponse<>();
            r.code = 0; r.message = "ok"; r.data = data;
            r.traceId = TraceContextHolder.currentTraceId();
            return r;
        }
    }

    public static class LogAdvise implements Advise<OrderController> {
        static final LongAdder CALLS = new LongAdder();
        static final List<String> LOGS = new ArrayList<>();

        @Override
        public Object around(OrderController target, Method method, Object[] args, Advise.Chain chain) throws Throwable {
            CALLS.increment();
            TraceContextHolder.startNew();
            long t0 = System.nanoTime();
            try {
                Object r = chain.proceed();
                long ms = (System.nanoTime() - t0) / 1_000_000L;
                LOGS.add(String.format("%s ms=%d trace=%s", method.getName(), ms, TraceContextHolder.currentTraceId()));
                return r;
            } finally {
                TraceContextHolder.stop();
            }
        }
    }

    public static class AuthAdvise implements Advise<OrderController> {
        @Override
        public Object around(OrderController target, Method method, Object[] args, Advise.Chain chain) throws Throwable {
            // args[0] = token
            if (args[0] == null) throw new JwtException("missing token");
            // 不在这里真正 verify（OrderController.create 内部 verify）
            // 这里只演示切面可被串联：可以加更多校验
            return chain.proceed();
        }
    }

    // ====================================================================
    // 端到端流程：登录 → 下单 → 列表 → 注销 → 限流 → BeanCopier
    // ====================================================================

    @Test
    public void testFullStack_endToEnd() throws Exception {
        // 1) 装服务
        UserService userService = new UserService();
        OrderService orderService = new OrderService(DS);
        OrderController controller = ProxyFactory.wrap(new OrderControllerImpl(userService, orderService));
        // 演示 BeanCopier：把 User → UserVo
        // (实际在下单后展示)

        // 2) 登录
        String token = userService.login("alice", "p@ss");
        assertNotNull(token);
        assertTrue("token has 3 parts", token.split("\\.").length == 3);

        // 3) 下单
        LogAdvise.LOGS.clear();
        // 把 userId 提取出来用于后面断言
        User loggedIn = userService.verify(token);
        String aliceId = loggedIn.id;
        for (int i = 0; i < 5; i++) {
            ApiResponse<OrderDto> r = controller.create(token, "P" + (i % 2), 1);
            assertEquals(0, r.code);
            assertEquals(aliceId, r.data.userId);
            assertNotNull("orderNo should be set", r.data.orderNo);
            assertTrue("orderNo starts with O", r.data.orderNo.startsWith("O"));
            assertNotNull("traceId from response", r.traceId);
        }

        // 4) 列表
        ApiResponse<List<OrderDto>> list = controller.list(token);
        assertEquals(5, list.data.size());
        for (OrderDto o : list.data) {
            assertEquals(aliceId, o.userId);
        }

        // 5) traceId 注入：每个切面调用都有独立 traceId，日志能串联
        // 5 create + 1 list = 6 logs
        assertEquals(6, LogAdvise.LOGS.size());
        for (String log : LogAdvise.LOGS) {
            assertTrue("log has traceId: " + log, log.contains("trace="));
        }

        // 6) 注销
        userService.logout(token);
        // 注销后 verify 应该失败
        try {
            controller.create(token, "P0", 1);
            fail("revoked token should be rejected");
        } catch (Exception e) {
            assertTrue("rejection reason: " + e.getMessage(),
                    e.getMessage().contains("revoked") || e.getCause() != null);
        }

        // 7) MeteredCache 统计
        MeteredCache<String, User> metered = (MeteredCache<String, User>) userService.sessionCache();
        // 注意：put/get 都计数；get hit 应远 > 0
        assertTrue("hit count should be > 0, got " + metered.meter().hitCount(),
                metered.meter().hitCount() > 0);

        // 8) BeanCopier：User → UserVo
        User u = new User();
        u.id = "U123"; u.name = "bob"; u.role = "admin";
        UserVo vo = BeanCopier.copy(u, UserVo.class);
        assertEquals("U123", vo.id);
        assertEquals("bob", vo.name);
        assertEquals("admin", vo.role);
        assertNull("sessionToken not in source", vo.sessionToken);

        // 9) 重试 + 限流 + 舱壁：演示配置 OK
        Retry retry = Retry.builder().maxAttempts(3).initialDelay(5, java.util.concurrent.TimeUnit.MILLISECONDS)
                .retryOn(RuntimeException.class).build();
        AtomicInteger calls = new AtomicInteger();
        String r = retry.call(() -> {
            if (calls.incrementAndGet() < 2) throw new RuntimeException("flaky");
            return "ok";
        });
        assertEquals("ok", r);
        assertEquals(2, calls.get());

        // 10) TimeLimiter
        TimeLimiter tl = new TimeLimiter(2, 1000);
        assertEquals((Object) "fast", tl.call(() -> "fast", 50));
        tl.shutdown();

        // 11) Bulkhead
        Bulkhead bh = new Bulkhead(2);
        assertEquals((Object) Integer.valueOf(1), bh.call(() -> 1));
        assertEquals(2, bh.availablePermits());

        // 12) FileLock（单机，验证 FileLock 也工作）
        FileDistributedLock fileLock = new FileDistributedLock(
                java.nio.file.Files.createTempFile("selfcheck-", ".lock").toString());
        String ftoken = fileLock.tryLock();
        assertNotNull(ftoken);
        fileLock.unlock(ftoken);
    }

    @Test
    public void testConcurrentOrders_unique() throws Exception {
        UserService u = new UserService();
        OrderService o = new OrderService(DS);
        u.login("carol", "p");
        // 多个线程同 (user, product) → DB 应有 5 行（互锁 5 次 200ms 内串行完成）
        Thread[] ts = new Thread[5];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> {
                try { o.create("carol", "P-A", 1); } catch (Exception ignored) {}
            });
        }
        for (Thread t : ts) t.start();
        for (Thread t : ts) t.join();
        // 5 线程都应在 200ms × 5 = 1s 内串行完成
        try (Connection c = DS.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM t_order WHERE user_id='carol' AND product_id='P-A'")) {
            assertTrue(rs.next());
            int n = rs.getInt(1);
            assertTrue("expected 5 orders, got " + n, n == 5);
        }
    }
}
