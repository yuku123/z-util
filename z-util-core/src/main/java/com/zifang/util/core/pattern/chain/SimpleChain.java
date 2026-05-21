package com.zifang.util.core.pattern.chain;

import java.util.*;

/**
 * 简单链实现
 *
 * @param <C> 上下文类型
 * @author zifang
 */
public class SimpleChain<C extends ChainContext<?, ?>> implements Chain<C> {

    private final String name;
    private final List<Processor<C>> processors;

    public SimpleChain() {
        this("SimpleChain-" + UUID.randomUUID().toString().substring(0, 8));
    }

    public SimpleChain(String name) {
        this.name = name;
        this.processors = new ArrayList<>();
    }

    public SimpleChain(Processor<C> processor) {
        this();
        if (processor != null) {
            this.processors.add(processor);
        }
    }

    public SimpleChain(String name, Processor<C> processor) {
        this.name = name;
        this.processors = new ArrayList<>();
        if (processor != null) {
            this.processors.add(processor);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Processor<C>> getProcessors() {
        return Collections.unmodifiableList(processors);
    }

    @Override
    public ProcessorResult process(C context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        ProcessorResult lastResult = ProcessorResult.CONTINUE;

        for (Processor<C> processor : processors) {
            lastResult = processor.process(context);
            if (!lastResult.shouldContinue()) {
                break;
            }
        }

        return lastResult;
    }

    @Override
    public Chain<C> addProcessor(Processor<C> processor) {
        if (processor != null) {
            this.processors.add(processor);
        }
        return this;
    }

    @Override
    public Chain<C> addFirst(Processor<C> processor) {
        if (processor != null) {
            this.processors.add(0, processor);
        }
        return this;
    }

    @Override
    public Chain<C> addAt(int index, Processor<C> processor) {
        if (processor != null && index >= 0 && index <= processors.size()) {
            this.processors.add(index, processor);
        }
        return this;
    }

    @Override
    public Chain<C> remove(Processor<C> processor) {
        this.processors.remove(processor);
        return this;
    }

    @Override
    public Chain<C> clear() {
        this.processors.clear();
        return this;
    }

    @Override
    public Chain<C> prepend(Chain<C> other) {
        if (other != null && !other.isEmpty()) {
            List<Processor<C>> otherProcessors = new ArrayList<>(other.getProcessors());
            otherProcessors.addAll(this.processors);
            this.processors.clear();
            this.processors.addAll(otherProcessors);
        }
        return this;
    }

    @Override
    public Chain<C> append(Chain<C> other) {
        if (other != null && !other.isEmpty()) {
            this.processors.addAll(other.getProcessors());
        }
        return this;
    }

    @Override
    public String toString() {
        return "SimpleChain{" +
                "name='" + name + '\'' +
                ", processors=" + processors.size() +
                '}';
    }
}
