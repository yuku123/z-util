package com.zifang.util.core.trace.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法级日志注解：自动记录入参/出参/耗时/异常。
 * <p>
 * 配合 z-util-aop 使用：
 * <pre>{@code
 *   public class OrderService {
 *       @com.zifang.util.trace.log.Loggable
 *       public OrderDto create(String userId, String productId) {
 *           // 业务代码，无需手写日志
 *       }
 *   }
 *
 *   OrderService svc = (OrderService) ProxyFactory.wrap(new OrderServiceImpl());
 *   svc.create("alice", "P001");
 *   // 自动产出：
 *   //   [Loggable] enter OrderService.create(args=[alice, P001]) trace=xxx
 *   //   [Loggable] exit  OrderService.create -> OrderDto{...} took=12ms
 * }</pre>
 *
 * <h3>注意事项</h3>
 * <ul>
 *   <li>需要用 {@code ProxyFactory.wrap(...)} 创建代理才会生效</li>
 *   <li>入参 toString 可能很贵/含敏感信息，可通过 {@link #maskArgs()} 关闭</li>
 *   <li>返回值同理会暴露业务数据，可通过 {@link #logResult()} 关闭</li>
 * </ul>
 *
 * @see LoggableAdvise
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {

    /**
     * 日志级别：默认 INFO。
     */
    Level level() default Level.INFO;

    /**
     * 是否记录入参（默认 true，敏感方法可关）。
     */
    boolean logArgs() default true;

    /**
     * 是否记录返回值（默认 true）。
     */
    boolean logResult() default true;

    /**
     * 是否记录异常（默认 true，会用 ERROR 级别）。
     */
    boolean logException() default true;

    /**
     * 超过此毫秒数自动升级到 WARN，0 表示不升级。
     */
    long slowThresholdMs() default 0;

    /**
     * 标记 marker 名（可选，用于日志路由）。
     */
    String marker() default "";

    enum Level {TRACE, DEBUG, INFO, WARN, ERROR}
}
