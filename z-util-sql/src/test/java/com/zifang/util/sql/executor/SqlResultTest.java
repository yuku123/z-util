package com.zifang.util.sql.executor;

import com.zifang.util.sql.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SqlResult测试 - 包含格式化输出测试
 */
public class SqlResultTest {
    
    @Test
    public void testCreateSuccessResult() {
        SqlResult result = new SqlResult(true, "Query OK", 5);
        
        assertTrue(result.isSuccess());
        assertEquals("Query OK", result.getMessage());
        assertEquals(5, result.getAffectedRows());
        assertNull(result.getResultTable());
    }
    
    @Test
    public void testCreateSuccessResultWithTable() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1, "Alice");
        
        SqlResult result = new SqlResult(true, "Query OK", 1, table);
        
        assertTrue(result.isSuccess());
        assertNotNull(result.getResultTable());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testCreateFailureResult() {
        SqlResult result = new SqlResult(false, "Table not found", 0);
        
        assertFalse(result.isSuccess());
        assertEquals("Table not found", result.getMessage());
        assertEquals(0, result.getAffectedRows());
    }
    
    @Test
    public void testSetters() {
        SqlResult result = new SqlResult(true, "OK", 0);
        
        result.setSuccess(false);
        result.setMessage("Error");
        result.setAffectedRows(10);
        
        assertFalse(result.isSuccess());
        assertEquals("Error", result.getMessage());
        assertEquals(10, result.getAffectedRows());
    }
    
    @Test
    public void testSetResultTable() {
        SqlResult result = new SqlResult(true, "OK", 0);
        assertNull(result.getResultTable());
        
        Table table = new Table("test");
        result.setResultTable(table);
        
        assertNotNull(result.getResultTable());
        assertEquals("test", result.getResultTable().getName());
    }
    
    @Test
    public void testToStringWithTable() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, "Alice");
        table.addRow(2, "Bob");
        
        SqlResult result = new SqlResult(true, "Query OK", 2, table);
        String str = result.toString();
        
        assertTrue(str.contains("Query OK"));
        assertTrue(str.contains("id"));
        assertTrue(str.contains("name"));
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("Bob"));
    }
    
    @Test
    public void testToStringWithoutTable() {
        SqlResult result = new SqlResult(true, "1 row updated", 1);
        String str = result.toString();
        
        assertEquals("1 row updated", str);
    }
    
    @Test
    public void testToStringWithEmptyTable() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        
        SqlResult result = new SqlResult(true, "Query OK", 0, table);
        String str = result.toString();
        
        assertTrue(str.contains("Query OK"));
        assertTrue(str.contains("users"));
    }
    
    @Test
    public void testToStringWithAllColumnTypes() {
        Table table = new Table("test");
        table.addColumn("intCol", Integer.class);
        table.addColumn("longCol", Long.class);
        table.addColumn("doubleCol", Double.class);
        table.addColumn("floatCol", Float.class);
        table.addColumn("boolCol", Boolean.class);
        table.addColumn("stringCol", String.class);
        table.addRow(42, 100L, 3.14, 2.71f, true, "hello");
        
        SqlResult result = new SqlResult(true, "OK", 1, table);
        String str = result.toString();
        
        assertTrue(str.contains("intCol"));
        assertTrue(str.contains("longCol"));
        assertTrue(str.contains("doubleCol"));
        assertTrue(str.contains("floatCol"));
        assertTrue(str.contains("boolCol"));
        assertTrue(str.contains("stringCol"));
        assertTrue(str.contains("42"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("3.14"));
        assertTrue(str.contains("2.71"));
        assertTrue(str.contains("true"));
        assertTrue(str.contains("hello"));
    }
    
    @Test
    public void testToStringWithManyRows() {
        Table table = new Table("numbers");
        table.addColumn("id", Integer.class);
        
        for (int i = 1; i <= 100; i++) {
            table.addRow(i);
        }
        
        SqlResult result = new SqlResult(true, "OK", 100, table);
        String str = result.toString();
        
        assertTrue(str.contains("100")); // affected rows
    }
    
    @Test
    public void testToStringWithNullValues() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addColumn("name", String.class);
        table.addRow(1, null);
        
        SqlResult result = new SqlResult(true, "OK", 1, table);
        String str = result.toString();
        
        assertTrue(str.contains("OK"));
        assertTrue(str.contains("users"));
    }
    
    @Test
    public void testToStringWithUnicode() {
        Table table = new Table("users");
        table.addColumn("name", String.class);
        table.addRow("张三");
        table.addRow("李四");
        
        SqlResult result = new SqlResult(true, "OK", 2, table);
        String str = result.toString();
        
        assertTrue(str.contains("张三"));
        assertTrue(str.contains("李四"));
    }
    
    @Test
    public void testFailureResultWithMessage() {
        SqlResult result = new SqlResult(false, "Syntax error in SQL", 0);
        
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Syntax error"));
        assertEquals(0, result.getAffectedRows());
        assertNull(result.getResultTable());
    }
    
    @Test
    public void testSuccessResultMessageTypes() {
        SqlResult insertResult = new SqlResult(true, "1 row inserted", 1);
        assertTrue(insertResult.getMessage().contains("inserted"));
        
        SqlResult updateResult = new SqlResult(true, "3 rows updated", 3);
        assertTrue(updateResult.getMessage().contains("updated"));
        
        SqlResult deleteResult = new SqlResult(true, "2 rows deleted", 2);
        assertTrue(deleteResult.getMessage().contains("deleted"));
    }
    
    @Test
    public void testResultWithZeroAffectedRows() {
        SqlResult result = new SqlResult(true, "No rows matched", 0);
        
        assertTrue(result.isSuccess());
        assertEquals(0, result.getAffectedRows());
        assertTrue(result.getMessage().contains("No rows"));
    }
    
    @Test
    public void testResultWithLargeAffectedRows() {
        SqlResult result = new SqlResult(true, "10000 rows affected", 10000);
        
        assertEquals(10000, result.getAffectedRows());
    }
}