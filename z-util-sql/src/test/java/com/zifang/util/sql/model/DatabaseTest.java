package com.zifang.util.sql.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Database模型完整测试
 */
public class DatabaseTest {
    
    @Test
    public void testCreateDatabase() {
        Database db = new Database("test");
        assertEquals("test", db.getName());
    }
    
    @Test
    public void testCreateTable() {
        Database db = new Database("test");
        db.createTable("users", 
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        
        assertTrue(db.hasTable("users"));
        assertNotNull(db.getTable("users"));
    }
    
    @Test
    public void testDropTable() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        
        assertTrue(db.hasTable("users"));
        
        db.dropTable("users");
        assertFalse(db.hasTable("users"));
    }
    
    @Test
    public void testGetTableNames() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        db.createTable("orders", new Column("id", Integer.class));
        db.createTable("products", new Column("id", Integer.class));
        
        assertEquals(3, db.getTableNames().size());
        assertTrue(db.getTableNames().contains("users"));
        assertTrue(db.getTableNames().contains("orders"));
        assertTrue(db.getTableNames().contains("products"));
    }
    
    @Test
    public void testGetTableCount() {
        Database db = new Database("test");
        assertEquals(0, db.getTableCount());
        
        db.createTable("users", new Column("id", Integer.class));
        assertEquals(1, db.getTableCount());
        
        db.createTable("orders", new Column("id", Integer.class));
        assertEquals(2, db.getTableCount());
    }
    
    @Test
    public void testGetTableByName() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        
        Table table = db.getTable("users");
        assertNotNull(table);
        assertEquals("users", table.getName());
    }
    
    @Test
    public void testGetNonExistentTable() {
        Database db = new Database("test");
        
        Table table = db.getTable("nonexistent");
        assertNull(table);
    }
    
    @Test
    public void testHasTableCaseInsensitive() {
        Database db = new Database("test");
        db.createTable("Users", new Column("id", Integer.class));
        
        assertTrue(db.hasTable("users"));
        assertTrue(db.hasTable("USERS"));
        assertTrue(db.hasTable("Users"));
    }
    
    @Test
    public void testCreateTableWithColumnDefinitions() {
        Database db = new Database("test");
        Table table = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class),
            new Column("age", Integer.class)
        );
        
        assertEquals(3, table.getColumnCount());
        assertEquals("id", table.getColumn(0).getName());
        assertEquals(Integer.class, table.getColumn(0).getType());
    }
    
    @Test
    public void testDropNonExistentTable() {
        Database db = new Database("test");
        
        // 应该不抛异常，只是返回false
        assertFalse(db.dropTable("nonexistent"));
    }
    
    @Test
    public void testClearAllTables() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        db.createTable("orders", new Column("id", Integer.class));
        
        db.clearAllTables();
        
        assertEquals(0, db.getTableCount());
    }
    
    @Test
    public void testDatabaseWithData() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        
        users.addRow(1, "Alice");
        users.addRow(2, "Bob");
        
        Table retrieved = db.getTable("users");
        assertEquals(2, retrieved.getRowCount());
    }
    
    @Test
    public void testMultipleTablesWithData() {
        Database db = new Database("test");
        
        Table users = db.createTable("users", 
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");
        
        Table orders = db.createTable("orders",
            new Column("id", Integer.class),
            new Column("user_id", Integer.class)
        );
        orders.addRow(1, 1);
        
        assertEquals(1, db.getTable("users").getRowCount());
        assertEquals(1, db.getTable("orders").getRowCount());
    }
    
    @Test
    public void testTableIsolation() {
        Database db = new Database("test");
        Table table1 = db.createTable("users", new Column("id", Integer.class));
        Table table2 = db.createTable("orders", new Column("id", Integer.class));
        
        table1.addRow(1);
        table2.addRow(1);
        table2.addRow(2);
        
        assertEquals(1, table1.getRowCount());
        assertEquals(2, table2.getRowCount());
        assertEquals(1, db.getTable("users").getRowCount());
        assertEquals(2, db.getTable("orders").getRowCount());
    }
    
    @Test
    public void testToString() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        
        String str = db.toString();
        assertTrue(str.contains("test"));
        assertTrue(str.contains("users"));
    }
    
    @Test
    public void testDropTableThenRecreate() {
        Database db = new Database("test");
        
        db.createTable("users", new Column("id", Integer.class));
        assertTrue(db.hasTable("users"));
        
        db.dropTable("users");
        assertFalse(db.hasTable("users"));
        
        db.createTable("users", new Column("id", Long.class));
        assertTrue(db.hasTable("users"));
        assertEquals(Long.class, db.getTable("users").getColumn(0).getType());
    }
}