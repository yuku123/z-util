package com.zifang.util.visuallization.swing.manager.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 树形节点注册器
 * 用于注册和管理树形结构中的节点信息
 */
public class RegisterTreeNode {

    private List<TreeNode> treeNodes = new ArrayList<>();

    /**
     * 创建树节点注册器
     */
    public RegisterTreeNode() {
    }

    /**
     * 获取所有注册的节点
     * @return 节点列表
     */
    public List<TreeNode> getTreeNodes() {
        return treeNodes;
    }

    /**
     * 设置节点列表
     * @param treeNodes 节点列表
     */
    public void setTreeNodes(List<TreeNode> treeNodes) {
        this.treeNodes = treeNodes;
    }

    /**
     * 注册一个节点（支持链式调用）
     * @param treeNode 要注册的节点
     * @return 当前注册器实例
     */
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
