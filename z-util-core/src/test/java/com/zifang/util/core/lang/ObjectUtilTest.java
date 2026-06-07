package com.zifang.util.core.lang;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ObjectUtilTest类。
 */
public class ObjectUtilTest {

    // --- deepCloneObject ---

    @Test
    /**
     * testDeepCloneObject_WithSerializableObject方法。
     */
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
    /**
     * testDeepCloneObject_ValuesAreIndependent方法。
     */
    public void testDeepCloneObject_ValuesAreIndependent() {
        TestSerializable original = new TestSerializable();
        original.setValue("original");

        TestSerializable cloned = ObjectUtil.deepCloneObject(original);
        cloned.setValue("modified");

        assertEquals("original", original.getValue());
        assertEquals("modified", cloned.getValue());
    }

    @Test
    /**
     * testDeepCloneObject_WithNull方法。
     */
    public void testDeepCloneObject_WithNull() {
        String result = ObjectUtil.deepCloneObject(null);
        assertNull(result);
    }

    @Test
    /**
     * testDeepCloneObject_WithString方法。
     */
    public void testDeepCloneObject_WithString() {
        String original = "test";
        String cloned = ObjectUtil.deepCloneObject(original);
        assertEquals(original, cloned);
        assertNotSame(original, cloned);
    }

    // --- deepCloneCollection ---

    @Test
    /**
     * testDeepCloneCollection_WithList方法。
     */
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
    /**
     * testDeepCloneCollection_ValuesAreIndependent方法。
     */
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
    /**
     * testDeepCloneCollection_WithEmptyList方法。
     */
    public void testDeepCloneCollection_WithEmptyList() throws Exception {
        List<String> original = new ArrayList<>();
        Collection<String> cloned = ObjectUtil.deepCloneCollection(original);

        assertNotNull(cloned);
        assertTrue(cloned.isEmpty());
        assertNotSame(original, cloned);
    }

    @Test
    /**
     * testDeepCloneCollection_WithArraysAsList方法。
     */
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

    /**
     * getValue方法。
     * @return String类型返回值
     */
        public String getValue() {
            return value;
        }

    /**
     * setValue方法。
     *      * @param value String类型参数
     */
        public void setValue(String value) {
            this.value = value;
        }

    /**
     * getCount方法。
     * @return int类型返回值
     */
        public int getCount() {
            return count;
        }

    /**
     * setCount方法。
     *      * @param count int类型参数
     */
        public void setCount(int count) {
            this.count = count;
        }
    }
}
