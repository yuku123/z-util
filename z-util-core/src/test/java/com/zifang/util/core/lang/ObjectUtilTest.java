package com.zifang.util.core.lang;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class ObjectUtilTest {

    // --- deepCloneObject ---

    @Test
    public void testDeepCloneObject_WithSerializableObject() {
        TestSerializable original = new TestSerializable();
        original.setValue("test");
        original.setCount(42);

        TestSerializable cloned = ObjectUtil.deepCloneObject(original);

        assertNotNull(cloned);
        assertEquals(original.getValue(), cloned.getValue());
        assertEquals(original.getCount(), cloned.getCount());
        assertNotSame(original, cloned);
    }

    @Test
    public void testDeepCloneObject_ValuesAreIndependent() {
        TestSerializable original = new TestSerializable();
        original.setValue("original");

        TestSerializable cloned = ObjectUtil.deepCloneObject(original);
        cloned.setValue("modified");

        assertEquals("original", original.getValue());
        assertEquals("modified", cloned.getValue());
    }

    @Test
    public void testDeepCloneObject_WithNull() {
        String result = ObjectUtil.deepCloneObject(null);
        assertNull(result);
    }

    @Test
    public void testDeepCloneObject_WithString() {
        String original = "test";
        String cloned = ObjectUtil.deepCloneObject(original);
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    // --- deepCloneCollection ---

    @Test
    public void testDeepCloneCollection_WithList() throws Exception {
        List<String> original = new ArrayList<>();
        original.add("a");
        original.add("b");
        original.add("c");

        Collection<String> cloned = ObjectUtil.deepCloneCollection(original);

        assertNotNull(cloned);
        assertEquals(original.size(), cloned.size());
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    @Test
    public void testDeepCloneCollection_ValuesAreIndependent() throws Exception {
        List<String> original = new ArrayList<>();
        original.add("original");

        Collection<String> cloned = ObjectUtil.deepCloneCollection(original);
        List<String> clonedList = new ArrayList<>(cloned);
        clonedList.set(0, "modified");

        assertEquals("original", original.get(0));
        assertEquals("modified", clonedList.get(0));
    }

    @Test
    public void testDeepCloneCollection_WithEmptyList() throws Exception {
        List<String> original = new ArrayList<>();
        Collection<String> cloned = ObjectUtil.deepCloneCollection(original);

        assertNotNull(cloned);
        assertTrue(cloned.isEmpty());
        assertNotSame(original, cloned);
    }

    @Test
    public void testDeepCloneCollection_WithArraysAsList() throws Exception {
        List<Integer> original = Arrays.asList(1, 2, 3);
        Collection<Integer> cloned = ObjectUtil.deepCloneCollection(original);

        assertNotNull(cloned);
        assertEquals(original, cloned);
    }

    // Helper class for testing
    static class TestSerializable implements Serializable {
        private static final long serialVersionUID = 1L;
        private String value;
        private int count;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
