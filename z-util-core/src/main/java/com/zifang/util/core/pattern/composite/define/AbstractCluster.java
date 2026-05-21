package com.zifang.util.core.pattern.composite.define;

import java.util.Collection;

/**
 * 集群抽象基类。
 * <p>
 * 实现 ICluster 接口，提供集群的基本功能。
 *
 * @author zifang
 * @see ICluster
 */
public abstract class AbstractCluster implements ICluster {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Collection<INode> members() {
        return null;
    }
}
