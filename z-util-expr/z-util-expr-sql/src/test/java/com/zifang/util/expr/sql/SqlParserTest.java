package com.zifang.util.expr.sql;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SQL 解析器测试
 */
public class SqlParserTest {

    private SqlParser parser;

    @Before
    public void setUp() {
        parser = new SqlParser();
    }

    @Test
    public void testParseSelect() {
        String sql = "SELECT id, name, age FROM users WHERE status = 1";
        SqlStatement statement = parser.parse(sql);

        assertEquals(SqlStatement.Type.SELECT, statement.getType());
        assertEquals("users", statement.getTableName());
        assertEquals(3, statement.getColumns().size());
        assertTrue(statement.getColumns().contains("id"));
        assertTrue(statement.getColumns().contains("name"));
        assertTrue(statement.getColumns().contains("age"));
        assertEquals(1, statement.getWhereConditions().size());

        SqlStatement.WhereCondition condition = statement.getWhereConditions().get(0);
        assertEquals("status", condition.getColumn());
        assertEquals("=", condition.getOperator());
        assertEquals("1", condition.getValue());
    }

    @Test
    public void testParseInsert() {
        String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";
        SqlStatement statement = parser.parse(sql);

        assertEquals(SqlStatement.Type.INSERT, statement.getType());
        assertEquals("users", statement.getTableName());
        assertEquals(3, statement.getColumns().size());
        assertTrue(statement.getColumns().contains("name"));
        assertTrue(statement.getColumns().contains("age"));
        assertTrue(statement.getColumns().contains("email"));
        assertEquals(3, statement.getPlaceholders().size());
    }

    @Test
    public void testParseUpdate() {
        String sql = "UPDATE users SET name = 'zhangsan', age = 25 WHERE id = :id";
        SqlStatement statement = parser.parse(sql);

        assertEquals(SqlStatement.Type.UPDATE, statement.getType());
        assertEquals("users", statement.getTableName());
        assertEquals(2, statement.getColumns().size());
        assertTrue(statement.getColumns().contains("name"));
        assertTrue(statement.getColumns().contains("age"));
        assertEquals(1, statement.getWhereConditions().size());
        assertEquals(1, statement.getNamedPlaceholders().size());
        assertEquals("id", statement.getNamedPlaceholders().get(0).getName());
    }

    @Test
    public void testParseDelete() {
        String sql = "DELETE FROM users WHERE id = ?";
        SqlStatement statement = parser.parse(sql);

        assertEquals(SqlStatement.Type.DELETE, statement.getType());
        assertEquals("users", statement.getTableName());
        assertEquals(0, statement.getColumns().size());
        assertEquals(1, statement.getWhereConditions().size());
        assertEquals(1, statement.getPlaceholders().size());
    }

    @Test
    public void testPlaceholders() {
        String sql = "SELECT * FROM users WHERE name = :name AND age > :age AND status = ?";
        SqlStatement statement = parser.parse(sql);

        assertEquals(1, statement.getPlaceholders().size());
        assertEquals(2, statement.getNamedPlaceholders().size());
        assertEquals("name", statement.getNamedPlaceholders().get(0).getName());
        assertEquals("age", statement.getNamedPlaceholders().get(1).getName());
    }

    @Test
    public void testWhereConditions() {
        String sql = "SELECT * FROM users WHERE name = 'zhangsan' AND age >= 18 OR status IN (1, 2, 3) AND score < 100";
        SqlStatement statement = parser.parse(sql);

        assertEquals(4, statement.getWhereConditions().size());

        // name = 'zhangsan' (quotes stripped by parser)
        SqlStatement.WhereCondition c1 = statement.getWhereConditions().get(0);
        assertEquals("name", c1.getColumn());
        assertEquals("=", c1.getOperator());
        assertEquals("zhangsan", c1.getValue());
        assertEquals(SqlStatement.WhereCondition.LogicalOperator.NONE, c1.getLogicalOperator());

        // age >= 18
        SqlStatement.WhereCondition c2 = statement.getWhereConditions().get(1);
        assertEquals("age", c2.getColumn());
        assertEquals(">=", c2.getOperator());
        assertEquals("18", c2.getValue());
        assertEquals(SqlStatement.WhereCondition.LogicalOperator.AND, c2.getLogicalOperator());

        // status IN (1, 2, 3)
        SqlStatement.WhereCondition c3 = statement.getWhereConditions().get(2);
        assertEquals("status", c3.getColumn());
        assertEquals("IN", c3.getOperator());
        assertEquals(SqlStatement.WhereCondition.LogicalOperator.OR, c3.getLogicalOperator());

        // score < 100
        SqlStatement.WhereCondition c4 = statement.getWhereConditions().get(3);
        assertEquals("score", c4.getColumn());
        assertEquals("<", c4.getOperator());
        assertEquals("100", c4.getValue());
        assertEquals(SqlStatement.WhereCondition.LogicalOperator.AND, c4.getLogicalOperator());
    }
}
