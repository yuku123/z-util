package com.zifang.util.core.trace.log;

/**
 * 日志/追踪相关的 HTTP 头常量。
 * <p>
 * 提供统一的字符串常量，避免每个项目都自己定义"X-Request-Id"等。
 *
 * <h3>W3C Trace Context（标准）</h3>
 * <ul>
 *   <li>{@link #TRACEPARENT} - 标准 trace 传播头，格式见 {@code TraceIds.parseTraceparent}</li>
 *   <li>{@link #TRACESTATE} - 厂商扩展字段（可选）</li>
 * </ul>
 *
 * <h3>事实标准（业界广泛使用）</h3>
 * <ul>
 *   <li>{@link #X_REQUEST_ID} - 网关/反向代理生成的单次请求唯一 ID（nginx / envoy / slb）</li>
 *   <li>{@link #X_USER_ID} - 上游服务透传的用户 ID（不推荐；MDC 里更合适）</li>
 *   <li>{@link #X_B3_TRACE_ID} / {@link #X_B3_SPAN_ID} - Zipkin 旧版 B3 头（兼容老系统）</li>
 * </ul>
 */
public final class LogHttpHeaders {

    /**
     * W3C 标准：trace 上下文传播。
     */
    public static final String TRACEPARENT = "traceparent";

    /**
     * W3C 标准：trace 状态（厂商扩展）。
     */
    public static final String TRACESTATE = "tracestate";

    /**
     * 通用请求 ID（由网关或第一跳生成）。
     */
    public static final String X_REQUEST_ID = "X-Request-Id";

    /**
     * 用户 ID（透传，慎用）。
     */
    public static final String X_USER_ID = "X-User-Id";

    /**
     * Zipkin B3：traceId（兼容老系统）。
     */
    public static final String X_B3_TRACE_ID = "X-B3-TraceId";

    /**
     * Zipkin B3：spanId。
     */
    public static final String X_B3_SPAN_ID = "X-B3-SpanId";

    /**
     * Zipkin B3：parentSpanId。
     */
    public static final String X_B3_PARENT_SPAN_ID = "X-B3-ParentSpanId";

    /**
     * Zipkin B3：采样标志。
     */
    public static final String X_B3_SAMPLED = "X-B3-Sampled";

    private LogHttpHeaders() {
    }
}
