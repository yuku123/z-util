package com.zifang.util.core.pattern.chain;

/**
 * 命名处理器
 * <p>
 * 为处理器添加名称，方便调试和日志记录
 *
 * @param <C> 上下文类型
 * @author zifang
 */
public class NamedProcessor<C extends ChainContext<?, ?>> implements Processor<C> {

    private final String name;
    private final Processor<C> delegate;

    public NamedProcessor(String name, Processor<C> delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    @Override
    public ProcessorResult process(C context) {
        return delegate.process(context);
    }

    public String getName() {
        return name;
    }

    public Processor<C> getDelegate() {
        return delegate;
    }

    @Override
    public String toString() {
        return "NamedProcessor{name='" + name + "'}";
    }
}
