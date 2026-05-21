package com.zifang.util.core.pattern.composite.define;

import java.util.List;

/**
 * 节点抽象基类。
 * <p>
 * 实现 INode 接口，提供节点的基本属性和默认实现。
 *
 * @author zifang
 * @see INode
 */
public class AbstractNode implements INode {

    protected String id;

    protected String name;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public List<INode> getCombinedNode() {
        return null;
    }
}
