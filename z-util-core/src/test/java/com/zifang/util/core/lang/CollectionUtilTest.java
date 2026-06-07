package com.zifang.util.core.lang;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * CollectionUtilTest类。
 */
public class CollectionUtilTest {

    // --- findValueOfType ---

    @SuppressWarnings("unchecked")
    @Test
    /**
     * testFindValueOfType_WithSingleMatch方法。
     */
    public void testFindValueOfType_WithSingleMatch() {
        List<Number> list = Arrays.asList(1, 2L);
        Integer result = CollectionUtil.findValueOfType((Collection) list, Integer.class);
        assertEquals(Integer.valueOf(1), result);
    }

    @SuppressWarnings("unchecked")
    @Test
    /**
     * testFindValueOfType_WithNoMatch方法。
     */
    public void testFindValueOfType_WithNoMatch() {
        List<Number> list = Arrays.asList(1, 2L, 3);
        String result = CollectionUtil.findValueOfType((Collection) list, String.class);
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    /**
     * testFindValueOfType_WithMultipleMatches方法。
     */
    public void testFindValueOfType_WithMultipleMatches() {
        List<Number> list = Arrays.asList(1, 2, 3);
        Integer result = CollectionUtil.findValueOfType((Collection) list, Integer.class);
        assertNull(result); // More than one value found, returns null
    }

    @Test
    /**
     * testFindValueOfType_WithNullCollection方法。
     */
    public void testFindValueOfType_WithNullCollection() {
        Integer result = CollectionUtil.findValueOfType(null, Integer.class);
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    /**
     * testFindValueOfType_WithEmptyCollection方法。
     */
    public void testFindValueOfType_WithEmptyCollection() {
        List<Number> list = Collections.emptyList();
        Integer result = (Integer) CollectionUtil.findValueOfType((Collection) list, (Class) Integer.class);
        assertNull(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    /**
     * testFindValueOfType_WithNullType方法。
     */
    public void testFindValueOfType_WithNullType() {
        List<Number> list = Collections.singletonList(1);
        Object result = CollectionUtil.findValueOfType((Collection) list, null);
        assertEquals(1, result); // null type matches any element
    }

    // --- hasUniqueObject ---

    @Test
    /**
     * testHasUniqueObject_WithUniqueObjects方法。
     */
    public void testHasUniqueObject_WithUniqueObjects() {
        List<Object> list = Arrays.asList(new Object(), new Object());
        assertFalse(CollectionUtil.hasUniqueObject(list));
    }

    @Test
    /**
     * testHasUniqueObject_WithSameInstance方法。
     */
    public void testHasUniqueObject_WithSameInstance() {
        Object obj = new Object();
        List<Object> list = Arrays.asList(obj, obj);
        assertTrue(CollectionUtil.hasUniqueObject(list));
    }

    @Test
    /**
     * testHasUniqueObject_WithSingleElement方法。
     */
    public void testHasUniqueObject_WithSingleElement() {
        List<Object> list = Collections.singletonList(new Object());
        assertTrue(CollectionUtil.hasUniqueObject(list));
    }

    @Test
    /**
     * testHasUniqueObject_WithNullCollection方法。
     */
    public void testHasUniqueObject_WithNullCollection() {
        assertFalse(CollectionUtil.hasUniqueObject(null));
    }

    @Test
    /**
     * testHasUniqueObject_WithEmptyCollection方法。
     */
    public void testHasUniqueObject_WithEmptyCollection() {
        assertFalse(CollectionUtil.hasUniqueObject(Collections.emptyList()));
    }

    // --- findCommonElementType ---

    @Test
    /**
     * testFindCommonElementType_WithIntegers方法。
     */
    public void testFindCommonElementType_WithIntegers() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertEquals(Integer.class, CollectionUtil.findCommonElementType(list));
    }

    @Test
    /**
     * testFindCommonElementType_WithStrings方法。
     */
    public void testFindCommonElementType_WithStrings() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertEquals(String.class, CollectionUtil.findCommonElementType(list));
    }

    @Test
    /**
     * testFindCommonElementType_WithMixedTypes方法。
     */
    public void testFindCommonElementType_WithMixedTypes() {
        List<Object> list = Arrays.asList(1, "two", 3);
        assertNull(CollectionUtil.findCommonElementType(list));
    }

    @Test
    /**
     * testFindCommonElementType_WithNullsAndIntegers方法。
     */
    public void testFindCommonElementType_WithNullsAndIntegers() {
        List<Integer> list = Arrays.asList(null, 1, null);
        assertEquals(Integer.class, CollectionUtil.findCommonElementType(list));
    }

    @Test
    /**
     * testFindCommonElementType_WithAllNulls方法。
     */
    public void testFindCommonElementType_WithAllNulls() {
        List<Object> list = Arrays.asList(null, null);
        assertNull(CollectionUtil.findCommonElementType(list));
    }

    @Test
    /**
     * testFindCommonElementType_WithNullCollection方法。
     */
    public void testFindCommonElementType_WithNullCollection() {
        assertNull(CollectionUtil.findCommonElementType(null));
    }

    @Test
    /**
     * testFindCommonElementType_WithEmptyCollection方法。
     */
    public void testFindCommonElementType_WithEmptyCollection() {
        assertNull(CollectionUtil.findCommonElementType(Collections.emptyList()));
    }

    // --- firstElement (List) ---

    @Test
    /**
     * testFirstElement_List_WithNormalList方法。
     */
    public void testFirstElement_List_WithNormalList() {
        List<String> list = Arrays.asList("first", "second", "third");
        assertEquals("first", CollectionUtil.firstElement(list));
    }

    @Test
    /**
     * testFirstElement_List_WithSingleElement方法。
     */
    public void testFirstElement_List_WithSingleElement() {
        List<String> list = Collections.singletonList("only");
        assertEquals("only", CollectionUtil.firstElement(list));
    }

    @Test
    /**
     * testFirstElement_List_WithNullCollection方法。
     */
    public void testFirstElement_List_WithNullCollection() {
        assertNull(CollectionUtil.firstElement((List<String>) null));
    }

    @Test
    /**
     * testFirstElement_List_WithEmptyList方法。
     */
    public void testFirstElement_List_WithEmptyList() {
        assertNull(CollectionUtil.firstElement(Collections.emptyList()));
    }

    // --- firstElement (Set) ---

    @Test
    /**
     * testFirstElement_Set_WithHashSet方法。
     */
    public void testFirstElement_Set_WithHashSet() {
        Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c"));
        String result = CollectionUtil.firstElement(set);
        assertNotNull(result);
        assertTrue(set.contains(result));
    }

    @Test
    /**
     * testFirstElement_Set_WithSortedSet方法。
     */
    public void testFirstElement_Set_WithSortedSet() {
        SortedSet<String> set = new TreeSet<>(Arrays.asList("c", "b", "a"));
        assertEquals("a", CollectionUtil.firstElement(set));
    }

    @Test
    /**
     * testFirstElement_Set_WithSingleElement方法。
     */
    public void testFirstElement_Set_WithSingleElement() {
        Set<String> set = Collections.singleton("only");
        assertEquals("only", CollectionUtil.firstElement(set));
    }

    @Test
    /**
     * testFirstElement_Set_WithNullCollection方法。
     */
    public void testFirstElement_Set_WithNullCollection() {
        assertNull(CollectionUtil.firstElement((Set<String>) null));
    }

    @Test
    /**
     * testFirstElement_Set_WithEmptySet方法。
     */
    public void testFirstElement_Set_WithEmptySet() {
        assertNull(CollectionUtil.firstElement(Collections.emptySet()));
    }

    // --- lastElement (List) ---

    @Test
    /**
     * testLastElement_List_WithNormalList方法。
     */
    public void testLastElement_List_WithNormalList() {
        List<String> list = Arrays.asList("first", "second", "third");
        assertEquals("third", CollectionUtil.lastElement(list));
    }

    @Test
    /**
     * testLastElement_List_WithSingleElement方法。
     */
    public void testLastElement_List_WithSingleElement() {
        List<String> list = Collections.singletonList("only");
        assertEquals("only", CollectionUtil.lastElement(list));
    }

    @Test
    /**
     * testLastElement_List_WithNullCollection方法。
     */
    public void testLastElement_List_WithNullCollection() {
        assertNull(CollectionUtil.lastElement((List<String>) null));
    }

    @Test
    /**
     * testLastElement_List_WithEmptyList方法。
     */
    public void testLastElement_List_WithEmptyList() {
        assertNull(CollectionUtil.lastElement(Collections.emptyList()));
    }

    // --- lastElement (Set) ---

    @Test
    /**
     * testLastElement_Set_WithHashSet方法。
     */
    public void testLastElement_Set_WithHashSet() {
        Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c"));
        String result = CollectionUtil.lastElement(set);
        assertNotNull(result);
        assertTrue(set.contains(result));
    }

    @Test
    /**
     * testLastElement_Set_WithSortedSet方法。
     */
    public void testLastElement_Set_WithSortedSet() {
        SortedSet<String> set = new TreeSet<>(Arrays.asList("c", "b", "a"));
        assertEquals("c", CollectionUtil.lastElement(set));
    }

    @Test
    /**
     * testLastElement_Set_WithSingleElement方法。
     */
    public void testLastElement_Set_WithSingleElement() {
        Set<String> set = Collections.singleton("only");
        assertEquals("only", CollectionUtil.lastElement(set));
    }

    @Test
    /**
     * testLastElement_Set_WithNullCollection方法。
     */
    public void testLastElement_Set_WithNullCollection() {
        assertNull(CollectionUtil.lastElement((Set<String>) null));
    }

    @Test
    /**
     * testLastElement_Set_WithEmptySet方法。
     */
    public void testLastElement_Set_WithEmptySet() {
        assertNull(CollectionUtil.lastElement(Collections.emptySet()));
    }

    // --- toArray ---

    @Test
    /**
     * testToArray_WithEnumeration方法。
     */
    public void testToArray_WithEnumeration() {
        Vector<String> vector = new Vector<>(Arrays.asList("a", "b", "c"));
        Enumeration<String> enumeration = vector.elements();
        String[] result = CollectionUtil.toArray(enumeration, new String[0]);
        assertArrayEquals(new String[]{"a", "b", "c"}, result);
    }

    @Test
    /**
     * testToArray_WithEmptyEnumeration方法。
     */
    public void testToArray_WithEmptyEnumeration() {
        Vector<String> vector = new Vector<>();
        Enumeration<String> enumeration = vector.elements();
        String[] result = CollectionUtil.toArray(enumeration, new String[0]);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    /**
     * testToArray_WithIntegerEnumeration方法。
     */
    public void testToArray_WithIntegerEnumeration() {
        Vector<Integer> vector = new Vector<>(Arrays.asList(1, 2, 3));
        Enumeration<Integer> enumeration = vector.elements();
        Number[] result = CollectionUtil.toArray(enumeration, new Number[0]);
        assertArrayEquals(new Number[]{1, 2, 3}, result);
    }

    // --- toIterator ---

    @Test
    /**
     * testToIterator_WithEnumeration方法。
     */
    public void testToIterator_WithEnumeration() {
        Vector<String> vector = new Vector<>(Arrays.asList("a", "b", "c"));
        Enumeration<String> enumeration = vector.elements();
        Iterator<String> iterator = CollectionUtil.toIterator(enumeration);
        assertTrue(iterator.hasNext());
        assertEquals("a", iterator.next());
        assertEquals("b", iterator.next());
        assertEquals("c", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    /**
     * testToIterator_WithNullEnumeration方法。
     */
    public void testToIterator_WithNullEnumeration() {
        Iterator<?> iterator = CollectionUtil.toIterator(null);
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    /**
     * testToIterator_RemoveThrowsUnsupportedOperation方法。
     */
    public void testToIterator_RemoveThrowsUnsupportedOperation() {
        Vector<String> vector = new Vector<>(Collections.singletonList("a"));
        Iterator<String> iterator = CollectionUtil.toIterator(vector.elements());
        assertTrue(iterator.hasNext());
        iterator.next();
        try {
            iterator.remove();
            fail("Should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    // --- mergePropertiesIntoMap ---

    @Test
    /**
     * testMergePropertiesIntoMap_WithNormalProperties方法。
     */
    public void testMergePropertiesIntoMap_WithNormalProperties() {
        Properties props = new Properties();
        props.setProperty("key1", "value1");
        props.setProperty("key2", "value2");
        Map<String, Object> map = new HashMap<>();
        CollectionUtil.mergePropertiesIntoMap(props, map);
        assertEquals("value1", map.get("key1"));
        assertEquals("value2", map.get("key2"));
    }

    @Test
    /**
     * testMergePropertiesIntoMap_WithNullProperties方法。
     */
    public void testMergePropertiesIntoMap_WithNullProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("existing", "value");
        CollectionUtil.mergePropertiesIntoMap(null, map);
        assertEquals("value", map.get("existing"));
        assertTrue(map.size() == 1);
    }

    @Test
    /**
     * testMergePropertiesIntoMap_WithEmptyProperties方法。
     */
    public void testMergePropertiesIntoMap_WithEmptyProperties() {
        Properties props = new Properties();
        Map<String, Object> map = new HashMap<>();
        CollectionUtil.mergePropertiesIntoMap(props, map);
        assertTrue(map.isEmpty());
    }

    @Test
    /**
     * testMergePropertiesIntoMap_PreservesExistingEntries方法。
     */
    public void testMergePropertiesIntoMap_PreservesExistingEntries() {
        Properties props = new Properties();
        props.setProperty("newKey", "newValue");
        Map<String, Object> map = new HashMap<>();
        map.put("existingKey", "existingValue");
        CollectionUtil.mergePropertiesIntoMap(props, map);
        assertEquals("existingValue", map.get("existingKey"));
        assertEquals("newValue", map.get("newKey"));
    }

    // --- capacity ---

    @Test
    /**
     * testCapacity_WithZero方法。
     */
    public void testCapacity_WithZero() {
        assertEquals(1, CollectionUtil.capacity(0));
    }

    @Test
    /**
     * testCapacity_WithOne方法。
     */
    public void testCapacity_WithOne() {
        assertEquals(2, CollectionUtil.capacity(1));
    }

    @Test
    /**
     * testCapacity_WithTwo方法。
     */
    public void testCapacity_WithTwo() {
        assertEquals(3, CollectionUtil.capacity(2));
    }

    @Test
    /**
     * testCapacity_WithSmallValues方法。
     */
    public void testCapacity_WithSmallValues() {
        assertEquals(3, CollectionUtil.capacity(2));
        assertEquals(5, CollectionUtil.capacity(3));
        assertEquals(6, CollectionUtil.capacity(4));
    }

    @Test(expected = IllegalArgumentException.class)
    /**
     * testCapacity_WithNegativeValue方法。
     */
    public void testCapacity_WithNegativeValue() {
        CollectionUtil.capacity(-1);
    }

    // --- parseByteValue ---

    @Test
    /**
     * testParseByteValue_WithValidValue方法。
     */
    public void testParseByteValue_WithValidValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("byteKey", "10");
        assertEquals(Byte.valueOf("10"), CollectionUtil.parseByteValue(map, "byteKey"));
    }

    @Test
    /**
     * testParseByteValue_WithNullMap方法。
     */
    public void testParseByteValue_WithNullMap() {
        assertNull(CollectionUtil.parseByteValue(null, "key"));
    }

    @Test
    /**
     * testParseByteValue_WithNullKey方法。
     */
    public void testParseByteValue_WithNullKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "5");
        assertNull(CollectionUtil.parseByteValue(map, "nonExistentKey"));
    }

    @Test
    /**
     * testParseByteValue_WithIntegerValue方法。
     */
    public void testParseByteValue_WithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("byteKey", 10);
        assertEquals(Byte.valueOf("10"), CollectionUtil.parseByteValue(map, "byteKey"));
    }

    // --- parseShortValue ---

    @Test
    /**
     * testParseShortValue_WithValidValue方法。
     */
    public void testParseShortValue_WithValidValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("shortKey", "100");
        assertEquals(Short.valueOf("100"), CollectionUtil.parseShortValue(map, "shortKey"));
    }

    @Test
    /**
     * testParseShortValue_WithNullMap方法。
     */
    public void testParseShortValue_WithNullMap() {
        assertNull(CollectionUtil.parseShortValue(null, "key"));
    }

    @Test
    /**
     * testParseShortValue_WithNullKey方法。
     */
    public void testParseShortValue_WithNullKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "5");
        assertNull(CollectionUtil.parseShortValue(map, "nonExistentKey"));
    }

    @Test
    /**
     * testParseShortValue_WithIntegerValue方法。
     */
    public void testParseShortValue_WithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("shortKey", 100);
        assertEquals(Short.valueOf("100"), CollectionUtil.parseShortValue(map, "shortKey"));
    }

