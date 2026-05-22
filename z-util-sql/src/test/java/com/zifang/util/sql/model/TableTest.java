package com.zifang.util.sql.model;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Table模型测试
 */
public class TableTest {

    @Test
    public void testCreateTable() {
        Table table = new Table("users");
        assertEquals("users", table.getName());
        assertEquals(0, table.getColumnCount());
        assertEquals(0, table.getRowCount());
    }

    @Test
    public void testAddColumn() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);

        assertEquals(2, table.getColumnCount());
        assertEquals("id", table.getColumn(0).getName());
        assertEquals("name", table.getColumn(1).getName());
    }

    @Test
    public void testAddRow() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);

        table.addRow(1, "Alice");
        table.addRow(2, "Bob");

        assertEquals(2, table.getRowCount());
        assertEquals(1, table.getRow(0).get(0));
        assertEquals("Alice", table.getRow(0).get(1));
    }

    @Test
    public void testGetRowInvalidIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);

        assertNull(table.getRow(-1));
        assertNull(table.getRow(0));
        assertNull(table.getRow(100));
    }

    @Test
    public void testGetRows() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);

        List<Row> rows = table.getRows();
        assertEquals(2, rows.size());
    }

    @Test
    public void testClearRows() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);

        table.clearRows();
        assertEquals(0, table.getRowCount());
    }

    @Test
    public void testRemoveRow() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);
        table.addRow(3);

        table.removeRow(1);
        assertEquals(2, table.getRowCount());
        assertEquals(1, table.getRow(0).get(0));
        assertEquals(3, table.getRow(1).get(0));
    }

    @Test
    public void testGetColumnIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addColumn("age", Integer.class);

        assertEquals(0, table.getColumnIndex("id"));
        assertEquals(1, table.getColumnIndex("name"));
        assertEquals(2, table.getColumnIndex("age"));
        assertEquals(-1, table.getColumnIndex("notexist"));
    }

    @Test
    public void testGetColumnIndexCaseInsensitive() {
        Table table = new Table("users");
        table.addColumn("Name", String.class);

        assertEquals(0, table.getColumnIndex("name"));
        assertEquals(0, table.getColumnIndex("NAME"));
    }

    @Test
    public void testHasColumn() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);

        assertTrue(table.hasColumn("id"));
        assertFalse(table.hasColumn("name"));
    }

    // ========== 索引测试 ==========

    @Test
    public void testCreateIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("email", String.class);
        table.addRow(1, "a@test.com");
        table.addRow(2, "b@test.com");
        table.addRow(3, "c@test.com");

        table.createIndex("email");

        assertTrue(table.getIndexedColumns().contains("email"));
    }

    @Test
    public void testCreateIndexOnNonExistentColumn() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);

        try {
            table.createIndex("notexist");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("notexist"));
        }
    }

    @Test
    public void testDropIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);

        table.createIndex("id");
        assertTrue(table.getIndexedColumns().contains("id"));

        table.dropIndex("id");
        assertFalse(table.getIndexedColumns().contains("id"));
    }

    @Test
    public void testFindRowsByIndexValue() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("dept_id", Integer.class);
        table.addRow(1, 10);
        table.addRow(2, 20);
        table.addRow(3, 10);
        table.addRow(4, 10);

        table.createIndex("dept_id");

        List<Integer> results = table.findRowsByIndexValue("dept_id", 10);
        assertEquals(3, results.size());
    }

    @Test
    public void testFindRowsByIndexValueNotFound() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);

        table.createIndex("id");

        List<Integer> results = table.findRowsByIndexValue("id", 999);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testRebuildIndexes() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);
        table.addRow(3);

        table.createIndex("id");
        table.addRow(4);

        table.rebuildIndexes();
        // id=4 only appears in 1 row
        assertEquals(1, table.findRowsByIndexValue("id", 4).size());
        // total 4 rows
        assertEquals(4, table.getRowCount());
    }

    @Test
    public void testIndexOnNonExistentColumn() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);

        table.createIndex("id");
        assertTrue(table.getIndexedColumns().contains("id"));
    }

    @Test
    public void testMultipleIndexes() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("email", String.class);
        table.addColumn("dept_id", Integer.class);
        table.addRow(1, "a@test.com", 10);

        table.createIndex("id");
        table.createIndex("email");
        table.createIndex("dept_id");

        assertEquals(3, table.getIndexedColumns().size());
    }

    // ========== 复制测试 ==========

    @Test
    public void testCreateEmptyCopy() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);

        Table copy = table.createEmptyCopy();
        assertEquals("users", copy.getName());
        assertEquals(2, copy.getColumnCount());
        assertEquals(0, copy.getRowCount());
    }

    @Test
    public void testCopy() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");

        Table copy = table.copy();
        assertEquals(1, copy.getRowCount());
        assertEquals(1, copy.getRow(0).get(0));
        assertEquals("Alice", copy.getRow(0).get(1));

        copy.getRow(0).set(1, "Bob");
        assertEquals("Alice", table.getRow(0).get(1));
    }

    // ========== toString 测试 ==========

    @Test
    public void testToString() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);

        String str = table.toString();
        assertTrue(str.contains("users"));
        assertTrue(str.contains("id"));
    }
}
