package com.zifang.util.sql.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Row模型完整测试
 */
public class RowTest {
    
    @Test
    public void testCreateRow() {
        Row row = new Row();
        assertEquals(0, row.size());
    }
    
    @Test
    public void testAddValue() {
        Row row = new Row();
        row.add(1);
        row.add("Alice");
        
        assertEquals(2, row.size());
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));
    }
    
    @Test
    public void testSetValue() {
        Row row = new Row();
        row.add(1);
        row.add("Alice");
        
        row.set(1, "Bob");
        
        assertEquals("Bob", row.get(1));
    }
    
    @Test
    public void testGetInvalidIndex() {
        Row row = new Row();
        row.add(1);
        
        assertNull(row.get(-1));
        assertNull(row.get(100));
    }
    
    @Test
    public void testSetInvalidIndex() {
        Row row = new Row();
        row.add(1);
        
        row.set(-1, "Alice");
        row.set(100, "Bob");
        
        // 应该不抛异常，只是忽略
        assertEquals(1, row.size());
    }
    
    @Test
    public void testRowWithIntegers() {
        Row row = new Row();
        row.add(1);
        row.add(2);
        row.add(3);
        
        assertEquals(3, row.size());
        assertEquals(1, row.get(0));
        assertEquals(2, row.get(1));
        assertEquals(3, row.get(2));
    }
    
    @Test
    public void testRowWithStrings() {
        Row row = new Row();
        row.add("Alice");
        row.add("Bob");
        row.add("Charlie");
        
        assertEquals(3, row.size());
        assertEquals("Alice", row.get(0));
        assertEquals("Bob", row.get(1));
        assertEquals("Charlie", row.get(2));
    }
    
    @Test
    public void testRowWithMixedTypes() {
        Row row = new Row();
        row.add(1);
        row.add("Alice");
        row.add(3.14);
        row.add(true);
        
        assertEquals(4, row.size());
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));
        assertEquals(3.14, row.get(2));
        assertEquals(true, row.get(3));
    }
    
    @Test
    public void testRowWithDoubles() {
        Row row = new Row();
        row.add(3.14159);
        row.add(-273.15);
        row.add(0.0);
        
        assertEquals(3, row.size());
        assertEquals(3.14159, row.get(0));
        assertEquals(-273.15, row.get(1));
        assertEquals(0.0, row.get(2));
    }
    
    @Test
    public void testRowWithBooleans() {
        Row row = new Row();
        row.add(true);
        row.add(false);
        
        assertEquals(2, row.size());
        assertEquals(true, row.get(0));
        assertEquals(false, row.get(1));
    }
    
    @Test
    public void testRowWithLongs() {
        Row row = new Row();
        row.add(9223372036854775807L);
        row.add(-9223372036854775808L);
        
        assertEquals(2, row.size());
        assertEquals(9223372036854775807L, row.get(0));
        assertEquals(-9223372036854775808L, row.get(1));
    }
    
    @Test
    public void testRowWithNull() {
        Row row = new Row();
        row.add(null);
        
        assertEquals(1, row.size());
        assertNull(row.get(0));
    }
    
    @Test
    public void testRowWithMixedNullAndValues() {
        Row row = new Row();
        row.add(1);
        row.add(null);
        row.add("Alice");
        
        assertEquals(3, row.size());
        assertEquals(1, row.get(0));
        assertNull(row.get(1));
        assertEquals("Alice", row.get(2));
    }
    
    @Test
    public void testRowClear() {
        Row row = new Row();
        row.add(1);
        row.add("Alice");
        
        row.clear();
        
        assertEquals(0, row.size());
    }
    
    @Test
    public void testRowIsEmpty() {
        Row row = new Row();
        assertTrue(row.isEmpty());
        
        row.add(1);
        assertFalse(row.isEmpty());
    }
    
    @Test
    public void testRowToString() {
        Row row = new Row();
        row.add(1);
        row.add("Alice");
        
        String str = row.toString();
        assertNotNull(str);
        assertTrue(str.contains("1"));
        assertTrue(str.contains("Alice"));
    }
    
    @Test
    public void testRowEquals() {
        Row row1 = new Row();
        row1.add(1);
        row1.add("Alice");
        
        Row row2 = new Row();
        row2.add(1);
        row2.add("Alice");
        
        assertEquals(row1, row2);
        assertEquals(row1.hashCode(), row2.hashCode());
    }
    
    @Test
    public void testRowNotEquals() {
        Row row1 = new Row();
        row1.add(1);
        row1.add("Alice");
        
        Row row2 = new Row();
        row2.add(1);
        row2.add("Bob");
        
        assertNotEquals(row1, row2);
    }
    
    @Test
    public void testRowNotEqualsDifferentSize() {
        Row row1 = new Row();
        row1.add(1);
        
        Row row2 = new Row();
        row2.add(1);
        row2.add("Alice");
        
        assertNotEquals(row1, row2);
    }
    
    @Test
    public void testRowNotEqualsNull() {
        Row row = new Row();
        row.add(1);
        
        assertNotEquals(row, null);
    }
    
    @Test
    public void testRowIterator() {
        Row row = new Row();
        row.add(1);
        row.add(2);
        row.add(3);
        
        int sum = 0;
        for (Object val : row) {
            sum += (Integer) val;
        }
        
        assertEquals(6, sum);
    }
}