    // --- parseIntegerValue ---

    @Test
    /**
     * testParseIntegerValue_WithValidValue方法。
     */
    public void testParseIntegerValue_WithValidValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("intKey", "123");
        assertEquals(Integer.valueOf(123), CollectionUtil.parseIntegerValue(map, "intKey"));
    }

    @Test
    /**
     * testParseIntegerValue_WithNullMap方法。
     */
    public void testParseIntegerValue_WithNullMap() {
        assertNull(CollectionUtil.parseIntegerValue(null, "key"));
    }

    @Test
    /**
     * testParseIntegerValue_WithNullKey方法。
     */
    public void testParseIntegerValue_WithNullKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "5");
        assertNull(CollectionUtil.parseIntegerValue(map, "nonExistentKey"));
    }

    @Test
    /**
     * testParseIntegerValue_WithIntegerValue方法。
     */
    public void testParseIntegerValue_WithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("intKey", 123);
        assertEquals(Integer.valueOf(123), CollectionUtil.parseIntegerValue(map, "intKey"));
    }

    @Test
    /**
     * testParseIntegerValue_WithLongValue方法。
     */
    public void testParseIntegerValue_WithLongValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("intKey", 123L);
        assertEquals(Integer.valueOf(123), CollectionUtil.parseIntegerValue(map, "intKey"));
    }

    // --- parseLongValue ---

    @Test
    /**
     * testParseLongValue_WithValidValue方法。
     */
    public void testParseLongValue_WithValidValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("longKey", "9223372036854775807");
        assertEquals(Long.valueOf(9223372036854775807L), CollectionUtil.parseLongValue(map, "longKey"));
    }

    @Test
    /**
     * testParseLongValue_WithNullMap方法。
     */
    public void testParseLongValue_WithNullMap() {
        assertNull(CollectionUtil.parseLongValue(null, "key"));
    }

    @Test
    /**
     * testParseLongValue_WithNullKey方法。
     */
    public void testParseLongValue_WithNullKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "5");
        assertNull(CollectionUtil.parseLongValue(map, "nonExistentKey"));
    }

    @Test
    /**
     * testParseLongValue_WithIntegerValue方法。
     */
    public void testParseLongValue_WithIntegerValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("longKey", 123);
        assertEquals(Long.valueOf(123), CollectionUtil.parseLongValue(map, "longKey"));
    }

    @Test
    /**
     * testParseLongValue_WithLongValue方法。
     */
    public void testParseLongValue_WithLongValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("longKey", 9223372036854775807L);
        assertEquals(Long.valueOf(9223372036854775807L), CollectionUtil.parseLongValue(map, "longKey"));
    }
}
