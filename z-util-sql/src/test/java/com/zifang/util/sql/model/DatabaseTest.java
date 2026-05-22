package com.zifang.util.sql.model;

import org.junit.Test;
import java.util.Collection;
import java.util.Set;
import static org.junit.Assert.*;

/**
 * Database模型测试
 */
public class DatabaseTest {

    @Test
    public void testCreateDatabase() {
        Database db = new Database("test");
        assertEquals("test", db.getName());
    }

    @Test
    public void testSetName() {
        Database db = new Database("old");
        db.setName("new");
        assertEquals("new", db.getName());
    }

    @Test
    public void testCreateTable() {
        Database db = new Database("test");
        Table table = db.createTable("users");

        assertNotNull(table);
        assertEquals("users", table.getName());
        assertTrue(db.hasTable("users"));
    }

    @Test
    public void testCreateTableWithColumns() {
        Database db = new Database("test");
        Table table = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );

        assertEquals(2, table.getColumnCount());
        assertTrue(db.hasTable("users"));
    }

    @Test
    public void testGetTable() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));

        Table table = db.getTable("users");
        assertNotNull(table);
        assertEquals("users", table.getName());
    }

    @Test
    public void testGetNonExistentTable() {
        Database db = new Database("test");
        assertNull(db.getTable("notexist"));
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
    public void testDropTable() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        assertTrue(db.hasTable("users"));

        boolean result = db.dropTable("users");
        assertTrue(result);
        assertFalse(db.hasTable("users"));
    }

    @Test
    public void testDropNonExistentTable() {
        Database db = new Database("test");
        boolean result = db.dropTable("notexist");
        assertFalse(result);
    }

    @Test
    public void testDropTableCaseInsensitive() {
        Database db = new Database("test");
        db.createTable("Users", new Column("id", Integer.class));

        assertTrue(db.dropTable("users"));
        assertFalse(db.hasTable("Users"));
    }

    @Test
    public void testGetTableNames() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        db.createTable("orders", new Column("id", Integer.class));
        db.createTable("products", new Column("id", Integer.class));

        Set<String> names = db.getTableNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("users"));
        assertTrue(names.contains("orders"));
        assertTrue(names.contains("products"));
    }

    @Test
    public void testGetTables() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        db.createTable("orders", new Column("id", Integer.class));

        Collection<Table> tables = db.getTables();
        assertEquals(2, tables.size());
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
    public void testClearAllTables() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        db.createTable("orders", new Column("id", Integer.class));

        db.clearAllTables();
        assertEquals(0, db.getTableCount());
        assertFalse(db.hasTable("users"));
        assertFalse(db.hasTable("orders"));
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
