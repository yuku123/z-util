package com.zifang.util.sql.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Table模型测试 - 包含索引测试
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
        assertEquals(Integer.class, table.getColumn(0).getType());
        assertEquals("name", table.getColumn(1).getName());
        assertEquals(String.class, table.getColumn(1).getType());
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
    public void testGetColumnIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addColumn("age", Integer.class);
        
        assertEquals(0, table.getColumnIndex("id"));
        assertEquals(1, table.getColumnIndex("name"));
        assertEquals(2, table.getColumnIndex("age"));
        assertEquals(0, table.getColumnIndex("ID")); // 大小写不敏感
        assertEquals(-1, table.getColumnIndex("nonexistent"));
    }
    
    @Test
    public void testGetColumnByName() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        
        Column col = table.getColumn("name");
        assertNotNull(col);
        assertEquals("name", col.getName());
        assertEquals(String.class, col.getType());
    }
    
    @Test
    public void testClearRows() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        
        assertEquals(2, table.getRowCount());
        
        table.clearRows();
        assertEquals(0, table.getRowCount());
        assertEquals(2, table.getColumnCount()); // 列定义还在
    }
    
    @Test
    public void testCreateEmptyCopy() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1, "Alice");
        
        Table copy = table.createEmptyCopy();
        assertEquals("users", copy.getName());
        assertEquals(1, copy.getColumnCount());
        assertEquals(0, copy.getRowCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddRowWithWrongSize() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        
        table.addRow(1); // 缺少name
    }
    
    // ========== 索引测试 ==========
    
    @Test
    public void testCreateIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        table.addRow(3, "Alice");
        
        // 创建id列的索引
        table.createIndex("id");
        
        assertTrue(table.hasIndex("id"));
        assertFalse(table.hasIndex("name"));
        
        // 查找id=2的行
        java.util.List<Integer> indices = table.getRowIndicesByIndex("id", 2);
        assertEquals(1, indices.size());
        assertEquals(2, table.getRow(indices.get(0)).get(0));
    }
    
    @Test
    public void testCreateAllIndexes() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        
        table.createAllIndexes();
        
        assertTrue(table.hasIndex("id"));
        assertTrue(table.hasIndex("name"));
    }
    
    @Test
    public void testDropIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1, "Alice");
        
        table.createIndex("id");
        assertTrue(table.hasIndex("id"));
        
        table.dropIndex("id");
        assertFalse(table.hasIndex("id"));
    }
    
    @Test
    public void testIndexLookups() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        table.addRow(3, "Charlie");
        
        table.createIndex("id");
        
        // 精确查找
        assertEquals(1, table.getRowIndicesByIndex("id", 1).size());
        assertEquals(1, table.getRowIndicesByIndex("id", 2).size());
        
        // 不存在的值
        assertEquals(0, table.getRowIndicesByIndex("id", 99).size());
    }
    
    @Test
    public void testIndexWithDuplicateValues() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Alice");
        table.addRow(3, "Alice");
        
        table.createIndex("name");
        
        // 查找所有name="Alice"的行
        java.util.List<Integer> indices = table.getRowIndicesByIndex("name", "Alice");
        assertEquals(3, indices.size());
    }
    
    @Test
    public void testScanRowsWithOperators() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addColumn("age", Integer.class);
        table.addRow(1, "Alice", 25);
        table.addRow(2, "Bob", 30);
        table.addRow(3, "Charlie", 35);
        
        // 全表扫描 with > operator
        java.util.List<Integer> indices = table.scanRows("age", 30, ">");
        assertEquals(1, indices.size()); // 只有Charlie(35)
    }
    
    @Test
    public void testScanRowsWithLessThan() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("age", Integer.class);
        table.addRow(1, 25);
        table.addRow(2, 30);
        table.addRow(3, 35);
        
        java.util.List<Integer> indices = table.scanRows("age", 30, "<");
        assertEquals(1, indices.size()); // 只有Alice(25)
    }
    
    @Test
    public void testRebuildIndexes() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        
        table.createIndex("id");
        
        // 添加更多数据
        table.addRow(3, "Charlie");
        
        // 重建索引
        table.rebuildIndexes();
        
        // 索引仍然有效
        assertTrue(table.hasIndex("id"));
        assertEquals(3, table.getRowIndicesByIndex("id", 3).size());
    }
    
    @Test
    public void testGetIndexedColumns() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        
        assertTrue(table.getIndexedColumns().isEmpty());
        
        table.createIndex("id");
        table.createIndex("name");
        
        assertEquals(2, table.getIndexedColumns().size());
    }
    
    @Test
    public void testToString() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1, "Alice");
        
        String str = table.toString();
        assertTrue(str.contains("Table: users"));
        assertTrue(str.contains("Columns:"));
        assertTrue(str.contains("Rows: 1"));
    }
}
