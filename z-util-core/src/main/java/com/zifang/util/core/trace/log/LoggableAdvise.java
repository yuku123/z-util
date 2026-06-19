package com.zifang.util.core.trace.log;

import com.zifang.util.aop.Advise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.reflect.Method;

/**
 * {@link Loggable} 注解的拦截器实现。
 * <p>
 * 用法：在业务方法上加 {@code @Loggable} + {@code @Intercept(LoggableAdvise.class)}，
 * 然后用 {@code ProxyFactory.wrap(...)} 包一层。
 *
 * <pre>{@code
 *   public class OrderService {
 *       @com.zifang.util.core.trace.log.Loggable
 *       @com.zifang.util.aop.Intercept(LoggableAdvise.class)
 *       public OrderDto create(String userId, String productId) { ... }
 *   }
 * }</pre>
 *
 * <p>注意：本类是多线程并发安全的——所有方法每次调用都重新读取注解。
 *
 * @param <T> 目标对象类型（与 z-util-aop 的 {@link Advise} 一致）
 */
public class LoggableAdvise<T> implements Advise<T> {

    private static Loggable.Level levelOrWarn(Loggable ann, long ms) {
        if (ann.slowThresholdMs() > 0 && ms >= ann.slowThresholdMs()) {
            return Loggable.Level.WARN;
        }
        return ann.level();
    }

    private static void log(Marker marker, Logger log, Loggable.Level lvl, String fmt, Object... args) {
        switch (lvl) {
            case TRACE:
                if (marker != null) log.trace(marker, fmt, args);
                else log.trace(fmt, args);
                break;
            case DEBUG:
                if (marker != null) log.debug(marker, fmt, args);
                else log.debug(fmt, args);
                break;
            case INFO:
                if (marker != null) log.info(marker, fmt, args);
                else log.info(fmt, args);
                break;
            case WARN:
                if (marker != null) log.warn(marker, fmt, args);
                else log.warn(fmt, args);
                break;
            case ERROR:
                if (marker != null) log.error(marker, fmt, args);
                else log.error(fmt, args);
                break;
        }
    }

    private static String safeArgs(Object[] args) {
        if (args == null || args.length == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(safeToString(args[i]));
        }
        return sb.append(']').toString();
    }

    private static String safeResult(Object r) {
        return safeToString(r);
    }

    /**
     * toString 兜底：toString 抛异常时只输出类名。
     */
    private static String safeToString(Object o) {
        if (o == null) return "null";
        try {
            String s = o.toString();
            if (s == null) return o.getClass().getSimpleName() + "@null";
            return s;
        } catch (Throwable t) {
            return o.getClass().getSimpleName() + "@(toString-failed:" + t.getClass().getSimpleName() + ")";
        }
    }

    @Override
    public Object around(T target, Method method, Object[] args, Advise.Chain chain) throws Throwable {
        Loggable ann = method.getAnnotation(Loggable.class);
        if (ann == null) {
            return chain.proceed();
        }
        Logger log = LoggerFactory.getLogger(method.getDeclaringClass());
        Marker marker = ann.marker().isEmpty() ? null : MarkerFactory.getMarker(ann.marker());

        long t0 = System.nanoTime();
        boolean entered = false;
        try {
            if (ann.logArgs()) {
                log(marker, log, ann.level(), "enter {}.{}({})",
                        method.getDeclaringClass().getSimpleName(),
                        method.getName(),
                        safeArgs(args));
            }
            entered = true;
            Object result = chain.proceed();
            long ms = (System.nanoTime() - t0) / 1_000_000L;
            if (ann.logResult()) {
                log(marker, log, levelOrWarn(ann, ms), "exit  {}.{} -> {} took={}ms",
                        method.getDeclaringClass().getSimpleName(),
                        method.getName(),
                        safeResult(result), ms);
            }
            return result;
        } catch (Throwable t) {
            long ms = (System.nanoTime() - t0) / 1_000_000L;
            if (ann.logException()) {
                log.error(marker, "{}.{} threw {} after {}ms: {}",
                        method.getDeclaringClass().getSimpleName(),
                        method.getName(),
                        t.getClass().getSimpleName(), ms, t.getMessage(), t);
            } else if (entered) {
                log(marker, log, ann.level(), "{}.{} failed after {}ms",
                        method.getDeclaringClass().getSimpleName(),
                        method.getName(), ms);
            }
            throw t;
        }
    }
}
