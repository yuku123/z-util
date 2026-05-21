package com.zifang.util.sql.model;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Table模型完整测试 - 包含索引测试
 */
public class TableTest {
    
    // ========== 基本测试 ==========
    
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
        assertEquals(1, table.getColumnCount()); // 列定义还在
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
    
    @Test
    public void testGetRowInvalidIndex() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        
        assertNull(table.getRow(-1));
        assertNull(table.getRow(100));
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
        List<Integer> indices = table.getRowIndicesByIndex("id", 2);
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
        table.addColumn("age", Integer.class);
        table.addRow(1, "Alice", 25);
        table.addRow(2, "Bob", 30);
        table.addRow(3, "Charlie", 35);
        
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
        List<Integer> indices = table.getRowIndicesByIndex("name", "Alice");
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
        List<Integer> indices = table.scanRows("age", 30, ">");
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
        
        List<Integer> indices = table.scanRows("age", 30, "<");
        assertEquals(1, indices.size()); // 只有Alice(25)
    }
    
    @Test
    public void testScanRowsWithNotEqual() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        table.addRow(3, "Charlie");
        
        List<Integer> indices = table.scanRows("name", "Alice", "!=");
        assertEquals(2, indices.size());
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
        assertEquals(1, table.getRowIndicesByIndex("id", 3).size());
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
    public void testIndexOnNonExistentColumn() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        
        table.createIndex("nonexistent");
        
        // 不应该创建索引
        assertFalse(table.hasIndex("nonexistent"));
    }
    
    @Test
    public void testGetRowIndicesByNonIndexedColumn() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        
        // 没有创建索引，直接调用会返回null或空
        List<Integer> indices = table.getRowIndicesByIndex("name", "Alice");
        
        // 如果没有索引，应该返回null或者空
        // 具体行为取决于实现
        assertNotNull(indices);
    }
    
    @Test
    public void testMultipleIndexes() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("email", String.class);
        table.addColumn("name", String.class);
        table.addRow(1, "alice@test.com", "Alice");
        
        table.createIndex("id");
        table.createIndex("email");
        
        assertTrue(table.hasIndex("id"));
        assertTrue(table.hasIndex("email"));
        assertFalse(table.hasIndex("name"));
        
        assertEquals(1, table.getRowIndicesByIndex("id", 1).size());
        assertEquals(1, table.getRowIndicesByIndex("email", "alice@test.com").size());
    }
    
    // ========== 类型测试 ==========
    
    @Test
    public void testTableWithAllColumnTypes() {
        Table table = new Table("test");
        table.addColumn("intCol", Integer.class);
        table.addColumn("longCol", Long.class);
        table.addColumn("doubleCol", Double.class);
        table.addColumn("floatCol", Float.class);
        table.addColumn("boolCol", Boolean.class);
        table.addColumn("stringCol", String.class);
        
        table.addRow(1, 100L, 3.14, 2.71f, true, "test");
        
        assertEquals(6, table.getColumnCount());
        assertEquals(1, table.getRow(0).get(0));
        assertEquals(100L, table.getRow(0).get(1));
        assertEquals(3.14, table.getRow(0).get(2));
        assertEquals(2.71f, table.getRow(0).get(3));
        assertEquals(true, table.getRow(0).get(4));
        assertEquals("test", table.getRow(0).get(5));
    }
    
    // ========== 边界测试 ==========
    
    @Test
    public void testEmptyTable() {
        Table table = new Table("empty");
        assertTrue(table.getIndexedColumns().isEmpty());
        assertNull(table.getRow(0));
    }
    
    @Test
    public void testSingleRow() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        
        assertEquals(1, table.getRowCount());
        assertEquals(1, table.getRow(0).get(0));
    }
    
    @Test
    public void testSingleColumn() {
        Table table = new Table("ids");
        table.addColumn("id", Integer.class);
        table.addRow(1);
        table.addRow(2);
        table.addRow(3);
        
        assertEquals(1, table.getColumnCount());
        assertEquals(3, table.getRowCount());
    }
    
    @Test
    public void testLargeNumberOfRows() {
        Table table = new Table("numbers");
        table.addColumn("id", Integer.class);
        
        for (int i = 0; i < 1000; i++) {
            table.addRow(i);
        }
        
        assertEquals(1000, table.getRowCount());
        assertEquals(0, table.getRow(0).get(0));
        assertEquals(999, table.getRow(999).get(0));
    }
    
    @Test
    public void testLargeNumberOfColumns() {
        Table table = new Table("wide");
        
        for (int i = 0; i < 100; i++) {
            table.addColumn("col" + i, Integer.class);
        }
        
        assertEquals(100, table.getColumnCount());
    }
    
    // ========== Null值测试 ==========
    
    @Test
    public void testRowWithNullValues() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        
        table.addRow(1, null);
        
        assertEquals(1, table.getRowCount());
        assertNull(table.getRow(0).get(1));
    }
    
    @Test
    public void testIndexWithNullValues() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, null);
        table.addRow(3, "Bob");
        
        table.createIndex("name");
        
        // 应该能找到非null的值
        List<Integer> indices = table.getRowIndicesByIndex("name", "Alice");
        assertEquals(1, indices.size());
    }
    
    // ========== String列测试 ==========
    
    @Test
    public void testStringIndexLookup() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        table.addRow(3, "Charlie");
        
        table.createIndex("name");
        
        assertEquals(1, table.getRowIndicesByIndex("name", "Bob").size());
        assertEquals(0, table.getRowIndicesByIndex("name", "Unknown").size());
    }
    
    @Test
    public void testStringIndexWithSpecialCharacters() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice's House");
        table.addRow(2, "Bob's Cafe");
        
        table.createIndex("name");
        
        List<Integer> indices = table.getRowIndicesByIndex("name", "Alice's House");
        assertEquals(1, indices.size());
    }
    
    @Test
    public void testStringIndexWithUnicode() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "张三");
        table.addRow(2, "李四");
        
        table.createIndex("name");
        
        List<Integer> indices = table.getRowIndicesByIndex("name", "张三");
        assertEquals(1, indices.size());
    }
}