package com.zifang.util.core.trace.log;

import com.zifang.util.core.trace.TraceContext;
import com.zifang.util.core.trace.TraceContextHolder;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 日志上下文（MDC）的工具类。
 * <p>
 * MDC（Mapped Diagnostic Context）是 SLF4J 提供的线程局部"键值对"，
 * 配合日志模板里的 {@code %X{key}} 占位符可自动出现在每条日志里。
 * <p>
 * 本类做两件事：
 * <ol>
 *   <li>薄封装 SLF4J {@link MDC}，少写 {@code MDC.put/remove} 模板</li>
 *   <li>提供 {@link #with(Map, Runnable)} / {@link #with(Map, Callable)}，
 *       让"加 MDC 字段→执行业务→清理"原子化（异常路径也保证清理）</li>
 * </ol>
 *
 * <h3>traceId/spanId 集成</h3>
 * 调用 {@link TraceContextHolder#startNew()} 时，traceId/spanId 已经自动写入 MDC，
 * 不需要本类再 put。但 {@link #currentTraceId()} 提供一个无 trace 上下文时的
 * 默认值（{@code "no-trace"}），方便日志模板里直接用 {@code %X{traceId}}。
 *
 * <h3>典型使用</h3>
 * <pre>{@code
 *   // HTTP 入口处：
 *   TraceContextHolder.startWith(TraceIds.parseTraceparent(req.getHeader("traceparent")));
 *   LogContext.put("userId", user.getId());
 *
 *   // 异步任务里（线程切换）丢失 MDC，用 with 重新注入：
 *   LogContext.with(Map.of("userId", uid), () -> {
 *       executor.submit(this::doWork);
 *   });
 * }</pre>
 */
public final class LogContext {

    /** 没有 trace 上下文时，traceId 字段填这个值。 */
    public static final String NO_TRACE = "no-trace";

    private LogContext() {}

    // ===== 单字段操作 =====

    public static void put(String key, String value) {
        if (value == null) MDC.remove(key);
        else MDC.put(key, value);
    }

    public static String get(String key) {
        return MDC.get(key);
    }

    public static void remove(String key) {
        MDC.remove(key);
    }

    public static void clear() {
        MDC.clear();
    }

    // ===== 批量操作 =====

    /**
     * 把 map 里所有键值写入 MDC（已有的同 key 会被覆盖）。
     */
    public static void putAll(Map<String, String> entries) {
        if (entries == null) return;
        for (Map.Entry<String, String> e : entries.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    /**
     * 拿到当前线程 MDC 的快照（防御性 copy）。
     */
    public static Map<String, String> snapshot() {
        Map<String, String> ctx = MDC.getCopyOfContextMap();
        return ctx == null ? new HashMap<>() : ctx;
    }

    /**
     * 把快照回放（用于异步线程之间传递 MDC）。
     */
    public static void restore(Map<String, String> snapshot) {
        if (snapshot == null) return;
        for (Map.Entry<String, String> e : snapshot.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    // ===== 作用域：with（try-finally 风格的 MDC 块） =====

    /**
     * 在指定 MDC 上下文里执行任务，结束后恢复原状（异常也保证）。
     */
    public static void with(Map<String, String> entries, Runnable task) {
        Map<String, String> prev = snapshot();
        try {
            putAll(entries);
            task.run();
        } finally {
            // 只清掉本次新加的，原有的 restore
            clear();
            restore(prev);
        }
    }

    /**
     * Callable 版本，透传返回值和受检异常。
     */
    public static <T> T with(Map<String, String> entries, Callable<T> task) throws Exception {
        Map<String, String> prev = snapshot();
        try {
            putAll(entries);
            return task.call();
        } finally {
            clear();
            restore(prev);
        }
    }

    // ===== trace 上下文便捷访问 =====

    /** 当前 traceId（无上下文时返回 {@link #NO_TRACE}）。 */
    public static String currentTraceId() {
        TraceContext c = TraceContextHolder.current();
        return c == null ? NO_TRACE : c.traceId();
    }

    /** 当前 spanId（无上下文时返回 {@code "no-span"}）。 */
    public static String currentSpanId() {
        TraceContext c = TraceContextHolder.current();
        return c == null ? "no-span" : c.spanId();
    }
}
