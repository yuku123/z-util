package com.zifang.util.visuallization.swing.manager.tree;

public class TreeNode {
    private String id;
    private String name;
    private String parentId;

    public TreeNode() {
    }

    public TreeNode(String id, String name, String parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

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
