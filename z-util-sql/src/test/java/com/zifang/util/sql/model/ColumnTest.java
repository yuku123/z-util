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
    public void testToString() {
        Column column = new Column("id", Integer.class);
        assertEquals("id (Integer)", column.toString());
    }
    
    @Test
    public void testToStringWithStringType() {
        Column column = new Column("name", String.class);
        assertEquals("name (String)", column.toString());
    }
    
    @Test
    public void testToStringWithDoubleType() {
        Column column = new Column("score", Double.class);
        assertEquals("score (Double)", column.toString());
    }
    
    @Test
    public void testToStringWithBooleanType() {
        Column column = new Column("active", Boolean.class);
        assertEquals("active (Boolean)", column.toString());
    }
}
