package com.zifang.util.sql.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Column模型完整测试
 */
public class ColumnTest {
    
    @Test
    public void testCreateColumn() {
        Column col = new Column("id", Integer.class);
        
        assertEquals("id", col.getName());
        assertEquals(Integer.class, col.getType());
    }
    
    @Test
    public void testColumnWithStringType() {
        Column col = new Column("name", String.class);
        
        assertEquals("name", col.getName());
        assertEquals(String.class, col.getType());
    }
    
    @Test
    public void testColumnWithDoubleType() {
        Column col = new Column("price", Double.class);
        
        assertEquals("price", col.getName());
        assertEquals(Double.class, col.getType());
    }
    
    @Test
    public void testColumnWithBooleanType() {
        Column col = new Column("active", Boolean.class);
        
        assertEquals("active", col.getName());
        assertEquals(Boolean.class, col.getType());
    }
    
    @Test
    public void testColumnWithLongType() {
        Column col = new Column("bigvalue", Long.class);
        
        assertEquals("bigvalue", col.getName());
        assertEquals(Long.class, col.getType());
    }
    
    @Test
    public void testColumnWithFloatType() {
        Column col = new Column("amount", Float.class);
        
        assertEquals("amount", col.getName());
        assertEquals(Float.class, col.getType());
    }
    
    @Test
    public void testColumnSetName() {
        Column col = new Column("id", Integer.class);
        col.setName("userId");
        
        assertEquals("userId", col.getName());
    }
    
    @Test
    public void testColumnSetType() {
        Column col = new Column("id", Integer.class);
        col.setType(Long.class);
        
        assertEquals(Long.class, col.getType());
    }
    
    @Test
    public void testColumnToString() {
        Column col = new Column("id", Integer.class);
        String str = col.toString();
        
        assertTrue(str.contains("id"));
        assertTrue(str.contains("Integer"));
    }
    
    @Test
    public void testColumnEquals() {
        Column col1 = new Column("id", Integer.class);
        Column col2 = new Column("id", Integer.class);
        
        assertEquals(col1, col2);
        assertEquals(col1.hashCode(), col2.hashCode());
    }
    
    @Test
    public void testColumnNotEquals() {
        Column col1 = new Column("id", Integer.class);
        Column col2 = new Column("name", Integer.class);
        
        assertNotEquals(col1, col2);
    }
    
    @Test
    public void testColumnNotEqualsDifferentType() {
        Column col1 = new Column("id", Integer.class);
        Column col2 = new Column("id", Long.class);
        
        assertNotEquals(col1, col2);
    }
    
    @Test
    public void testColumnNotEqualsNull() {
        Column col = new Column("id", Integer.class);
        
        assertNotEquals(col, null);
    }
    
    @Test
    public void testColumnNotEqualsDifferentClass() {
        Column col = new Column("id", Integer.class);
        
        assertNotEquals(col, "not a column");
    }
    
    @Test
    public void testColumnWithAllTypes() {
        Column intCol = new Column("intCol", Integer.class);
        Column longCol = new Column("longCol", Long.class);
        Column doubleCol = new Column("doubleCol", Double.class);
        Column floatCol = new Column("floatCol", Float.class);
        Column boolCol = new Column("boolCol", Boolean.class);
        Column stringCol = new Column("stringCol", String.class);
        
        assertEquals(Integer.class, intCol.getType());
        assertEquals(Long.class, longCol.getType());
        assertEquals(Double.class, doubleCol.getType());
        assertEquals(Float.class, floatCol.getType());
        assertEquals(Boolean.class, boolCol.getType());
        assertEquals(String.class, stringCol.getType());
    }
}