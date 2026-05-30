package com.zifang.util.core.pattern.memento;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 备忘录发起人（Originator）- 增强版
 * <p>
 * 功能特性：
 * <ul>
 *   <li>支持状态验证</li>
 *   <li>支持状态比较</li>
 *   <li>支持懒加载状态</li>
 *   <li>支持状态拷贝</li>
 *   <li>支持与MementoContext联动</li>
 * </ul>
 *
 * @param <T> 状态类型
 * @author zifang
 */
/**
 * Originator类。
 */
public class Originator<T> {

    private T state;
    private final StateValidator<T> validator;
    private final StateCopier<T> copier;

    @FunctionalInterface
/**
 * StateValidator接口。
 */
    public interface StateValidator<T> {
        boolean validate(T state);
    }

    @FunctionalInterface
/**
 * StateCopier接口。
 */
    public interface StateCopier<T> {
        T copy(T original);
    }

    /**
     * Originator方法。
     */
    public Originator() {
        this(null, null);
    }

    /**
     * Originator方法。
     *      * @param validator StateValidatorT类型参数
     */
    public Originator(StateValidator<T> validator) {
        this(validator, null);
    }

    /**
     * Originator方法。
     *      * @param validator StateValidatorT类型参数
     * @param copier StateCopierT类型参数
     */
    public Originator(StateValidator<T> validator, StateCopier<T> copier) {
        this.validator = validator;
        this.copier = copier;
    }

    /**
     * 保存当前状态到备忘录
     */
    /**
     * save方法。
     * @return Memento<T>类型返回值
     */
    public Memento<T> save() {
        return new Memento<>(state, System.currentTimeMillis());
    }

    /**
     * 保存当前状态到备忘录（带标签）
     */
    /**
     * save方法。
     *      * @param label String类型参数
     * @return Memento<T>类型返回值
     */
    public Memento<T> save(String label) {
        return new Memento<>(state, System.currentTimeMillis(), label);
    }

    /**
     * 从备忘录恢复状态
     */
    /**
     * restore方法。
     *      * @param memento MementoT类型参数
     * @return boolean类型返回值
     */
    public boolean restore(Memento<T> memento) {
        if (memento == null) {
            return false;
        }
        return restoreTo(memento.getState());
    }

    /**
     * 恢复到指定状态
     */
    /**
     * restoreTo方法。
     *      * @param newState T类型参数
     * @return boolean类型返回值
     */
    public boolean restoreTo(T newState) {
        if (validator != null && !validator.validate(newState)) {
            return false;
        }
        this.state = newState;
        return true;
    }

    /**
     * 安全设置状态（验证后设置）
     */
    /**
     * setState方法。
     *      * @param newState T类型参数
     * @return boolean类型返回值
     */
    public boolean setState(T newState) {
        if (validator != null && !validator.validate(newState)) {
            return false;
        }
        this.state = newState;
        return true;
    }

    /**
     * 安全设置状态（带错误回调）
     */
    /**
     * setState方法。
     *      * @param newState T类型参数
     * @param errorHandler ConsumerString类型参数
     * @return boolean类型返回值
     */
    public boolean setState(T newState, Consumer<String> errorHandler) {
        if (validator != null && !validator.validate(newState)) {
            if (errorHandler != null) {
                errorHandler.accept("State validation failed");
            }
            return false;
        }
        this.state = newState;
        return true;
    }

    /**
     * 获取当前状态
     */
    /**
     * getState方法。
     * @return T类型返回值
     */
    public T getState() {
        return state;
    }

    /**
     * 获取状态副本（如果copier已设置）
     */
    /**
     * getStateCopy方法。
     * @return Optional<T>类型返回值
     */
    public Optional<T> getStateCopy() {
        if (copier != null && state != null) {
            return Optional.of(copier.copy(state));
        }
        return Optional.empty();
    }

    /**
     * 比较两个状态是否相等
     */
    /**
     * stateEquals方法。
     *      * @param other T类型参数
     * @return boolean类型返回值
     */
    public boolean stateEquals(T other) {
        return Objects.equals(state, other);
    }

    /**
     * 比较当前状态与指定备忘录的状态
     */
    /**
     * stateEquals方法。
     *      * @param memento MementoT类型参数
     * @return boolean类型返回值
     */
    public boolean stateEquals(Memento<T> memento) {
        if (memento == null) {
            return state == null;
        }
        return Objects.equals(state, memento.getState());
    }

    /**
     * 创建状态更新器
     */
    /**
     * updater方法。
     * @return StateUpdater<T>类型返回值
     */
    public StateUpdater<T> updater() {
        return new StateUpdater<>(this);
    }

    /**
     * 与Context联动：保存状态
     */
    /**
     * saveTo方法。
     *      * @param context MementoContextT类型参数
     */
    public void saveTo(MementoContext<T> context) {
        context.save(state);
    }

    /**
     * 与Context联动：保存状态（带标签）
     */
    /**
     * saveTo方法。
     *      * @param context MementoContextT类型参数
     * @param label String类型参数
     */
    public void saveTo(MementoContext<T> context, String label) {
        context.save(state, label);
    }

    /**
     * 与Context联动：恢复到Context的当前状态
     */
    /**
     * restoreFrom方法。
     *      * @param context MementoContextT类型参数
     */
    public void restoreFrom(MementoContext<T> context) {
        T state = context.current();
        if (state != null) {
            restoreTo(state);
        }
    }

    /**
     * 状态更新器
     */
    public static class StateUpdater<T> {
        private final Originator<T> originator;
        private T backup;

        private StateUpdater(Originator<T> originator) {
            this.originator = originator;
        }

        /**
         * 更新前备份当前状态
         */
    /**
     * backup方法。
     * @return StateUpdater<T>类型返回值
     */
        public StateUpdater<T> backup() {
            this.backup = originator.state;
            return this;
        }

        /**
         * 执行更新
         */
    /**
     * update方法。
     *      * @param newState T类型参数
     * @return boolean类型返回值
     */
        public boolean update(T newState) {
            return originator.setState(newState);
        }

        /**
         * 回滚到备份状态
         */
    /**
     * rollback方法。
     */
        public void rollback() {
            if (backup != null) {
                originator.state = backup;
            }
        }

        /**
         * 获取发起人
         */
    /**
     * getOriginator方法。
     * @return Originator<T>类型返回值
     */
        public Originator<T> getOriginator() {
            return originator;
        }
    }

    /**
     * 备忘录实现
     */
    public static class Memento<T> {
        private final T state;
        private final long timestamp;
        private final String label;

    /**
     * Memento方法。
     *      * @param state T类型参数
     * @param timestamp long类型参数
     */
        public Memento(T state, long timestamp) {
            this(state, timestamp, null);
        }

    /**
     * Memento方法。
     *      * @param state T类型参数
     * @param timestamp long类型参数
     * @param label String类型参数
     */
        public Memento(T state, long timestamp, String label) {
            this.state = state;
            this.timestamp = timestamp;
            this.label = label;
        }

    /**
     * getState方法。
     * @return T类型返回值
     */
        public T getState() { return state; }
    /**
     * getTimestamp方法。
     * @return long类型返回值
     */
        public long getTimestamp() { return timestamp; }
    /**
     * getLabel方法。
     * @return String类型返回值
     */
        public String getLabel() { return label; }
    }
}
