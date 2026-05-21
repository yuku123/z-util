package com.zifang.util.core.pattern.chain;

import java.util.*;

/**
 * 命名链实现
 *
 * @param <C> 上下文类型
 * @author zifang
 */
public class NamedChain<C extends ChainContext<?, ?>> extends SimpleChain<C> {

    public NamedChain(String name) {
        super(name);
    }

    public NamedChain(String name, Processor<C> processor) {
        super(name, processor);
    }
}
