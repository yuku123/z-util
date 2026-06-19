package com.zifang.util.core.trace.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * 日志门面：SLF4J 之上的薄封装。
 * <p>
 * 设计目的：
 * <ul>
 *   <li>统一项目内"取 Logger"的方式，避免每处都写 {@code LoggerFactory.getLogger(Xxx.class)}</li>
 *   <li>提供"惰性求值"日志（{@link #debug(Logger, Supplier)})，避免在日志级别关闭时仍拼接大字符串</li>
 *   <li>在 debug/trace 关闭时短路（{@link #trace} 各种重载）</li>
 * </ul>
 *
 * <h3>使用</h3>
 * <pre>{@code
 *   private static final Logger LOG = Logs.of(MyService.class);
 *
 *   LOG.info("user login: {}", userId);
 *   Logs.info(LOG, "saved order: {}", orderId);
 *
 *   // 大对象惰性输出：debug 关时不会调 toString
 *   Logs.debug(LOG, () -> "huge dump: " + expensiveRender(obj));
 * }</pre>
 *
 * <h3>为什么不用 Lombok @Slf4j</h3>
 * 因为本项目要尽量少编译期依赖；且保留 {@link #debug(Logger, Supplier)} 这类工具方法。
 */
public final class Logs {

    private Logs() {
    }

    /**
     * 按 Class 取 Logger。
     */
    public static Logger of(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 按名字取 Logger。
     */
    public static Logger of(String name) {
        return LoggerFactory.getLogger(name);
    }

    // ===== 顶层便捷方法（少写 LOG.） =====

    public static void trace(Logger log, String msg) {
        if (log.isTraceEnabled()) log.trace(msg);
    }

    public static void trace(Logger log, String fmt, Object arg) {
        if (log.isTraceEnabled()) log.trace(fmt, arg);
    }

    public static void trace(Logger log, String fmt, Object a, Object b) {
        if (log.isTraceEnabled()) log.trace(fmt, a, b);
    }

    public static void trace(Logger log, String fmt, Object... args) {
        if (log.isTraceEnabled()) log.trace(fmt, args);
    }

    public static void debug(Logger log, Supplier<String> msg) {
        if (log.isDebugEnabled()) log.debug(msg.get());
    }

    public static void info(Logger log, String msg) {
        if (log.isInfoEnabled()) log.info(msg);
    }

    public static void info(Logger log, String fmt, Object arg) {
        if (log.isInfoEnabled()) log.info(fmt, arg);
    }

    public static void info(Logger log, String fmt, Object a, Object b) {
        if (log.isInfoEnabled()) log.info(fmt, a, b);
    }

    public static void info(Logger log, String fmt, Object... args) {
        if (log.isInfoEnabled()) log.info(fmt, args);
    }

    public static void warn(Logger log, String msg) {
        log.warn(msg);
    }

    public static void warn(Logger log, String fmt, Object... args) {
        log.warn(fmt, args);
    }

    public static void warn(Logger log, String msg, Throwable t) {
        log.warn(msg, t);
    }

    public static void error(Logger log, String msg) {
        log.error(msg);
    }

    public static void error(Logger log, String fmt, Object... args) {
        log.error(fmt, args);
    }

    public static void error(Logger log, String msg, Throwable t) {
        log.error(msg, t);
    }

    // ===== 异常统一处理（最常用模式） =====

    /**
     * 记录异常：消息 + cause；用于 catch 块里"既要记日志又要保留异常信息"的场景。
     */
    public static void error(Logger log, Throwable t) {
        log.error(t.getMessage(), t);
    }
}
