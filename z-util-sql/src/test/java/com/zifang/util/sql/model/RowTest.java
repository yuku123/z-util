package com.zifang.util.sql.model;

import org.junit.Test;

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
        Row row = new Row(new Object[]{1, "Alice", true});
        assertEquals(3, row.size());
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));
        assertEquals(true, row.get(2));
    }
    
    @Test
    public void testGetSet() {
        Row row = new Row(3);
        row.set(0, 1);
        row.set(1, "Alice");
        row.set(2, 30);
        
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));
        assertEquals(30, row.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativeIndex() {
        Row row = new Row(3);
        row.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndex() {
        Row row = new Row(3);
        row.get(5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetNegativeIndex() {
        Row row = new Row(3);
        row.set(-1, "value");
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsIndex() {
        Row row = new Row(3);
        row.set(5, "value");
    }
    
    @Test
    public void testGetValues() {
        Object[] values = new Object[]{1, "Alice"};
        Row row = new Row(values);
        
        Object[] retrieved = row.getValues();
        assertEquals(values.length, retrieved.length);
        assertEquals(1, retrieved[0]);
        assertEquals("Alice", retrieved[1]);
    }
    
    @Test
    public void testSetValues() {
        Row row = new Row(3);
        row.setValues(new Object[]{1, "Alice", true});
        
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));
        assertEquals(true, row.get(2));
    }
    
    @Test
    public void testToString() {
        Row row = new Row(new Object[]{1, "Alice", 30});
        String str = row.toString();
        assertTrue(str.contains("1"));
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("30"));
    }
    
    @Test
    public void testNullValues() {
        Row row = new Row(3);
        row.set(0, null);
        row.set(1, null);
        row.set(2, "value");
        
        assertNull(row.get(0));
        assertNull(row.get(1));
        assertEquals("value", row.get(2));
    }
}
