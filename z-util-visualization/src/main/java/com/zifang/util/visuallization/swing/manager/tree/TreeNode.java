package com.zifang.util.visuallization.swing.manager.tree;

/**
 * 树形节点数据类
 * 存储树形节点的ID、名称和父节点ID信息
 */
public class TreeNode {
    private String id;
    private String name;
    private String parentId;

    /**
     * 创建树节点
     */
    public TreeNode() {
    }

    /**
     * 创建树节点
     * @param id 节点唯一标识
     * @param name 节点显示名称
     * @param parentId 父节点ID（null表示根节点）
     */
    public TreeNode(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    /**
     * 获取节点ID
     * @return 节点ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置节点ID
     * @param id 节点ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取节点名称
     * @return 节点名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置节点名称
     * @param name 节点名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取父节点ID
     * @return 父节点ID，null表示根节点
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置父节点ID
     * @param parentId 父节点ID
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "TreeNode{id=" + id + ", name=" + name + ", parentId=" + parentId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return java.util.Objects.equals(id, treeNode.id) &&
               java.util.Objects.equals(name, treeNode.name) &&
               java.util.Objects.equals(parentId, treeNode.parentId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, parentId);
    }
}
