package com.zifang.util.sql;

import com.zifang.util.sql.executor.SqlExecutor;
import com.zifang.util.sql.executor.SqlResult;
import com.zifang.util.sql.model.*;
import com.zifang.util.sql.parser.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 核心功能测试
 */
public class AllTest {

    // ========== Table 测试 ==========

    @Test
    public void testTableCreateAndAddRow() {
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
    public void testTableGetRow() {
        Table table = new Table("users");
        table.addColumn("id", Integer.class);
        table.addRow(1);

        assertNotNull(table.getRow(0));
        assertNull(table.getRow(-1));
        assertNull(table.getRow(100));
    }

    // ========== Database 测试 ==========

    @Test
    public void testDatabaseCreateTable() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));

        assertTrue(db.hasTable("users"));
        assertEquals(1, db.getTableCount());
    }

    @Test
    public void testDatabaseDropTable() {
        Database db = new Database("test");
        db.createTable("users", new Column("id", Integer.class));
        assertTrue(db.dropTable("users"));
        assertFalse(db.hasTable("users"));
    }

    // ========== SqlParser 测试 ==========

    @Test
    public void testParseSelect() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }

    @Test
    public void testParseSelectWithColumns() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT id, name FROM users");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(2, stmt.getSelectColumns().size());
    }

    @Test
    public void testParseSelectWithWhere() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE id = 1");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("id", stmt.getWhereColumn());
        assertEquals("=", stmt.getWhereOp());
    }

    @Test
    public void testParseInsert() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("INSERT INTO users (id, name) VALUES (1, 'Alice')");

        assertEquals(SqlStatementType.INSERT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(2, stmt.getColumns().size());
        assertEquals(2, stmt.getValues().size());
    }

    @Test
    public void testParseUpdate() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("UPDATE users SET name = 'Bob' WHERE id = 1");

        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("name", stmt.getUpdateColumn());
    }

    @Test
    public void testParseDelete() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("DELETE FROM users WHERE id = 1");

        assertEquals(SqlStatementType.DELETE, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }

    // ========== SqlExecutor 测试 ==========

    @Test
    public void testExecutorInsertAndSelect() {
        Database db = new Database("test");
        db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );

        SqlExecutor executor = new SqlExecutor(db);

        SqlResult r1 = executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        assertTrue(r1.isSuccess());

        SqlResult r2 = executor.execute("SELECT * FROM users");
        assertTrue(r2.isSuccess());
        assertEquals(1, r2.getResultTable().getRowCount());
    }

    @Test
    public void testExecutorSelectWithWhere() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");
        users.addRow(2, "Bob");
        users.addRow(3, "Charlie");

        SqlExecutor executor = new SqlExecutor(db);

        SqlResult r = executor.execute("SELECT * FROM users WHERE id = 2");
        assertTrue(r.isSuccess());
        assertEquals(1, r.getResultTable().getRowCount());
        assertEquals("Bob", r.getResultTable().getRow(0).get(1));
    }

    @Test
    public void testExecutorUpdate() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");

        SqlExecutor executor = new SqlExecutor(db);

        SqlResult r = executor.execute("UPDATE users SET name = 'Bob' WHERE id = 1");
        assertTrue(r.isSuccess());

        assertEquals("Bob", users.getRow(0).get(1));
    }

    @Test
    public void testExecutorDelete() {
        Database db = new Database("test");
        Table users = db.createTable("users",
            new Column("id", Integer.class),
            new Column("name", String.class)
        );
        users.addRow(1, "Alice");
        users.addRow(2, "Bob");

        SqlExecutor executor = new SqlExecutor(db);

        SqlResult r = executor.execute("DELETE FROM users WHERE id = 1");
        assertTrue(r.isSuccess());
        assertEquals(1, users.getRowCount());
    }

    @Test
    public void testExecutorInnerJoin() {
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

        SqlResult r = executor.execute(
            "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id"
        );
        assertTrue(r.isSuccess());
        assertEquals(3, r.getResultTable().getRowCount());
    }

    @Test
    public void testExecutorLeftJoin() {
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
        // user_id=2 没有订单
        // user_id=3 没有订单

        SqlExecutor executor = new SqlExecutor(db);

        SqlResult r = executor.execute(
            "SELECT users.name, orders.id FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );
        assertTrue(r.isSuccess());
        // LEFT JOIN: Alice有2条, Bob有0条(所以是NULL), Charlie有0条(所以是NULL)
        // 结果应该是 2 行 (Alice的两条订单)
        assertEquals(2, r.getResultTable().getRowCount());
    }

    // ========== Row 测试 ==========

    @Test
    public void testRowOperations() {
        Row row = new Row();
        row.add(1);
        row.add("Alice");

        assertEquals(2, row.size());
        assertEquals(1, row.get(0));
        assertEquals("Alice", row.get(1));

        row.clear();
        assertTrue(row.isEmpty());
    }

    // ========== SqlStatement 测试 ==========

    @Test
    public void testSqlStatementJoin() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinTable("orders");
        stmt.setLeftJoin(true);

        assertTrue(stmt.hasJoin());
        assertTrue(stmt.isLeftJoin());
    }

    @Test
    public void testSqlStatementClearJoin() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinTable("orders");
        stmt.setLeftJoin(true);

        stmt.clearJoin();

        assertFalse(stmt.hasJoin());
        assertFalse(stmt.isLeftJoin());
        assertNull(stmt.getJoinTable());
    }
}
