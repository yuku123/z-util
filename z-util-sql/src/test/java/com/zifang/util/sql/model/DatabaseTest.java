package com.zifang.util.sql.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Database模型测试
 */
public class DatabaseTest {
    
    @Test
    public void testCreateDatabase() {
        Database db = new Database("testdb");
        assertEquals("testdb", db.getName());
        assertEquals(0, db.getTableCount());
        assertTrue(db.getTableNames().isEmpty());
    }
    
    @Test
    public void testAddTable() {
        Database db = new Database("testdb");
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        
        db.addTable(table);
        
        assertEquals(1, db.getTableCount());
        assertTrue(db.hasTable("users"));
        assertFalse(db.hasTable("orders"));
    }
    
    @Test
    public void testCreateTable() {
        Database db = new Database("testdb");
        Table table = db.createTable("users");
        
        assertNotNull(table);
        assertEquals("users", table.getName());
        assertTrue(db.hasTable("users"));
    }
    
    @Test
    public void testGetTable() {
        Database db = new Database("testdb");
        Table users = db.createTable("users");
        Table orders = db.createTable("orders");
        
        assertEquals(users, db.getTable("users"));
        assertEquals(orders, db.getTable("orders"));
        assertNull(db.getTable("nonexistent"));
    }
    
    @Test
    public void testDropTable() {
        Database db = new Database("testdb");
        db.createTable("users");
        db.createTable("orders");
        
        assertEquals(2, db.getTableCount());
        
        db.dropTable("users");
        
        assertEquals(1, db.getTableCount());
        assertFalse(db.hasTable("users"));
        assertTrue(db.hasTable("orders"));
    }
    
    @Test
    public void testGetTables() {
        Database db = new Database("testdb");
        db.createTable("users");
        db.createTable("orders");
        db.createTable("products");
        
        assertEquals(3, db.getTables().size());
    }
    
    @Test
    public void testTableNameCaseInsensitive() {
        Database db = new Database("testdb");
        db.createTable("Users");
        
        assertTrue(db.hasTable("users"));
        assertTrue(db.hasTable("Users"));
        assertTrue(db.hasTable("USERS"));
    }
    
    @Test
    public void testSetName() {
        Database db = new Database("testdb");
        db.setName("newdb");
        assertEquals("newdb", db.getName());
    }
    
    @Test
    public void testToString() {
        Database db = new Database("testdb");
        db.createTable("users");
        
        String str = db.toString();
        assertTrue(str.contains("Database: testdb"));
        assertTrue(str.contains("Tables: 1"));
        assertTrue(str.contains("users"));
    }
}
