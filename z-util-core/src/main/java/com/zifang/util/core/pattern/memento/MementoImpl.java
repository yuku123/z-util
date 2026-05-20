package com.zifang.util.core.pattern.memento;

/**
 * 备忘录实现类
 *
 * @param <T> 状态类型
 * @author zifang
 */
public class MementoImpl<T> implements Memento<T> {

    private final T state;
    private final long timestamp;
    private final String label;
    private final String description;

    private MementoImpl(Builder<T> builder) {
        this.state = builder.state;
        this.timestamp = builder.timestamp;
        this.label = builder.label;
        this.description = builder.description;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> MementoImpl<T> of(T state) {
        return new Builder<T>().state(state).build();
    }

    public static <T> MementoImpl<T> of(T state, String label) {
        return new Builder<T>().state(state).label(label).build();
    }

    @Override
    public T getState() {
        return state;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static class Builder<T> {
        private T state;
        private long timestamp = System.currentTimeMillis();
        private String label;
        private String description;

        private Builder() {}

        public Builder<T> state(T state) {
            this.state = state;
            return this;
        }

        public Builder<T> timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder<T> label(String label) {
            this.label = label;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        public MementoImpl<T> build() {
            return new MementoImpl<>(this);
        }
    }
}