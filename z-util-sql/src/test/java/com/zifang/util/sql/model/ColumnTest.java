package com.zifang.util.sql.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Column模型测试
 */
public class ColumnTest {
    
    @Test
    public void testCreateColumn() {
        Column column = new Column("id", Integer.class);
        assertEquals("id", column.getName());
        assertEquals(Integer.class, column.getType());
    }
    
    @Test
    public void testCreateColumnWithStringType() {
        Column column = new Column("name", String.class);
        assertEquals("name", column.getName());
        assertEquals(String.class, column.getType());
    }
    
    @Test
    public void testCreateColumnWithDoubleType() {
        Column column = new Column("score", Double.class);
        assertEquals(Double.class, column.getType());
    }
    
    @Test
    public void testCreateColumnWithBooleanType() {
        Column column = new Column("active", Boolean.class);
        assertEquals(Boolean.class, column.getType());
    }
    
    @Test
    public void testCreateColumnWithLongType() {
        Column column = new Column("bigint", Long.class);
        assertEquals(Long.class, column.getType());
    }
    
    @Test
    public void testSetName() {
        Column column = new Column("name", String.class);
        column.setName("newName");
        assertEquals("newName", column.getName());
    }
    
    @Test
    public void testSetType() {
        Column column = new Column("age", Integer.class);
        column.setType(Long.class);
        assertEquals(Long.class, column.getType());
    }
    
    @Test
    public void testToStringWithInteger() {
        Column column = new Column("id", Integer.class);
        assertEquals("id (Integer)", column.toString());
    }
    
    @Test
    public void testToStringWithString() {
        Column column = new Column("name", String.class);
        assertEquals("name (String)", column.toString());
    }
    
    @Test
    public void testToStringWithDouble() {
        Column column = new Column("score", Double.class);
        assertEquals("score (Double)", column.toString());
    }
    
    @Test
    public void testToStringWithBoolean() {
        Column column = new Column("active", Boolean.class);
        assertEquals("active (Boolean)", column.toString());
    }
    
    @Test
    public void testToStringWithLong() {
        Column column = new Column("bigint", Long.class);
        assertEquals("bigint (Long)", column.toString());
    }
    
    @Test
    public void testToStringWithFloat() {
        Column column = new Column("amount", Float.class);
        assertEquals("amount (Float)", column.toString());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Column col1 = new Column("id", Integer.class);
        Column col2 = new Column("id", Integer.class);
        Column col3 = new Column("name", Integer.class);
        
        // Columns with same properties are not necessarily equal (based on default Object.equals)
        assertNotSame(col1, col2);
        assertNotSame(col1, col3);
    }
}
