package com.zifang.util.core.pattern.composite;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 树构建器（TreeBuilder）
 * <p>
 * 提供流式API构建树结构
 *
 * @param <T> 组件类型
 * @author zifang
 */
public class TreeBuilder<T extends Composite<T>> {

    private final Composite<T> root;
    private final Map<String, T> nodeMap;
    private final TreeBuilder<T> parentBuilder;
    private T currentNode;

    @SuppressWarnings("unchecked")
    private TreeBuilder(String name) {
        this.root = (Composite<T>) new Composite<>(name);
        this.nodeMap = new HashMap<>();
        this.nodeMap.put(name, (T) this.root);
        this.currentNode = (T) this.root;
        this.parentBuilder = null;
    }

    @SuppressWarnings("unchecked")
    private TreeBuilder(Composite<T> node, TreeBuilder<T> parent) {
        this.root = parent.root;
        this.nodeMap = parent.nodeMap;
        this.currentNode = (T) node;
        this.parentBuilder = parent;
    }

    /**
     * 创建新的树构建器
     */
    public static <T extends Composite<T>> TreeBuilder<T> create(String rootName) {
        return new TreeBuilder<>(rootName);
    }

    /**
     * 创建新的树构建器
     */
    public static <T extends Composite<T>> TreeBuilder<T> create(Composite<T> root) {
        TreeBuilder<T> builder = new TreeBuilder<>(root.getName());
        builder.nodeMap.clear();
        builder.nodeMap.put(root.getId(), (T) root);
        builder.root.addChild((T) root);
        return builder;
    }

    /**
     * 添加子节点
     */
    public TreeBuilder<T> addChild(String name) {
        Composite<T> child = new Composite<>(name);
        return addChild((T) child);
    }

    /**
     * 添加子节点
     */
    public TreeBuilder<T> addChild(T node) {
        currentNode.addChild(node);
        nodeMap.put(node.getId(), node);
        return new TreeBuilder<>(node, this);
    }

    /**
     * 添加多个子节点
     */
    public TreeBuilder<T> addChildren(String... names) {
        for (String name : names) {
            addChild(name);
        }
        return this;
    }

    /**
     * 返回父节点
     */
    public TreeBuilder<T> up() {
        if (parentBuilder == null) {
            throw new IllegalStateException("Already at root");
        }
        return parentBuilder;
    }

    /**
     * 进入子节点
     */
    public TreeBuilder<T> down(String name) {
        T child = currentNode.getChildren().stream()
                .filter(n -> n.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Child not found: " + name));
        return new TreeBuilder<>(child, this);
    }

    /**
     * 移动到指定节点
     */
    public TreeBuilder<T> moveTo(String id) {
        T node = nodeMap.get(id);
        if (node == null) {
            throw new IllegalArgumentException("Node not found: " + id);
        }
        return new TreeBuilder<>(node, findParentBuilder(node));
    }

    /**
     * 移动到根节点
     */
    public TreeBuilder<T> moveToRoot() {
        return new TreeBuilder<>(root.getName());
    }

    private TreeBuilder<T> findParentBuilder(T node) {
        return findParentBuilderHelper((T) root, node, null);
    }

    @SuppressWarnings("unchecked")
    private TreeBuilder<T> findParentBuilderHelper(T current, T target, TreeBuilder<T> parent) {
        if (current.equals(target)) {
            return parent != null ? parent : new TreeBuilder<>(root.getName());
        }
        for (T child : current.getChildren()) {
            TreeBuilder<T> result = findParentBuilderHelper(child, target, new TreeBuilder<>(current, parent != null ? parent : new TreeBuilder<>(root.getName())));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 设置当前节点名称
     */
    public TreeBuilder<T> name(String name) {
        currentNode.setName(name);
        return this;
    }

    /**
     * 设置当前节点元数据
     */
    public TreeBuilder<T> meta(String key, Object value) {
        ((Composite<T>) currentNode).setMetadata(key, value);
        return this;
    }

    /**
     * 获取当前节点
     */
    public T current() {
        return currentNode;
    }

    /**
     * 获取根节点
     */
    public T root() {
        return (T) root;
    }

    /**
     * 构建树
     */
    public Tree<T> build() {
        return new Tree<>((T) root);
    }

    /**
     * 从平面数据构建树
     */
    public static <T extends Composite<T>> Tree<T> buildFromList(
            List<T> nodes,
            Function<T, String> idGetter,
            Function<T, String> parentIdGetter) {

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes cannot be empty");
        }

        Map<String, T> nodeMap = new HashMap<>();
        for (T node : nodes) {
            nodeMap.put(idGetter.apply(node), node);
        }

        T root = null;
        for (T node : nodes) {
            String parentId = parentIdGetter.apply(node);
            if (parentId == null || parentId.isEmpty() || !nodeMap.containsKey(parentId)) {
                root = node;
            } else {
                T parent = nodeMap.get(parentId);
                parent.addChild(node);
            }
        }

        if (root == null) {
            throw new IllegalArgumentException("No root node found");
        }

        return new Tree<>(root);
    }

    /**
     * 从层级数据构建树
     */
    public static <T extends Composite<T>> Tree<T> buildFromHierarchy(
            List<HierarchyItem> items,
            Function<HierarchyItem, String> idGetter,
            Function<HierarchyItem, String> nameGetter,
            Function<HierarchyItem, String> parentIdGetter) {

        List<T> nodes = new ArrayList<>();
        for (HierarchyItem item : items) {
            @SuppressWarnings("unchecked")
            Composite<T> node = (Composite<T>) new Composite<>(idGetter.apply(item), nameGetter.apply(item));
            nodes.add((T) node);
        }

        return buildFromList(nodes, idGetter, parentIdGetter);
    }

    /**
     * 层级数据项
     */
    public interface HierarchyItem {
        String getId();
        String getName();
        String getParentId();
    }
}
