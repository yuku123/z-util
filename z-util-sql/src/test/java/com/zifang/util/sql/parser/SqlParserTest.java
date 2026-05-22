package com.zifang.util.sql.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * SqlParser测试
 */
public class SqlParserTest {

    // ========== SELECT 测试 ==========

    @Test
    public void testParseSimpleSelect() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(1, stmt.getSelectColumns().size());
        assertEquals("*", stmt.getSelectColumns().get(0));
    }

    @Test
    public void testParseSelectWithColumns() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT id, name FROM users");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(2, stmt.getSelectColumns().size());
        assertEquals("id", stmt.getSelectColumns().get(0));
        assertEquals("name", stmt.getSelectColumns().get(1));
    }

    @Test
    public void testParseSelectWithWhereEqual() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE id = 1");

        assertEquals("id", stmt.getWhereColumn());
        assertEquals("=", stmt.getWhereOp());
        assertEquals(1, stmt.getWhereValue());
    }

    @Test
    public void testParseSelectWithWhereGreaterThan() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE age > 18");

        assertEquals("age", stmt.getWhereColumn());
        assertEquals(">", stmt.getWhereOp());
    }

    @Test
    public void testParseSelectWithWhereLessThan() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE age < 65");

        assertEquals("age", stmt.getWhereColumn());
        assertEquals("<", stmt.getWhereOp());
    }

    @Test
    public void testParseSelectWithWhereNotEqual() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE status != 0");

        assertEquals("status", stmt.getWhereColumn());
        assertEquals("!=", stmt.getWhereOp());
    }

    @Test
    public void testParseSelectWithOrderBy() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users ORDER BY name");

        assertEquals("name", stmt.getOrderByColumn());
        assertFalse(stmt.isOrderByDesc());
    }

    @Test
    public void testParseSelectWithOrderByDesc() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users ORDER BY id DESC");

        assertEquals("id", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
    }

    @Test
    public void testParseSelectWithLimit() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users LIMIT 10");

        assertEquals(Integer.valueOf(10), stmt.getLimit());
    }

    @Test
    public void testParseSelectWithLimitAndOffset() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT * FROM users LIMIT 10 OFFSET 20");

        assertEquals(Integer.valueOf(10), stmt.getLimit());
        assertEquals(Integer.valueOf(20), stmt.getOffset());
    }

    @Test
    public void testParseSelectCount() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT COUNT(*) FROM users");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }

    @Test
    public void testParseSelectCaseInsensitive() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("select * from USERS where ID = 1");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("USERS", stmt.getTableName());
    }

    @Test
    public void testParseSelectWithExtraSpaces() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT  *  FROM  users");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }

    // ========== INSERT 测试 ==========

    @Test
    public void testParseInsert() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("INSERT INTO users (id, name) VALUES (1, 'Alice')");

        assertEquals(SqlStatementType.INSERT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(2, stmt.getColumns().size());
        assertEquals("id", stmt.getColumns().get(0));
        assertEquals("name", stmt.getColumns().get(1));
    }

    @Test
    public void testParseInsertWithDoubleQuotes() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("INSERT INTO users (id, name) VALUES (1, \"Alice\")");

        assertEquals(SqlStatementType.INSERT, stmt.getType());
    }

    @Test
    public void testParseInsertWithAllTypes() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("INSERT INTO test (i, s, d, b, l, f) VALUES (1, 'text', 3.14, true, 100, 2.71)");

        assertEquals(SqlStatementType.INSERT, stmt.getType());
    }

    // ========== UPDATE 测试 ==========

    @Test
    public void testParseUpdate() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("UPDATE users SET name = 'Bob' WHERE id = 1");

        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("name", stmt.getUpdateColumn());
        assertEquals("Bob", stmt.getUpdateValue());
    }

    @Test
    public void testParseUpdateWithWhere() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("UPDATE users SET age = 30 WHERE id = 1");

        assertEquals("age", stmt.getUpdateColumn());
        assertEquals(30, stmt.getUpdateValue());
        assertEquals("id", stmt.getWhereColumn());
    }

    // ========== DELETE 测试 ==========

    @Test
    public void testParseDelete() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("DELETE FROM users WHERE id = 1");

        assertEquals(SqlStatementType.DELETE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("id", stmt.getWhereColumn());
    }

    // ========== CREATE TABLE 测试 ==========

    @Test
    public void testParseCreateTable() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("CREATE TABLE users (id INT, name VARCHAR)");

        assertEquals(SqlStatementType.CREATE_TABLE, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }

    // ========== JOIN 测试 ==========

    @Test
    public void testParseSelectWithJoin() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse(
            "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id"
        );

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertTrue(stmt.hasJoin());
        assertEquals("orders", stmt.getJoinTable());
    }

    @Test
    public void testParseSelectWithLeftJoin() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse(
            "SELECT users.name, orders.amount FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertTrue(stmt.hasJoin());
        assertTrue(stmt.isLeftJoin());
    }

    @Test
    public void testParseSelectWithInnerJoin() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse(
            "SELECT * FROM users INNER JOIN orders ON users.id = orders.user_id"
        );

        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertTrue(stmt.hasJoin());
        assertFalse(stmt.isLeftJoin());
    }

    // ========== 错误处理测试 ==========

    @Test
    public void testParseEmptySql() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("");

        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }

    @Test
    public void testParseInvalidSql() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("NOT A VALID SQL");

        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }

    @Test
    public void testParsePartialSelect() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("SELECT");

        assertEquals(SqlStatementType.SELECT, stmt.getType());
    }

    @Test
    public void testParsePartialInsert() {
        SqlParser parser = new SqlParser();
        SqlStatement stmt = parser.parse("INSERT INTO");

        assertEquals(SqlStatementType.INSERT, stmt.getType());
    }
}
