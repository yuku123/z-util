package com.zifang.util.trace.log;

import com.zifang.util.aop.Advise;
import com.zifang.util.aop.Intercept;
import com.zifang.util.aop.ProxyFactory;
import com.zifang.util.trace.TraceContextHolder;
import org.junit.After;
import org.junit.Test;
import org.slf4j.MDC;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

/**
 * 日志工具单元测试。
 * <p>
 * 不依赖 log4j2 绑定（用 slf4j-simple 或 NOP binding 即可，默认走 NOP 不打日志也无所谓），
 * 只验证工具类自身的逻辑正确性。
 */
public class LogUtilsTest {

    @After
    public void cleanup() {
        TraceContextHolder.stop();
        MDC.clear();
    }

    // ===== LogHttpHeaders =====

    @Test
    public void httpHeaders_haveStandardValues() {
        assertEquals("traceparent", LogHttpHeaders.TRACEPARENT);
        assertEquals("tracestate", LogHttpHeaders.TRACESTATE);
        assertEquals("X-Request-Id", LogHttpHeaders.X_REQUEST_ID);
        assertEquals("X-User-Id", LogHttpHeaders.X_USER_ID);
        assertEquals("X-B3-TraceId", LogHttpHeaders.X_B3_TRACE_ID);
        assertEquals("X-B3-SpanId", LogHttpHeaders.X_B3_SPAN_ID);
    }

    // ===== LogMarkers =====

    @Test
    public void markers_haveExpectedNames() {
        assertEquals("BUSINESS", LogMarkers.BUSINESS.getName());
        assertEquals("SECURITY", LogMarkers.SECURITY.getName());
        assertEquals("PERFORMANCE", LogMarkers.PERFORMANCE.getName());
        assertEquals("EXTERNAL", LogMarkers.EXTERNAL.getName());
        assertEquals("DB", LogMarkers.DB.getName());
        assertEquals("CACHE", LogMarkers.CACHE.getName());
        assertEquals("LIFECYCLE", LogMarkers.LIFECYCLE.getName());
    }

    // ===== LogContext =====

    @Test
    public void putAndGet_roundTrips() {
        LogContext.put("k1", "v1");
        assertEquals("v1", LogContext.get("k1"));
    }

    @Test
    public void putNull_removesKey() {
        LogContext.put("k2", "v2");
        LogContext.put("k2", null);
        assertNull(LogContext.get("k2"));
    }

    @Test
    public void putAll_writesAllEntries() {
        Map<String, String> m = new HashMap<>();
        m.put("a", "1");
        m.put("b", "2");
        LogContext.putAll(m);
        assertEquals("1", LogContext.get("a"));
        assertEquals("2", LogContext.get("b"));
    }

    @Test
    public void snapshot_returnsDefensiveCopy() {
        LogContext.put("a", "1");
        Map<String, String> snap = LogContext.snapshot();
        snap.put("a", "modified");
        // 原值不变
        assertEquals("1", LogContext.get("a"));
    }

    @Test
    public void with_restoresPreviousMdcOnReturn() {
        LogContext.put("prev", "yes");
        LogContext.with(java.util.Collections.singletonMap("scoped", "1"), () -> {
            assertEquals("1", LogContext.get("scoped"));
            assertEquals("yes", LogContext.get("prev"));
        });
        // 退出 with：scoped 应被清，prev 应保留
        assertNull("scoped should be cleared", LogContext.get("scoped"));
        assertEquals("yes", LogContext.get("prev"));
    }

    @Test
    public void with_restoresMdcOnException() {
        LogContext.put("prev", "yes");
        try {
            LogContext.with(java.util.Collections.singletonMap("scoped", "1"), () -> {
                throw new RuntimeException("boom");
            });
            fail("should have thrown");
        } catch (RuntimeException expected) {
            assertEquals("boom", expected.getMessage());
        }
        // 即使抛异常，scoped 也应清掉，prev 保留
        assertNull(LogContext.get("scoped"));
        assertEquals("yes", LogContext.get("prev"));
    }

    @Test
    public void currentTraceId_returnsNoTraceWhenAbsent() {
        assertEquals(LogContext.NO_TRACE, LogContext.currentTraceId());
        assertEquals("no-span", LogContext.currentSpanId());
    }

    @Test
    public void currentTraceId_returnsActiveTraceId() {
        TraceContextHolder.startNew();
        String id = TraceContextHolder.currentTraceId();
        assertEquals(id, LogContext.currentTraceId());
        // spanId 也应非空
        assertNotEquals("no-span", LogContext.currentSpanId());
    }

    // ===== LoggableAdvise =====

    @Test
    public void loggableAdvise_passesThroughAndPropagatesExceptions() throws Throwable {
        // 调一次普通方法，验证 around 不改变返回值
        AtomicReference<String> captured = new AtomicReference<>();
        Advise<FakeTarget> advise = new LoggableAdvise<>();
        Object result = advise.around(new FakeTarget(), FakeTarget.class.getMethod("greet", String.class),
                new Object[]{"alice"},
                () -> { captured.set("chain-called"); return "hi-alice"; });
        assertEquals("hi-alice", result);
        assertEquals("chain-called", captured.get());
    }

    @Test
    public void loggableAdvise_exceptionIsPropagated() {
        LoggableAdvise<FakeTarget> advise = new LoggableAdvise<>();
        try {
            advise.around(new FakeTarget(), FakeTarget.class.getMethod("boom"),
                    new Object[0],
                    () -> { throw new IllegalStateException("expected"); });
            fail("expected exception");
        } catch (IllegalStateException e) {
            assertEquals("expected", e.getMessage());
        } catch (Throwable t) {
            fail("wrong type: " + t);
        }
    }

    // ===== @Loggable + ProxyFactory 端到端 =====

    public interface Service {
        String create(String user, int amount);
    }

    public static class ServiceImpl implements Service {
        @Override
        @Loggable(logResult = false)
        @Intercept(LoggableAdvise.class)
        public String create(String user, int amount) {
            return "created-for-" + user + "-" + amount;
        }
    }

    @Test
    public void loggable_worksThroughProxyFactory() {
        Service svc = (Service) ProxyFactory.wrap(new ServiceImpl());
        // 不抛异常即视为成功（实际日志走 slf4j 绑定，没有 binding 时静默）
        String r = svc.create("alice", 100);
        assertEquals("created-for-alice-100", r);
    }

    // ===== helper class =====

    public static class FakeTarget {
        public String greet(String name) { return "hi-" + name; }
        public void boom() { throw new IllegalStateException("expected"); }
    }
}
