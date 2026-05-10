package com.zifang.util.visuallization.swing.manager.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterTreeNode {

    private List<TreeNode> treeNodes = new ArrayList<>();

    public RegisterTreeNode() {
    }

    public List<TreeNode> getTreeNodes() {
        return treeNodes;
    }

    public void setTreeNodes(List<TreeNode> treeNodes) {
        this.treeNodes = treeNodes;
    }

    public RegisterTreeNode register(TreeNode treeNode) {
        treeNodes.add(treeNode);
        return this;
    }

    @Override
    public String toString() {
        return "RegisterTreeNode{treeNodes=" + treeNodes + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterTreeNode that = (RegisterTreeNode) o;
        return Objects.equals(treeNodes, that.treeNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(treeNodes);
    }
}
