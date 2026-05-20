package com.zifang.util.core.pattern.composite.tree;

import com.zifang.util.core.lang.tuples.Triplet;

import java.util.*;

/**
 * 叶子节点包装器
 * <p>
 * 将任意对象包装为ILeaf节点
 *
 * @param <A> ID类型
 * @param <B> 父ID类型
 * @param <C> 实际对象类型
 * @author zifang
 */
public class LeafWrapper<A, B, C> extends Triplet<A, B, C> implements ILeaf {

    private String name;
    private ILeaf parent;
    private List<ILeaf> subLeaves;
    private Map<String, Object> metadata;

    public LeafWrapper(A currentId, B parentId, C bean) {
        super(currentId, parentId, bean);
        this.name = bean != null ? bean.toString() : null;
    }

    @Override
    public List<ILeaf> getSubLeaves() {
        return subLeaves != null ? subLeaves : Collections.emptyList();
    }

    @Override
    public ILeaf getParentLeaf() {
        return parent;
    }

    @Override
    public void appendSubLeaf(ILeaf leaf) {
        if (subLeaves == null) {
            subLeaves = new ArrayList<>();
        }
        subLeaves.add(leaf);
        leaf.setParent(this);
    }

    @Override
    public void setParent(ILeaf leaf) {
        this.parent = leaf;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        A id = getA();
        return id != null ? id.toString() : null;
    }

    public B getParentId() {
        return getB();
    }

    public C getBean() {
        return getC();
    }

    public void setMeta(String key, Object value) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        metadata.put(key, value);
    }

    public Object getMeta(String key) {
        return metadata != null ? metadata.get(key) : null;
    }

    public Map<String, Object> getMetadata() {
        return metadata != null ? Collections.unmodifiableMap(metadata) : Collections.emptyMap();
    }

    @Override
    public void removeSubLeaf(ILeaf leaf) {
        if (subLeaves != null) {
            subLeaves.remove(leaf);
            leaf.setParent(null);
        }
    }

    @Override
    public String toString() {
        return "LeafWrapper{" +
                "id=" + getA() +
                ", name='" + name + '\'' +
                ", parentId=" + getB() +
                ", bean=" + getC() +
                ", childrenCount=" + (subLeaves != null ? subLeaves.size() : 0) +
                '}';
    }
}
