package com.zifang.util.core.pattern.composite;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;

public class CompositeTest {

    // ==================== Component Tests ====================

    @Test
    public void testComponentBasicOperations() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite leaf = new Composite("leaf");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(leaf);

        assertEquals("root", root.getName());
        assertEquals(2, root.getChildCount());
        assertEquals(2, root.getChildren().size());
        assertTrue(root.isRoot());
        assertFalse(root.isLeaf());

        assertEquals(root, child1.getParent());
        assertTrue(leaf.isLeaf());
    }

    @Test
    public void testGetDepth() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        // root depth = 1, children depth = 2, grandchild depth = 3
        assertEquals(1, root.getDepth());
        assertEquals(2, child1.getDepth());
        assertEquals(2, child2.getDepth());
        assertEquals(3, grandchild.getDepth());
    }

    @Test
    public void testGetSubtreeSize() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        assertEquals(4, root.getSubtreeSize());
        assertEquals(2, child1.getSubtreeSize());
        assertEquals(1, child2.getSubtreeSize());
        assertEquals(1, grandchild.getSubtreeSize());
    }

    @Test
    public void testGetPathFromRoot() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        child1.addChild(grandchild);

        List<Component> path = grandchild.getPathFromRoot();
        assertEquals(3, path.size());
        assertEquals("root", path.get(0).getName());
        assertEquals("child1", path.get(1).getName());
        assertEquals("grandchild", path.get(2).getName());
    }

    @Test
    public void testTraversePreOrder() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        StringBuilder result = new StringBuilder();
        root.traversePreOrder(node -> result.append(node.getName()).append("-"));

        assertEquals("root-child1-child2-", result.toString());
    }

    @Test
    public void testTraversePostOrder() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        StringBuilder result = new StringBuilder();
        root.traversePostOrder(node -> result.append(node.getName()).append("-"));

        assertEquals("child1-child2-root-", result.toString());
    }

    @Test
    public void testFind() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        assertEquals(child2, root.find(node -> node.getName().equals("child2")));
        assertNull(root.find(node -> node.getName().equals("nonexistent")));
    }

    @Test
    public void testFindAll() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child");
        Composite child2 = new Composite("child");

        root.addChild(child1);
        root.addChild(child2);

        List<Component> found = root.findAll(node -> node.getName().equals("child"));
        assertEquals(2, found.size());
    }

    @Test
    public void testMap() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        List<String> names = root.map(Component::getName);
        assertEquals(3, names.size());
        assertTrue(names.contains("root"));
        assertTrue(names.contains("child1"));
        assertTrue(names.contains("child2"));
    }

    @Test
    public void testGetRoot() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        child1.addChild(grandchild);

        assertEquals(root, grandchild.getRoot());
        assertEquals(root, child1.getRoot());
        assertEquals(root, root.getRoot());
    }

    // ==================== Composite Tests ====================

    @Test
    public void testCompositeAddRemove() {
        Composite parent = new Composite("parent");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        parent.addChild(child1);
        parent.addChild(child2);

        assertEquals(2, parent.getChildCount());
        assertTrue(parent.containsChild(child1));

        parent.removeChild(child1);
        assertEquals(1, parent.getChildCount());
        assertFalse(parent.containsChild(child1));
        assertTrue(child1.isRoot());
    }

    @Test
    public void testCompositeAddAllChildren() {
        Composite parent = new Composite("parent");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        parent.addAllChildren(java.util.Arrays.asList(child1, child2));
        assertEquals(2, parent.getChildCount());
    }

    @Test
    public void testGetChildAt() {
        Composite parent = new Composite("parent");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        parent.addChild(child1);
        parent.addChild(child2);

        assertEquals(child1, parent.getChildAt(0));
        assertEquals(child2, parent.getChildAt(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetChildAtOutOfBounds() {
        Composite parent = new Composite("parent");
        parent.getChildAt(0);
    }

    @Test
    public void testReplaceChild() {
        Composite parent = new Composite("parent");
        Composite oldChild = new Composite("old");
        Composite newChild = new Composite("new");

        parent.addChild(oldChild);
        assertTrue(parent.replaceChild(oldChild, newChild));
        assertFalse(parent.containsChild(oldChild));
        assertTrue(parent.containsChild(newChild));
    }

    @Test
    public void testFilterChildren() {
        Composite parent = new Composite("parent");
        Composite child1 = new Composite("a");
        Composite child2 = new Composite("b");
        Composite child3 = new Composite("a");

        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);

        List<Component> filtered = parent.filterChildren(node -> node.getName().equals("a"));
        assertEquals(2, filtered.size());
    }

    @Test
    public void testSortChildren() {
        Composite parent = new Composite("parent");
        Composite child1 = new Composite("b");
        Composite child2 = new Composite("a");
        Composite child3 = new Composite("c");

        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);

        parent.sortChildren(Comparator.comparing(Component::getName));

        assertEquals("a", parent.getChildAt(0).getName());
        assertEquals("b", parent.getChildAt(1).getName());
        assertEquals("c", parent.getChildAt(2).getName());
    }

    @Test
    public void testMetadata() {
        Composite node = new Composite("node");
        node.setMetadata("key1", "value1");
        node.setMetadata("key2", "value2");

        assertEquals("value1", node.getMetadata("key1"));
        assertEquals("value2", node.getMetadata("key2"));
        assertEquals(2, node.getMetadata().size());

        node.removeMetadata("key1");
        assertNull(node.getMetadata("key1"));
    }

    @Test
    public void testGetAllLeaves() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite leaf1 = new Composite("leaf1");
        Composite leaf2 = new Composite("leaf2");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(leaf1);
        child2.addChild(leaf2);

        List<Component> leaves = root.getAllLeaves();
        assertEquals(2, leaves.size());
    }

    // ==================== Tree Tests ====================

    @Test
    public void testTreeCreation() {
        Composite root = new Composite("root");
        Tree tree = new Tree(root);
        assertEquals(root, tree.getRoot());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTreeCreationWithNullRoot() {
        new Tree(null);
    }

    @Test
    public void testTreeDfs() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        StringBuilder result = new StringBuilder();
        tree.dfs(node -> result.append(node.getName()).append("-"));

        assertEquals("root-child1-child2-", result.toString());
    }

    @Test
    public void testTreeBfs() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        StringBuilder result = new StringBuilder();
        tree.bfs(node -> result.append(node.getName()).append("-"));

        assertEquals("root-child1-child2-", result.toString());
    }

    @Test
    public void testTreeFind() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        assertEquals(child2, tree.find(node -> node.getName().equals("child2")));
        assertNull(tree.find(node -> node.getName().equals("nonexistent")));
    }

    @Test
    public void testTreeFindAll() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child");
        Composite child2 = new Composite("child");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        List<Component> found = tree.findAll(node -> node.getName().equals("child"));
        assertEquals(2, found.size());
    }

    @Test
    public void testTreeGetNodeCount() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        Tree tree = new Tree(root);
        assertEquals(4, tree.getNodeCount());
    }

    @Test
    public void testTreeGetDepth() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        child1.addChild(grandchild);

        Tree tree = new Tree(root);
        assertEquals(3, tree.getDepth());
    }

    @Test
    public void testTreeGetLeafCount() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        assertEquals(2, tree.getLeafCount());
    }

    @Test
    public void testTreeGetLCA() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        Tree tree = new Tree(root);
        // grandchild and child2's common ancestor is root
        assertEquals(root, tree.getLowestCommonAncestor(grandchild, child2));
        // grandchild and child1's common ancestor is child1 (child1 is ancestor of grandchild)
        assertEquals(child1, tree.getLowestCommonAncestor(grandchild, child1));
        // child1 and child2's common ancestor is root
        assertEquals(root, tree.getLowestCommonAncestor(child1, child2));
    }

    @Test
    public void testTreeGetDistance() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        Tree tree = new Tree(root);
        // grandchild to child2: grandchild -> child1 -> root -> child2 = 3
        assertEquals(3, tree.getDistance(grandchild, child2));
        assertEquals(0, tree.getDistance(child1, child1));
        assertEquals(1, tree.getDistance(root, child1));
    }

    @Test
    public void testTreeToList() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        List<Component> list = tree.toList();
        assertEquals(3, list.size());
    }

    @Test
    public void testTreeToMap() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");

        root.addChild(child1);

        Tree tree = new Tree(root);
        Map<String, Component> map = tree.toMap();
        assertEquals(2, map.size());
    }

    @Test
    public void testTreeGetAverageDepth() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        Tree tree = new Tree(root);
        double avgDepth = tree.getAverageDepth();
        assertTrue(avgDepth > 0 && avgDepth <= 2);
    }

    @Test
    public void testTreeFindAllPaths() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild1 = new Composite("leaf");
        Composite grandchild2 = new Composite("leaf");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild1);
        child2.addChild(grandchild2);

        Tree tree = new Tree(root);
        List<List<Component>> paths = tree.findAllPaths(node -> node.getName().equals("leaf"));
        assertEquals(2, paths.size());
        assertEquals(3, paths.get(0).size());
    }

    @Test
    public void testTreeLevelOrderWithDepth() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");

        root.addChild(child1);
        root.addChild(child2);

        Tree tree = new Tree(root);
        StringBuilder result = new StringBuilder();
        tree.levelOrder((depth, node) -> result.append(depth).append(":").append(node.getName()).append("-"));

        assertTrue(result.toString().contains("0:root"));
        assertTrue(result.toString().contains("1:child1"));
        assertTrue(result.toString().contains("1:child2"));
    }

    // ==================== Leaf Tests ====================

    @Test(expected = UnsupportedOperationException.class)
    public void testLeafCannotAddChild() {
        Leaf leaf = new Leaf("leaf");
        assertTrue(leaf.isLeaf());
        leaf.addChild(new Composite("child"));
    }

    @Test
    public void testLeafMetadata() {
        Leaf leaf = new Leaf("leaf");
        leaf.setMetadata("key", "value");
        assertEquals("value", leaf.getMetadata("key"));
    }

    @Test
    public void testLeafParent() {
        Composite parent = new Composite("parent");
        Leaf leaf = new Leaf("leaf");
        parent.addChild(leaf);

        assertEquals(parent, leaf.getParent());
        assertFalse(leaf.isRoot());
        assertTrue(leaf.isLeaf());
    }

    // ==================== Print Tests ====================

    @Test
    public void testToTreeString() {
        Composite root = new Composite("root");
        Composite child1 = new Composite("child1");
        Composite child2 = new Composite("child2");
        Composite grandchild = new Composite("grandchild");

        root.addChild(child1);
        root.addChild(child2);
        child1.addChild(grandchild);

        Tree tree = new Tree(root);
        String treeString = tree.toTreeString();

        assertTrue(treeString.contains("root"));
        assertTrue(treeString.contains("child1"));
        assertTrue(treeString.contains("child2"));
        assertTrue(treeString.contains("grandchild"));
    }

    @Test
    public void testIsBinaryTree() {
        Composite root = new Composite("root");
        Composite left = new Composite("left");
        Composite right = new Composite("right");

        root.addChild(left);
        root.addChild(right);

        Tree tree = new Tree(root);
        assertTrue(tree.isBinaryTree());
    }

    @Test
    public void testIsBalanced() {
        Composite root = new Composite("root");
        Composite left = new Composite("left");
        Composite right = new Composite("right");

        root.addChild(left);
        root.addChild(right);

        Tree tree = new Tree(root);
        assertTrue(tree.isBalanced());
    }

    // ==================== TreeBuilder Tests ====================

    @Test
    public void testTreeBuilderBasic() {
        TreeBuilder builder = TreeBuilder.create("root");
        builder.addChild("child1");
        builder.addChild("child2");
        builder.addChild("child3");

        Tree tree = builder.build();
        assertEquals("root", tree.getRoot().getName());
        assertEquals(3, tree.getRoot().getChildCount());
    }

    @Test
    public void testTreeBuilderWithDown() {
        TreeBuilder builder = TreeBuilder.create("root");
        builder.addChild("child1");
        builder.down("child1").addChild("grandchild");
        builder.addChild("child2");

        Tree tree = builder.build();
        assertEquals("root", tree.getRoot().getName());
        assertEquals(2, tree.getRoot().getChildCount());
        assertEquals(1, tree.getRoot().getChildren().get(0).getChildCount());
    }

    @Test
    public void testTreeBuilderMetadata() {
        TreeBuilder builder = TreeBuilder.create("root");
        builder.addChild("child1");
        builder.down("child1").meta("key", "value");

        Tree tree = builder.build();
        assertEquals("value", tree.getRoot().getChildren().get(0).getMetadata("key"));
    }

    @Test
    public void testTreeBuilderBuildFromList() {
        java.util.List<Composite> nodes = new java.util.ArrayList<>();
        Composite root = new Composite("1", "root");
        Composite child1 = new Composite("2", "child1");
        Composite child2 = new Composite("3", "child2");

        // Set up parent-child relationships BEFORE adding to list
        root.addChild(child1);
        root.addChild(child2);

        nodes.add(root);
        nodes.add(child1);
        nodes.add(child2);

        Tree tree = TreeBuilder.buildFromList(
                nodes,
                Component::getId,
                c -> c.getParent() == null ? null : c.getParent().getId()
        );

        assertEquals("root", tree.getRoot().getName());
        assertEquals(2, tree.getRoot().getChildCount());
    }
}
