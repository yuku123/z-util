package com.zifang.util.core.pattern.composite;

import com.zifang.util.core.pattern.composite.tree.ILeaf;
import com.zifang.util.core.pattern.composite.tree.LeafHelper;
import com.zifang.util.core.pattern.composite.tree.LeafWrapper;
import com.zifang.util.core.pattern.composite.tree.Tree;
import com.zifang.util.core.pattern.composite.tree.TreeBuilder;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CompositeTest {

    // ==================== ILeaf Tests ====================

    @Test
    public void testILeafBasicOperations() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> leaf = new LeafWrapper<>("leaf", "child1", "leaf");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(leaf);

        assertEquals("root", root.getName());
        assertEquals(2, root.getChildCount());
        assertEquals(2, root.getSubLeaves().size());
        assertTrue(root.isRoot());
        assertFalse(root.isLeaf());

        assertEquals(root, child1.getParentLeaf());
        assertTrue(leaf.isLeaf());
    }

    @Test
    public void testGetDepth() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild);

        // root depth = 1, children depth = 2, grandchild depth = 3
        assertEquals(1, root.getDepth());
        assertEquals(2, child1.getDepth());
        assertEquals(2, child2.getDepth());
        assertEquals(3, grandchild.getDepth());
    }

    @Test
    public void testGetSubtreeSize() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild);

        assertEquals(4, root.getSubtreeSize());
        assertEquals(2, child1.getSubtreeSize());
        assertEquals(1, child2.getSubtreeSize());
        assertEquals(1, grandchild.getSubtreeSize());
    }

    @Test
    public void testGetPathFromRoot() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        child1.appendSubLeaf(grandchild);

        List<ILeaf> path = grandchild.getPathFromRoot();
        assertEquals(3, path.size());
        assertEquals("root", path.get(0).getName());
        assertEquals("child1", path.get(1).getName());
        assertEquals("grandchild", path.get(2).getName());
    }

    @Test
    public void testTraversePreOrder() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        StringBuilder result = new StringBuilder();
        root.traversePreOrder(node -> result.append(node.getName()).append("-"));

        assertEquals("root-child1-child2-", result.toString());
    }

    @Test
    public void testTraversePostOrder() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        StringBuilder result = new StringBuilder();
        root.traversePostOrder(node -> result.append(node.getName()).append("-"));

        assertEquals("child1-child2-root-", result.toString());
    }

    @Test
    public void testFind() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        assertEquals(child2, root.find(node -> node.getName().equals("child2")));
        assertNull(root.find(node -> node.getName().equals("nonexistent")));
    }

    @Test
    public void testFindAll() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child", "root", "child");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child", "root", "child");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        List<ILeaf> found = root.findAll(node -> node.getName().equals("child"));
        assertEquals(2, found.size());
    }

    @Test
    public void testMap() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        List<String> names = root.map(ILeaf::getName);
        assertEquals(3, names.size());
        assertTrue(names.contains("root"));
        assertTrue(names.contains("child1"));
        assertTrue(names.contains("child2"));
    }

    @Test
    public void testGetRoot() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        child1.appendSubLeaf(grandchild);

        assertEquals(root, grandchild.getRoot());
        assertEquals(root, child1.getRoot());
        assertEquals(root, root.getRoot());
    }

    // ==================== LeafWrapper Tests ====================

    @Test
    public void testLeafWrapperMetadata() {
        LeafWrapper<String, String, String> node = new LeafWrapper<>("node", null, "node");
        node.setMeta("key1", "value1");
        node.setMeta("key2", "value2");

        assertEquals("value1", node.getMeta("key1"));
        assertEquals("value2", node.getMeta("key2"));
        assertEquals(2, node.getMetadata().size());
    }

    @Test
    public void testLeafWrapperRemoveSubLeaf() {
        LeafWrapper<String, String, String> parent = new LeafWrapper<>("parent", null, "parent");
        LeafWrapper<String, String, String> child = new LeafWrapper<>("child", "parent", "child");

        parent.appendSubLeaf(child);
        assertEquals(1, parent.getChildCount());

        parent.removeSubLeaf(child);
        assertEquals(0, parent.getChildCount());
        assertTrue(child.isRoot());
    }

    @Test
    public void testLeafWrapperSortChildren() {
        LeafWrapper<String, String, String> parent = new LeafWrapper<>("parent", null, "parent");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("b", "parent", "b");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("a", "parent", "a");
        LeafWrapper<String, String, String> child3 = new LeafWrapper<>("c", "parent", "c");

        parent.appendSubLeaf(child1);
        parent.appendSubLeaf(child2);
        parent.appendSubLeaf(child3);

        parent.getSubLeaves().sort(Comparator.comparing(ILeaf::getName));

        assertEquals("a", parent.getSubLeaves().get(0).getName());
        assertEquals("b", parent.getSubLeaves().get(1).getName());
        assertEquals("c", parent.getSubLeaves().get(2).getName());
    }

    @Test
    public void testGetAllLeaves() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> leaf1 = new LeafWrapper<>("leaf1", "child1", "leaf1");
        LeafWrapper<String, String, String> leaf2 = new LeafWrapper<>("leaf2", "child2", "leaf2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(leaf1);
        child2.appendSubLeaf(leaf2);

        List<ILeaf> leaves = root.findAll(ILeaf::isLeaf);
        assertEquals(2, leaves.size());
    }

    // ==================== Tree Tests ====================

    @Test
    public void testTreeCreation() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        Tree tree = new Tree(root);
        assertEquals(root, tree.getRoot());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTreeCreationWithNullRoot() {
        new Tree(null);
    }

    @Test
    public void testTreeDfs() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        StringBuilder result = new StringBuilder();
        tree.dfs(node -> result.append(node.getName()).append("-"));

        assertEquals("root-child1-child2-", result.toString());
    }

    @Test
    public void testTreeBfs() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        StringBuilder result = new StringBuilder();
        tree.bfs(node -> result.append(node.getName()).append("-"));

        assertEquals("root-child1-child2-", result.toString());
    }

    @Test
    public void testTreeFind() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        assertEquals(child2, tree.find(node -> node.getName().equals("child2")));
        assertNull(tree.find(node -> node.getName().equals("nonexistent")));
    }

    @Test
    public void testTreeFindAll() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child", "root", "child");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child", "root", "child");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        List<ILeaf> found = tree.findAll(node -> node.getName().equals("child"));
        assertEquals(2, found.size());
    }

    @Test
    public void testTreeGetNodeCount() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild);

        Tree tree = new Tree(root);
        assertEquals(4, tree.getNodeCount());
    }

    @Test
    public void testTreeGetHeight() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        child1.appendSubLeaf(grandchild);

        Tree tree = new Tree(root);
        assertEquals(3, tree.getHeight());
    }

    @Test
    public void testTreeGetLeafCount() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        assertEquals(2, tree.getLeafCount());
    }

    @Test
    public void testTreeGetLCA() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild);

        Tree tree = new Tree(root);
        assertEquals(root, tree.getLowestCommonAncestor(grandchild, child2));
        assertEquals(child1, tree.getLowestCommonAncestor(grandchild, child1));
        assertEquals(root, tree.getLowestCommonAncestor(child1, child2));
    }

    @Test
    public void testTreeGetDistance() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild);

        Tree tree = new Tree(root);
        assertEquals(3, tree.getDistance(grandchild, child2));
        assertEquals(0, tree.getDistance(child1, child1));
        assertEquals(1, tree.getDistance(root, child1));
    }

    @Test
    public void testTreeToList() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        List<ILeaf> list = tree.toList();
        assertEquals(3, list.size());
    }

    @Test
    public void testTreeToMap() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");

        root.appendSubLeaf(child1);

        Tree tree = new Tree(root);
        Map<String, ILeaf> map = tree.toMap();
        assertEquals(2, map.size());
    }

    @Test
    public void testTreeGetAverageDepth() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild = new LeafWrapper<>("grandchild", "child1", "grandchild");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild);

        Tree tree = new Tree(root);
        double avgDepth = tree.getAverageDepth();
        assertTrue(avgDepth > 0 && avgDepth <= 3);
    }

    @Test
    public void testTreeFindAllPaths() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");
        LeafWrapper<String, String, String> grandchild1 = new LeafWrapper<>("leaf", "child1", "leaf");
        LeafWrapper<String, String, String> grandchild2 = new LeafWrapper<>("leaf", "child2", "leaf");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);
        child1.appendSubLeaf(grandchild1);
        child2.appendSubLeaf(grandchild2);

        Tree tree = new Tree(root);
        List<List<ILeaf>> paths = tree.findAllPaths(node -> node.getName().equals("leaf"));
        assertEquals(2, paths.size());
        assertEquals(3, paths.get(0).size());
    }

    @Test
    public void testTreeLevelOrderWithDepth() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> child1 = new LeafWrapper<>("child1", "root", "child1");
        LeafWrapper<String, String, String> child2 = new LeafWrapper<>("child2", "root", "child2");

        root.appendSubLeaf(child1);
        root.appendSubLeaf(child2);

        Tree tree = new Tree(root);
        StringBuilder result = new StringBuilder();
        tree.levelOrder((depth, node) -> result.append(depth).append(":").append(node.getName()).append("-"));

        assertTrue(result.toString().contains("0:root"));
        assertTrue(result.toString().contains("1:child1"));
        assertTrue(result.toString().contains("1:child2"));
    }

    @Test
    public void testIsBinaryTree() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> left = new LeafWrapper<>("left", "root", "left");
        LeafWrapper<String, String, String> right = new LeafWrapper<>("right", "root", "right");

        root.appendSubLeaf(left);
        root.appendSubLeaf(right);

        Tree tree = new Tree(root);
        assertTrue(tree.isBinaryTree());
    }

    @Test
    public void testIsBalanced() {
        LeafWrapper<String, String, String> root = new LeafWrapper<>("root", null, "root");
        LeafWrapper<String, String, String> left = new LeafWrapper<>("left", "root", "left");
        LeafWrapper<String, String, String> right = new LeafWrapper<>("right", "root", "right");

        root.appendSubLeaf(left);
        root.appendSubLeaf(right);

        Tree tree = new Tree(root);
        assertTrue(tree.isBalanced());
    }

    // ==================== TreeBuilder Tests ====================

    @Test
    public void testTreeBuilderBasic() {
        TreeBuilder builder = TreeBuilder.create("root");
        builder.add("child1");
        builder.add("child2");
        builder.add("child3");

        Tree tree = builder.build();
        assertEquals("root", tree.getRoot().getName());
        assertEquals(3, tree.getRoot().getChildCount());
    }

    @Test
    public void testTreeBuilderWithDown() {
        TreeBuilder builder = TreeBuilder.create("root");
        builder.add("child1");
        builder.down("child1").add("grandchild");
        builder.up().add("child2");

        Tree tree = builder.build();
        assertEquals("root", tree.getRoot().getName());
        assertEquals(2, tree.getRoot().getChildCount());
        assertEquals(1, tree.getRoot().getSubLeaves().get(0).getChildCount());
    }

    @Test
    public void testTreeBuilderMetadata() {
        TreeBuilder builder = TreeBuilder.create("root");
        builder.add("child1");
        builder.down("child1").meta("key", "value");

        Tree tree = builder.build();
        ILeaf child = tree.getRoot().getSubLeaves().get(0);
        assertEquals("value", ((LeafWrapper<?, ?, ?>) child).getMeta("key"));
    }

    @Test
    public void testTreeBuilderBuildFromHierarchy() {
        List<TreeBuilder.HierarchyItem> items = new java.util.ArrayList<>();
        items.add(new TreeBuilder.HierarchyItem() {
            @Override
            public String getId() { return "1"; }
            @Override
            public String getParentId() { return null; }
        });
        items.add(new TreeBuilder.HierarchyItem() {
            @Override
            public String getId() { return "2"; }
            @Override
            public String getParentId() { return "1"; }
        });
        items.add(new TreeBuilder.HierarchyItem() {
            @Override
            public String getId() { return "3"; }
            @Override
            public String getParentId() { return "1"; }
        });

        Tree tree = TreeBuilder.buildFromHierarchy(items);

        assertEquals("1", tree.getRoot().getId());
        assertEquals(2, tree.getRoot().getChildCount());
    }

    // ==================== LeafHelper Tests ====================

    @Test
    public void testLeafHelperPickRoot() {
        LeafWrapper<String, String, String> root1 = new LeafWrapper<>("root1", null, "root1");
        LeafWrapper<String, String, String> root2 = new LeafWrapper<>("root2", null, "root2");

        assertEquals(root1, LeafHelper.pickRootLeaf(java.util.Arrays.asList(root1, root2)));
    }

    @Test
    public void testLeafHelperHasDissociate() {
        LeafWrapper<String, String, String> root1 = new LeafWrapper<>("root1", null, "root1");
        LeafWrapper<String, String, String> root2 = new LeafWrapper<>("root2", null, "root2");

        assertTrue(LeafHelper.hasDissociateLeaf(java.util.Arrays.asList(root1, root2)));
        assertFalse(LeafHelper.hasDissociateLeaf(java.util.Arrays.asList(root1)));
    }

    @Test
    public void testLeafHelperWrapper() {
        LeafWrapper<String, String, String> wrapper = LeafHelper.wrapper("1", null, "data");
        assertEquals("1", wrapper.getId());
        assertEquals("data", wrapper.getBean());
    }

    @Test
    public void testLeafHelperTreeify() {
        List<LeafWrapper<String, String, String>> wrappers = new java.util.ArrayList<>();
        wrappers.add(new LeafWrapper<>("root", null, "root"));
        wrappers.add(new LeafWrapper<>("child1", "root", "child1"));
        wrappers.add(new LeafWrapper<>("child2", "root", "child2"));

        LeafWrapper<String, String, String> root = LeafHelper.treeify(wrappers);

        assertEquals("root", root.getName());
        assertEquals(2, root.getChildCount());
    }
}
