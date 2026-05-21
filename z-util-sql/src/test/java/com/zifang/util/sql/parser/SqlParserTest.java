package com.zifang.util.sql.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SqlParser测试
 */
public class SqlParserTest {
    
    private SqlParser parser = new SqlParser();
    
    // ========== SELECT 测试 ==========
    
    @Test
    public void testParseSelectAll() {
        SqlStatement stmt = parser.parse("SELECT * FROM users");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(1, stmt.getSelectColumns().size());
        assertEquals("*", stmt.getSelectColumns().get(0));
    }
    
    @Test
    public void testParseSelectSpecificColumns() {
        SqlStatement stmt = parser.parse("SELECT id, name, age FROM users");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(3, stmt.getSelectColumns().size());
        assertEquals("id", stmt.getSelectColumns().get(0));
        assertEquals("name", stmt.getSelectColumns().get(1));
        assertEquals("age", stmt.getSelectColumns().get(2));
    }
    
    @Test
    public void testParseSelectWhereEqual() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE id = 1");
        
        assertEquals("id", stmt.getWhereColumn());
        assertEquals(1, stmt.getWhereValue());
        assertEquals("=", stmt.getWhereOp());
    }
    
    @Test
    public void testParseSelectWhereGreaterThan() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE age > 25");
        
        assertEquals("age", stmt.getWhereColumn());
        assertEquals(25, stmt.getWhereValue());
        assertEquals(">", stmt.getWhereOp());
    }
    
    @Test
    public void testParseSelectWhereLessThan() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE age < 30");
        
        assertEquals("age", stmt.getWhereColumn());
        assertEquals(30, stmt.getWhereValue());
        assertEquals("<", stmt.getWhereOp());
    }
    
    @Test
    public void testParseSelectWhereNotEqual() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE id != 1");
        
        assertEquals("id", stmt.getWhereColumn());
        assertEquals(1, stmt.getWhereValue());
        assertEquals("!=", stmt.getWhereOp());
    }
    
    @Test
    public void testParseSelectWithStringWhere() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE name = 'Alice'");
        
        assertEquals("name", stmt.getWhereColumn());
        assertEquals("Alice", stmt.getWhereValue());
    }
    
    @Test
    public void testParseSelectOrderByAsc() {
        SqlStatement stmt = parser.parse("SELECT * FROM users ORDER BY age ASC");
        
        assertEquals("age", stmt.getOrderByColumn());
        assertFalse(stmt.isOrderByDesc());
    }
    
    @Test
    public void testParseSelectOrderByDesc() {
        SqlStatement stmt = parser.parse("SELECT * FROM users ORDER BY age DESC");
        
        assertEquals("age", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
    }
    
    @Test
    public void testParseSelectLimit() {
        SqlStatement stmt = parser.parse("SELECT * FROM users LIMIT 10");
        
        assertEquals(10, stmt.getLimit().intValue());
    }
    
    @Test
    public void testParseSelectLimitWithOffset() {
        SqlStatement stmt = parser.parse("SELECT * FROM users LIMIT 10 OFFSET 5");
        
        assertEquals(10, stmt.getLimit().intValue());
        assertEquals(5, stmt.getOffset().intValue());
    }
    
    @Test
    public void testParseSelectComplex() {
        SqlStatement stmt = parser.parse(
            "SELECT id, name, age FROM users WHERE age > 25 ORDER BY name DESC LIMIT 10 OFFSET 0"
        );
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(3, stmt.getSelectColumns().size());
        assertEquals("age", stmt.getWhereColumn());
        assertEquals(25, stmt.getWhereValue());
        assertEquals(">", stmt.getWhereOp());
        assertEquals("name", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
        assertEquals(10, stmt.getLimit().intValue());
        assertEquals(0, stmt.getOffset().intValue());
    }
    
    // ========== JOIN 测试 ==========
    
    @Test
    public void testParseSelectWithJoin() {
        SqlStatement stmt = parser.parse(
            "SELECT * FROM users JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals("orders", stmt.getJoinTable());
        assertEquals("id", stmt.getJoinLeftColumn());
        assertEquals("user_id", stmt.getJoinRightColumn());
    }
    
    @Test
    public void testParseSelectWithJoinAndWhere() {
        SqlStatement stmt = parser.parse(
            "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id WHERE users.age > 25"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals("orders", stmt.getJoinTable());
        assertEquals("age", stmt.getWhereColumn());
        assertEquals(25, stmt.getWhereValue());
    }
    
    // ========== INSERT 测试 ==========
    
    @Test
    public void testParseInsert() {
        SqlStatement stmt = parser.parse(
            "INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)"
        );
        
        assertEquals(SqlStatementType.INSERT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(3, stmt.getColumns().size());
        assertEquals("id", stmt.getColumns().get(0));
        assertEquals("name", stmt.getColumns().get(1));
        assertEquals("age", stmt.getColumns().get(2));
        assertEquals(3, stmt.getValues().size());
    }
    
    @Test
    public void testParseInsertWithDoubleQuotes() {
        SqlStatement stmt = parser.parse(
            "INSERT INTO users (name) VALUES (\"Alice\")"
        );
        
        assertEquals(SqlStatementType.INSERT, stmt.getType());
        assertEquals("Alice", stmt.getValues().get(0));
    }
    
    // ========== UPDATE 测试 ==========
    
    @Test
    public void testParseUpdate() {
        SqlStatement stmt = parser.parse("UPDATE users SET age = 35 WHERE id = 1");
        
        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("age", stmt.getUpdateColumn());
        assertEquals(35, stmt.getUpdateValue());
        assertEquals("id", stmt.getWhereColumn());
        assertEquals(1, stmt.getWhereValue());
    }
    
    @Test
    public void testParseUpdateWithoutWhere() {
        SqlStatement stmt = parser.parse("UPDATE users SET age = 35");
        
        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("age", stmt.getUpdateColumn());
        assertEquals(35, stmt.getUpdateValue());
        assertNull(stmt.getWhereColumn());
    }
    
    // ========== DELETE 测试 ==========
    
    @Test
    public void testParseDeleteWithWhere() {
        SqlStatement stmt = parser.parse("DELETE FROM users WHERE id = 1");
        
        assertEquals(SqlStatementType.DELETE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("id", stmt.getWhereColumn());
        assertEquals(1, stmt.getWhereValue());
    }
    
    @Test
    public void testParseDeleteWithoutWhere() {
        SqlStatement stmt = parser.parse("DELETE FROM users");
        
        assertEquals(SqlStatementType.DELETE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertNull(stmt.getWhereColumn());
    }
    
    // ========== CREATE TABLE 测试 ==========
    
    @Test
    public void testParseCreateTable() {
        SqlStatement stmt = parser.parse(
            "CREATE TABLE users (id INT, name STRING, age INT)"
        );
        
        assertEquals(SqlStatementType.CREATE_TABLE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(3, stmt.getColumnTypes().size());
        assertEquals(Integer.class, stmt.getColumnTypes().get("id"));
        assertEquals(String.class, stmt.getColumnTypes().get("name"));
        assertEquals(Integer.class, stmt.getColumnTypes().get("age"));
    }
    
    @Test
    public void testParseCreateTableWithAllTypes() {
        SqlStatement stmt = parser.parse(
            "CREATE TABLE test (id INT, name STRING, score DOUBLE, active BOOLEAN, amount FLOAT, bigint LONG)"
        );
        
        assertEquals(SqlStatementType.CREATE_TABLE, stmt.getType());
        assertEquals(Integer.class, stmt.getColumnTypes().get("id"));
        assertEquals(String.class, stmt.getColumnTypes().get("name"));
        assertEquals(Double.class, stmt.getColumnTypes().get("score"));
        assertEquals(Boolean.class, stmt.getColumnTypes().get("active"));
        assertEquals(Float.class, stmt.getColumnTypes().get("amount"));
        assertEquals(Long.class, stmt.getColumnTypes().get("bigint"));
    }
    
    // ========== DROP TABLE 测试 ==========
    
    @Test
    public void testParseDropTable() {
        SqlStatement stmt = parser.parse("DROP TABLE users");
        
        assertEquals(SqlStatementType.DROP_TABLE, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }
    
    // ========== 数值类型测试 ==========
    
    @Test
    public void testParseNegativeNumber() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE age > -25");
        
        assertEquals(-25, stmt.getWhereValue());
    }
    
    @Test
    public void testParseDouble() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE score > 3.14");
        
        assertEquals(3.14, stmt.getWhereValue());
    }
    
    @Test
    public void testParseBoolean() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE active = true");
        
        assertEquals(true, stmt.getWhereValue());
    }
    
    @Test
    public void testParseNull() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE name = NULL");
        
        assertNull(stmt.getWhereValue());
    }
    
    // ========== 大小写不敏感测试 ==========
    
    @Test
    public void testParseCaseInsensitive() {
        SqlStatement stmt1 = parser.parse("select * from users");
        SqlStatement stmt2 = parser.parse("SELECT * FROM USERS");
        SqlStatement stmt3 = parser.parse("SeLeCt * FrOm UsErS");
        
        assertEquals(stmt1.getTableName(), stmt2.getTableName());
        assertEquals(stmt1.getTableName(), stmt3.getTableName());
    }
    
    // ========== 未知类型测试 ==========
    
    @Test
    public void testParseUnknown() {
        SqlStatement stmt = parser.parse("INVALID SQL");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
}
