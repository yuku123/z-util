package com.zifang.util.core.state;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * 状态机中的"转移"定义。
 * <p>
 * 一条转移描述了在 {@code from} 状态下、收到 {@code event} 时，如何切换到 {@code to} 状态：
 * <ul>
 *   <li>{@code guard}：守卫条件。上下文通过时才能触发，否则保持原状态并触发 {@link StateListener#onTransitionRefused}</li>
 *   <li>{@code action}：转移执行期间的动作（可选）。在状态切换为 to 之前执行；抛错时转移中止并触发 {@link StateListener#onError}</li>
 * </ul>
 * 转移由 {@link StateMachineBuilder} 构造，无需用户直接 new。
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
/**
 * Transition类。
 */
public class Transition<S, E, C> {

    private final S from;
    private final E event;
    private final S to;
    private final Predicate<C> guard;
    private final BiConsumer<S, C> action;

    Transition(S from, E event, S to, Predicate<C> guard, BiConsumer<S, C> action) {
        this.from = Objects.requireNonNull(from, "from");
        this.event = Objects.requireNonNull(event, "event");
        this.to = Objects.requireNonNull(to, "to");
        this.guard = guard;
        this.action = action;
    }

    /**
     * getFrom方法。
     * @return S类型返回值
     */
    public S getFrom() {
        return from;
    }

    /**
     * getEvent方法。
     * @return E类型返回值
     */
    public E getEvent() {
        return event;
    }

    /**
     * getTo方法。
     * @return S类型返回值
     */
    public S getTo() {
        return to;
    }

    /**
     * 判断在当前上下文是否允许触发本转移。无 guard 时永远返回 true。
     */
    /**
     * isAllowed方法。
     *      * @param context C类型参数
     * @return boolean类型返回值
     */
    public boolean isAllowed(C context) {
        return guard == null || guard.test(context);
    }

    /**
     * 执行转移关联的动作。无 action 时直接返回。
     */
    /**
     * executeAction方法。
     *      * @param fromState S类型参数
     * @param context C类型参数
     */
    public void executeAction(S fromState, C context) {
        if (action != null) {
            action.accept(fromState, context);
        }
    }
}
