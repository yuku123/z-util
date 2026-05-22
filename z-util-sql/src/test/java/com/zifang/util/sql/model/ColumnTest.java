package com.zifang.util.sql.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Column模型测试
 */
public class ColumnTest {

    @Test
    public void testCreateColumn() {
        Column col = new Column("id", Integer.class);
        assertEquals("id", col.getName());
        assertEquals(Integer.class, col.getType());
    }

    @Test
    public void testCreateColumnWithDifferentTypes() {
        assertEquals(Integer.class, new Column("i", Integer.class).getType());
        assertEquals(Long.class, new Column("l", Long.class).getType());
        assertEquals(Double.class, new Column("d", Double.class).getType());
        assertEquals(String.class, new Column("s", String.class).getType());
        assertEquals(Boolean.class, new Column("b", Boolean.class).getType());
        assertEquals(Float.class, new Column("f", Float.class).getType());
    }

    @Test
    public void testColumnEquals() {
        Column c1 = new Column("id", Integer.class);
        Column c2 = new Column("id", Integer.class);
        Column c3 = new Column("name", Integer.class);
        Column c4 = new Column("id", String.class);

        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertNotEquals(c1, c4);
    }

    @Test
    public void testColumnHashCode() {
        Column c1 = new Column("id", Integer.class);
        Column c2 = new Column("id", Integer.class);

        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    public void testColumnToString() {
        Column col = new Column("id", Integer.class);
        String str = col.toString();

        assertTrue(str.contains("id"));
        assertTrue(str.contains("Integer"));
    }

    @Test
    public void testColumnSetName() {
        Column col = new Column("old", Integer.class);
        col.setName("new");
        assertEquals("new", col.getName());
    }

    @Test
    public void testColumnSetType() {
        Column col = new Column("id", Integer.class);
        col.setType(String.class);
        assertEquals(String.class, col.getType());
    }
}
