package com.zifang.util.sql.model;

import org.junit.Test;
import java.util.Iterator;
import static org.junit.Assert.*;

/**
 * Row模型测试
 */
public class RowTest {

    @Test
    public void testCreateRowWithSize() {
        Row row = new Row(3);
        assertEquals(3, row.size());
    }

    @Test
    public void testCreateRowWithValues() {
        Row row = new Row(1, "Alice", true);
        assertEquals(3, row.size());
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));
        assertEquals(true, row.get(2));
    }

    @Test
    public void testEmptyRow() {
        Row row = new Row();
        assertTrue(row.isEmpty());
        assertEquals(0, row.size());
    }

    @Test
    public void testAddValues() {
        Row row = new Row();
        row.add(1);
        row.add("Bob");
        row.add(3.14);

        assertEquals(3, row.size());
        assertEquals(1, row.get(0));
        assertEquals("Bob", row.get(1));
        assertEquals(3.14, row.get(2));
    }

    @Test
    public void testSetValue() {
        Row row = new Row(3);
        row.set(0, 100);
        row.set(1, "Test");
        row.set(2, false);

        assertEquals(100, row.get(0));
        assertEquals("Test", row.get(1));
        assertEquals(false, row.get(2));
    }

    @Test
    public void testGetInvalidIndex() {
        Row row = new Row(1);
        row.set(0, "value");

        assertNull(row.get(-1));
        assertNull(row.get(1));
    }

    @Test
    public void testClearRow() {
        Row row = new Row(1, 2, 3);
        assertEquals(3, row.size());

        row.clear();
        assertTrue(row.isEmpty());
        assertEquals(0, row.size());
    }

    @Test
    public void testIterator() {
        Row row = new Row(1, 2, 3);
        int count = 0;
        for (Object val : row) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    public void testToArray() {
        Row row = new Row(1, "two", true);
        Object[] arr = row.toArray();

        assertEquals(3, arr.length);
        assertEquals(1, arr[0]);
        assertEquals("two", arr[1]);
        assertEquals(true, arr[2]);
    }

    @Test
    public void testContainsValue() {
        Row row = new Row(1, "Alice", 3.14);

        assertTrue(row.contains(1));
        assertTrue(row.contains("Alice"));
        assertTrue(row.contains(3.14));
        assertFalse(row.contains("Bob"));
        assertFalse(row.contains(null));
    }

    @Test
    public void testIndexOf() {
        Row row = new Row(1, "Bob", 1);

        assertEquals(0, row.indexOf(1));
        assertEquals(1, row.indexOf("Bob"));
        assertEquals(-1, row.indexOf("NotFound"));
    }
}
