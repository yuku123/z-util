package com.zifang.util.visuallization.swing.manager.tree;

import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;

import static org.junit.Assert.*;

/**
 * TreeComponent 单元测试
 * 测试树形组件类的继承行为
 */
public class TreeComponentTest {

    @Test
    public void testTreeComponentCreation() {
        TreeComponent tree = new TreeComponent();
        assertNotNull(tree);
    }

    @Test
    public void testTreeComponentIsJTree() {
        TreeComponent tree = new TreeComponent();
        assertTrue(tree instanceof javax.swing.JTree);
    }

    @Test
    public void testTreeComponentDefaultConstructor() {
        TreeComponent tree = new TreeComponent();
        // JTree默认构造创建一个空模型
        assertNotNull(tree.getModel());
    }
}