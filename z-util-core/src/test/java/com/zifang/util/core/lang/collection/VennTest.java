package com.zifang.util.core.lang.collection;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class VennTest {

    // --- union ---

    @Test
    public void testUnion_Normal() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("b", "c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.union();
        assertEquals(4, result.size());
        assertTrue(result.containsAll(Arrays.asList("a", "b", "c", "d")));
    }

    @Test
    public void testUnion_NoOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.union();
        assertEquals(4, result.size());
    }

    @Test
    public void testUnion_Identical() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("a", "b");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.union();
        assertEquals(2, result.size());
    }

    @Test
    public void testUnion_OneEmpty() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = new ArrayList<>();
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.union();
        assertEquals(2, result.size());
    }

    @Test
    public void testUnion_BothEmpty() {
        Collection<String> c1 = new ArrayList<>();
        Collection<String> c2 = new ArrayList<>();
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.union();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- unionCount ---

    @Test
    public void testUnionCount_Normal() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("b", "c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        assertEquals(Integer.valueOf(4), venn.unionCount());
    }

    @Test
    public void testUnionCount_NoOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        assertEquals(Integer.valueOf(4), venn.unionCount());
    }

    @Test
    public void testUnionCount_Empty() {
        Collection<String> c1 = new ArrayList<>();
        Collection<String> c2 = new ArrayList<>();
        Venn<String> venn = new Venn<>(c1, c2);
        assertEquals(Integer.valueOf(0), venn.unionCount());
    }

    // --- intersection ---

    @Test
    public void testIntersection_Normal() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("b", "c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersection();
        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("b", "c")));
    }

    @Test
    public void testIntersection_NoOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersection();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIntersection_Identical() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("a", "b");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersection();
        assertEquals(2, result.size());
    }

    @Test
    public void testIntersection_OneEmpty() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = new ArrayList<>();
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersection();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIntersection_SingleOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("c", "d", "e");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersection();
        assertEquals(1, result.size());
        assertTrue(result.contains("c"));
    }

    // --- intersectionLeft ---

    @Test
    public void testIntersectionLeft_Normal() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("b", "c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionLeft();
        assertEquals(1, result.size());
        assertTrue(result.contains("a"));
    }

    @Test
    public void testIntersectionLeft_NoOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionLeft();
        assertEquals(2, result.size());
    }

    @Test
    public void testIntersectionLeft_CompleteOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("a", "b");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionLeft();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIntersectionLeft_OneEmpty() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = new ArrayList<>();
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionLeft();
        assertEquals(2, result.size());
    }

    // --- intersectionRight ---

    @Test
    public void testIntersectionRight_Normal() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("b", "c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionRight();
        assertEquals(1, result.size());
        assertTrue(result.contains("d"));
    }

    @Test
    public void testIntersectionRight_NoOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionRight();
        assertEquals(2, result.size());
    }

    @Test
    public void testIntersectionRight_CompleteOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("a", "b");
        Venn<String> venn = new Venn<>(c1, c2);
        Collection<String> result = venn.intersectionRight();
        assertTrue(result.isEmpty());
    }

    // --- intersectionCount ---

    @Test
    public void testIntersectionCount_Normal() {
        Collection<String> c1 = Arrays.asList("a", "b", "c");
        Collection<String> c2 = Arrays.asList("b", "c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        assertEquals(Integer.valueOf(2), venn.intersectionCount());
    }

    @Test
    public void testIntersectionCount_NoOverlap() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        assertEquals(Integer.valueOf(0), venn.intersectionCount());
    }

    @Test
    public void testIntersectionCount_Empty() {
        Collection<String> c1 = new ArrayList<>();
        Collection<String> c2 = new ArrayList<>();
        Venn<String> venn = new Venn<>(c1, c2);
        assertEquals(Integer.valueOf(0), venn.intersectionCount());
    }

    // --- dicript (empty stub) ---

    @Test
    public void testDicipt_Stub() {
        Collection<String> c1 = Arrays.asList("a", "b");
        Collection<String> c2 = Arrays.asList("c", "d");
        Venn<String> venn = new Venn<>(c1, c2);
        // method is empty stub, just verify it doesn't throw
        venn.dicript();
    }
}
