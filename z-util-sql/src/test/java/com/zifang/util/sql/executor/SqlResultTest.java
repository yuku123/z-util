package com.zifang.util.sql.executor;

import com.zifang.util.sql.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SqlResult测试
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
}
