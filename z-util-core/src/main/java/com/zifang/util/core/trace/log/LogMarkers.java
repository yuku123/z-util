package com.zifang.util.core.trace.log;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * 预置日志 {@link Marker}：用于日志聚合系统（ELK / Loki）做维度过滤。
 * <p>
 * Marker 跟 logger name / MDC 是不同的过滤维度：
 * <ul>
 *   <li>logger name: 来源模块</li>
 *   <li>MDC: 单次调用的上下文（traceId / userId）</li>
 *   <li>marker: 这条日志的"分类"（业务 / 安全 / 性能 / 外部调用 / DB）</li>
 * </ul>
 *
 * <h3>log4j2.xml 里按 marker 路由</h3>
 * <pre>{@code
 *   <Routing name="MarkerRoute">
 *     <Routes pattern="$${marker:}>
 *       <Route key="BUSINESS">
 *         <File name="BusinessLog" fileName="logs/business.log"/>
 *       </Route>
 *       <Route key="SECURITY">
 *         <File name="SecurityLog" fileName="logs/security.log"/>
 *       </Route>
 *       <Route key="${marker:}>
 *         <File name="DefaultLog" fileName="logs/app.log"/>
 *       </Route>
 *     </Routes>
 *   </Routing>
 * }</pre>
 *
 * <h3>使用</h3>
 * <pre>{@code
 *   LOG.info(LogMarkers.BUSINESS, "order created: {}", orderId);
 *   LOG.warn(LogMarkers.SECURITY, "login failed for user: {}", userId);
 * }</pre>
 */
public final class LogMarkers {

    /** 业务事件：订单创建、状态变更、流程节点等。 */
    public static final Marker BUSINESS = MarkerFactory.getMarker("BUSINESS");

    /** 安全事件：登录失败、权限拒绝、token 校验失败、登出等。 */
    public static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");

    /** 性能事件：慢查询、慢调用、超时阈值告警。 */
    public static final Marker PERFORMANCE = MarkerFactory.getMarker("PERFORMANCE");

    /** 外部调用：HTTP/RPC/DB 等出站请求。 */
    public static final Marker EXTERNAL = MarkerFactory.getMarker("EXTERNAL");

    /** 内部数据库调用。 */
    public static final Marker DB = MarkerFactory.getMarker("DB");

    /** 缓存读写。 */
    public static final Marker CACHE = MarkerFactory.getMarker("CACHE");

    /** 启动/关闭/重载等生命周期事件。 */
    public static final Marker LIFECYCLE = MarkerFactory.getMarker("LIFECYCLE");

    private LogMarkers() {}
}
