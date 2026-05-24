package com.zifang.util.core.lang.collection;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SetsTest {

    // --- newHashSet (varargs) ---

    @Test
    public void testNewHashSet_Normal() {
        HashSet<String> result = Sets.newHashSet("a", "b", "c");
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testNewHashSet_SingleElement() {
        HashSet<String> result = Sets.newHashSet("a");
        assertEquals(1, result.size());
        assertTrue(result.contains("a"));
    }

    @Test
    public void testNewHashSet_Empty() {
        HashSet<String> result = Sets.newHashSet();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewHashSet_WithNull() {
        HashSet<String> result = Sets.newHashSet("a", null, "c");
        assertEquals(3, result.size());
        assertTrue(result.contains(null));
    }

    @Test
    public void testNewHashSet_WithDuplicates() {
        HashSet<String> result = Sets.newHashSet("a", "a", "b");
        assertEquals(2, result.size());
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
    }

    // --- newHashSet (boolean isSorted, varargs) ---

    @Test
    public void testNewHashSetSorted_False() {
        // Cast false to avoid ambiguity between newHashSet(T...) and newHashSet(boolean, T...)
        HashSet<String> result = Sets.<String>newHashSet(false, "c", "a", "b");
        assertEquals(3, result.size());
        assertFalse(result instanceof LinkedHashSet);
    }

    @Test
    public void testNewHashSetSorted_True() {
        HashSet<String> result = Sets.<String>newHashSet(true, "c", "a", "b");
        assertEquals(3, result.size());
        assertTrue("Should be LinkedHashSet when sorted", result instanceof LinkedHashSet);
    }

    @Test
    public void testNewHashSetSorted_NullArray() {
        HashSet<String> result = Sets.<String>newHashSet(false, (String[]) null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewHashSetSorted_TrueNullArray() {
        HashSet<String> result = Sets.<String>newHashSet(true, (String[]) null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewHashSetSorted_EmptyArray() {
        HashSet<String> result = Sets.<String>newHashSet(false);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewHashSetSorted_IntegerElements() {
        HashSet<Integer> result = Sets.<Integer>newHashSet(false, 1, 2, 3);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(1, 2, 3)));
    }

    // --- empty stubs: intersection, difference, union ---

    @Test
    public void testIntersection_Basic() {
        Set<String> s1 = Sets.newHashSet("a", "b", "c");
        Set<String> s2 = Sets.newHashSet("b", "c", "d");
        Set<String> result = Sets.intersection(s1, s2);
        assertEquals(2, result.size());
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
    }

    @Test
    public void testDifference_Basic() {
        Set<String> s1 = Sets.newHashSet("a", "b", "c");
        Set<String> s2 = Sets.newHashSet("b", "c", "d");
        Set<String> result = Sets.difference(s1, s2);
        assertEquals(1, result.size());
        assertTrue(result.contains("a"));
    }

    @Test
    public void testUnion_Basic() {
        Set<String> s1 = Sets.newHashSet("a", "b");
        Set<String> s2 = Sets.newHashSet("b", "c");
        Set<String> result = Sets.union(s1, s2);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("a", "b", "c")));
    }
}
