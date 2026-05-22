package com.zifang.util.sql.executor;

import com.zifang.util.sql.model.*;
import com.zifang.util.sql.parser.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SqlExecutor测试
 */
public class SqlExecutorTest {

    private Database createTestDatabase() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");
        users.addRow(2, "Bob");
        users.addRow(3, "Charlie");
        return db;
    }

    // ========== INSERT 测试 ==========

    @Test
    public void testInsert() {
        Database db = new Database("test");
        db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );

        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");

        assertTrue(result.isSuccess());
        assertEquals(1, db.getTable("users").getRowCount());
    }

    @Test
    public void testInsertMultiple() {
        Database db = new Database("test");
        db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );

        SqlExecutor executor = new SqlExecutor(db);
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Charlie')");

        assertEquals(3, db.getTable("users").getRowCount());
    }

    @Test
    public void testInsertIntoNonExistentTable() {
        Database db = new Database("test");
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("INSERT INTO notexist (id) VALUES (1)");

        assertFalse(result.isSuccess());
    }

    // ========== SELECT 测试 ==========

    @Test
    public void testSelectAll() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users");

        assertTrue(result.isSuccess());
        assertEquals(3, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectSpecificColumns() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT name FROM users");

        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getColumnCount());
        assertEquals("Alice", result.getResultTable().getRow(0).get(0));
    }

    @Test
    public void testSelectNoResults() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 999");

        assertTrue(result.isSuccess());
        assertEquals(0, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectWhereEqual() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 1");

        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Alice", result.getResultTable().getRow(0).get(1));
    }

    @Test
    public void testSelectWhereGreaterThan() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users WHERE id > 1");

        assertEquals(2, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectWhereLessThan() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users WHERE id < 3");

        assertEquals(2, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectWhereNotEqual() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users WHERE id != 2");

        assertEquals(2, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectWhereString() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users WHERE name = 'Bob'");

        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Bob", result.getResultTable().getRow(0).get(1));
    }

    @Test
    public void testSelectWithBooleanWhere() {
        Database db = new Database("test");
        Table test = db.createTable("test",
            new Column("id", Integer.class),
            new Column("active", Boolean.class)
        );
        test.addRow(1, true);
        test.addRow(2, false);
        test.addRow(3, true);

        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM test WHERE active = true");

        assertEquals(2, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectOrderBy() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users ORDER BY name");

        assertTrue(result.isSuccess());
    }

    @Test
    public void testSelectOrderByDesc() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users ORDER BY id DESC");

        assertTrue(result.isSuccess());
        assertEquals(3, result.getResultTable().getRow(0).get(0));
    }

    @Test
    public void testSelectLimit() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users LIMIT 2");

        assertEquals(2, result.getResultTable().getRowCount());
    }

    @Test
    public void testSelectLimitWithOffset() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM users LIMIT 2 OFFSET 1");

        assertEquals(2, result.getResultTable().getRowCount());
    }

    // ========== UPDATE 测试 ==========

    @Test
    public void testUpdate() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("UPDATE users SET name = 'Ann' WHERE id = 1");

        assertTrue(result.isSuccess());
        assertEquals("Ann", db.getTable("users").getRow(0).get(1));
    }

    @Test
    public void testUpdateMultiple() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("UPDATE users SET name = 'All' WHERE id > 0");

        assertTrue(result.isSuccess());
    }

    @Test
    public void testUpdateNoMatch() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("UPDATE users SET name = 'X' WHERE id = 999");

        assertTrue(result.isSuccess());
    }

    // ========== DELETE 测试 ==========

    @Test
    public void testDelete() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("DELETE FROM users WHERE id = 1");

        assertTrue(result.isSuccess());
        assertEquals(2, db.getTable("users").getRowCount());
    }

    @Test
    public void testDeleteMultiple() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("DELETE FROM users WHERE id > 1");

        assertTrue(result.isSuccess());
        assertEquals(1, db.getTable("users").getRowCount());
    }

    @Test
    public void testDeleteAll() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("DELETE FROM users");

        assertTrue(result.isSuccess());
        assertEquals(0, db.getTable("users").getRowCount());
    }

    // ========== JOIN 测试 ==========

    @Test
    public void testInnerJoin() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");
        users.addRow(2, "Bob");

        Table orders = db.createTable("orders",
            new Column("id", Integer.class),
            new Column("user_id", Integer.class),
            new Column("amount", Double.class)
        );
        orders.addRow(1, 1, 100.0);
        orders.addRow(2, 1, 200.0);
        orders.addRow(3, 2, 150.0);

        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute(
            "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id"
        );

        assertTrue(result.isSuccess());
        assertEquals(3, result.getResultTable().getRowCount());
    }

    @Test
    public void testLeftJoin() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");
        users.addRow(2, "Bob");
        users.addRow(3, "Charlie");

        Table orders = db.createTable("orders",
            new Column("id", Integer.class),
            new Column("user_id", Integer.class)
        );
        orders.addRow(1, 1);
        orders.addRow(2, 1);

        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute(
            "SELECT users.name, orders.id FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );

        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
    }

    // ========== 错误处理测试 ==========

    @Test
    public void testMalformedSql() {
        Database db = createTestDatabase();
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("NOT SQL");

        assertFalse(result.isSuccess());
    }

    @Test
    public void testSelectFromNonExistentTable() {
        Database db = new Database("test");
        SqlExecutor executor = new SqlExecutor(db);
        SqlResult result = executor.execute("SELECT * FROM notexist");

        assertFalse(result.isSuccess());
    }
}
