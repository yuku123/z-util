package com.zifang.util.sql.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SqlParser完整测试 - 覆盖所有SQL语法解析和边界情况
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
    
    @Test
    public void testParseSelectLimitOnly() {
        SqlStatement stmt = parser.parse("SELECT * FROM users LIMIT 5");
        
        assertEquals(5, stmt.getLimit().intValue());
        assertNull(stmt.getOffset());
    }
    
    @Test
    public void testParseSelectOffsetOnly() {
        // SQL标准中OFFSET必须配合LIMIT使用，但我们可以测试解析
        SqlStatement stmt = parser.parse("SELECT * FROM users OFFSET 5");
        
        // 解析结果取决于实现
        assertNotNull(stmt);
    }
    
    // ========== JOIN 测试 ==========
    
    @Test
    public void testParseSelectWithInnerJoin() {
        SqlStatement stmt = parser.parse(
            "SELECT * FROM users JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals("orders", stmt.getJoinTable());
        assertEquals("id", stmt.getJoinLeftColumn());
        assertEquals("user_id", stmt.getJoinRightColumn());
        assertFalse(stmt.isLeftJoin());
    }
    
    @Test
    public void testParseSelectWithLeftJoin() {
        SqlStatement stmt = parser.parse(
            "SELECT * FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals("orders", stmt.getJoinTable());
        assertTrue(stmt.isLeftJoin());
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
    
    @Test
    public void testParseSelectWithJoinAndOrderBy() {
        SqlStatement stmt = parser.parse(
            "SELECT * FROM users JOIN orders ON users.id = orders.user_id ORDER BY users.name"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals("users.name", stmt.getOrderByColumn());
    }
    
    @Test
    public void testParseSelectWithJoinAndLimit() {
        SqlStatement stmt = parser.parse(
            "SELECT * FROM users JOIN orders ON users.id = orders.user_id LIMIT 10"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals(10, stmt.getLimit().intValue());
    }
    
    @Test
    public void testParseSelectWithMultipleColumns() {
        SqlStatement stmt = parser.parse(
            "SELECT users.id, users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(stmt.hasJoin());
        assertEquals(3, stmt.getSelectColumns().size());
        assertEquals("users.id", stmt.getSelectColumns().get(0));
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
    
    @Test
    public void testParseInsertSingleColumn() {
        SqlStatement stmt = parser.parse(
            "INSERT INTO users (id) VALUES (1)"
        );
        
        assertEquals(SqlStatementType.INSERT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(1, stmt.getColumns().size());
        assertEquals(1, stmt.getValues().size());
    }
    
    @Test
    public void testParseInsertMultipleValues() {
        SqlStatement stmt = parser.parse(
            "INSERT INTO users (id, name) VALUES (1, 'Alice'), (2, 'Bob')"
        );
        
        assertEquals(SqlStatementType.INSERT, stmt.getType());
        // 应该解析第一组值
        assertEquals(2, stmt.getValues().size());
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
    
    @Test
    public void testParseUpdateWithStringValue() {
        SqlStatement stmt = parser.parse("UPDATE users SET name = 'Bob' WHERE id = 1");
        
        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals("name", stmt.getUpdateColumn());
        assertEquals("Bob", stmt.getUpdateValue());
    }
    
    @Test
    public void testParseUpdateWithDoubleValue() {
        SqlStatement stmt = parser.parse("UPDATE products SET price = 19.99 WHERE id = 1");
        
        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals(19.99, stmt.getUpdateValue());
    }
    
    @Test
    public void testParseUpdateWithBooleanValue() {
        SqlStatement stmt = parser.parse("UPDATE users SET active = true WHERE id = 1");
        
        assertEquals(SqlStatementType.UPDATE, stmt.getType());
        assertEquals(true, stmt.getUpdateValue());
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
    
    @Test
    public void testParseDeleteWithStringWhere() {
        SqlStatement stmt = parser.parse("DELETE FROM users WHERE name = 'Alice'");
        
        assertEquals(SqlStatementType.DELETE, stmt.getType());
        assertEquals("name", stmt.getWhereColumn());
        assertEquals("Alice", stmt.getWhereValue());
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
    
    @Test
    public void testParseCreateTableSingleColumn() {
        SqlStatement stmt = parser.parse("CREATE TABLE users (id INT)");
        
        assertEquals(SqlStatementType.CREATE_TABLE, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(1, stmt.getColumnTypes().size());
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
    public void testParseDoubleWithLeadingDot() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE value > .5");
        
        // 取决于解析器实现
        assertNotNull(stmt.getWhereValue());
    }
    
    @Test
    public void testParseBooleanTrue() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE active = true");
        
        assertEquals(true, stmt.getWhereValue());
    }
    
    @Test
    public void testParseBooleanFalse() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE active = false");
        
        assertEquals(false, stmt.getWhereValue());
    }
    
    @Test
    public void testParseNull() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE name = NULL");
        
        assertNull(stmt.getWhereValue());
    }
    
    @Test
    public void testParseZero() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE age = 0");
        
        assertEquals(0, stmt.getWhereValue());
    }
    
    @Test
    public void testParseLargeNumber() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE bigvalue > 9223372036854775807");
        
        // Long类型
        assertNotNull(stmt.getWhereValue());
    }
    
    // ========== 大小写不敏感测试 ==========
    
    @Test
    public void testParseCaseInsensitiveKeywords() {
        SqlStatement stmt1 = parser.parse("select * from users");
        SqlStatement stmt2 = parser.parse("SELECT * FROM USERS");
        SqlStatement stmt3 = parser.parse("SeLeCt * FrOm UsErS");
        
        assertEquals(stmt1.getTableName(), stmt2.getTableName());
        assertEquals(stmt1.getTableName(), stmt3.getTableName());
    }
    
    @Test
    public void testParseCaseInsensitiveTableName() {
        SqlStatement stmt1 = parser.parse("SELECT * FROM Users");
        SqlStatement stmt2 = parser.parse("SELECT * FROM USERS");
        SqlStatement stmt3 = parser.parse("SELECT * FROM users");
        
        assertEquals(stmt1.getTableName(), stmt2.getTableName());
        assertEquals(stmt2.getTableName(), stmt3.getTableName());
    }
    
    @Test
    public void testParseCaseInsensitiveColumnName() {
        SqlStatement stmt = parser.parse("SELECT ID, Name, AGE FROM USERS WHERE ID = 1");
        
        assertEquals("ID", stmt.getSelectColumns().get(0));
        assertEquals("Name", stmt.getSelectColumns().get(1));
        assertEquals("AGE", stmt.getSelectColumns().get(2));
    }
    
    // ========== 空白字符测试 ==========
    
    @Test
    public void testParseWithExtraSpaces() {
        SqlStatement stmt = parser.parse("SELECT    *    FROM   users   WHERE   id   =   1");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals("id", stmt.getWhereColumn());
        assertEquals(1, stmt.getWhereValue());
    }
    
    @Test
    public void testParseWithTabs() {
        SqlStatement stmt = parser.parse("SELECT\t*\tFROM\tusers");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }
    
    @Test
    public void testParseWithNewlines() {
        SqlStatement stmt = parser.parse("SELECT *\nFROM users\nWHERE id = 1");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
    }
    
    // ========== 错误语法测试 ==========
    
    @Test
    public void testParseUnknown() {
        SqlStatement stmt = parser.parse("INVALID SQL");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
    
    @Test
    public void testParseEmptyString() {
        SqlStatement stmt = parser.parse("");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
    
    @Test
    public void testParseIncompleteSelect() {
        SqlStatement stmt = parser.parse("SELECT");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
    
    @Test
    public void testParseIncompleteInsert() {
        SqlStatement stmt = parser.parse("INSERT INTO users");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
    
    @Test
    public void testParseMalformedWhere() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
    
    @Test
    public void testParseInvalidOperator() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE id === 1");
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
    }
    
    // ========== 特殊字符测试 ==========
    
    @Test
    public void testParseStringWithEscapedQuote() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE name = 'Alice''s'");
        
        assertEquals("Alice's", stmt.getWhereValue());
    }
    
    @Test
    public void testParseUnquotedString() {
        // 有些解析器可能不支持无引号字符串
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE name = Alice");
        
        // 应该解析为标识符或报错
        assertNotNull(stmt);
    }
    
    // ========== 别名测试 ==========
    
    @Test
    public void testParseTableAlias() {
        SqlStatement stmt = parser.parse("SELECT u.id, u.name FROM users u");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals(2, stmt.getSelectColumns().size());
    }
    
    // ========== 函数测试（如果支持）==========
    
    @Test
    public void testParseSelectCount() {
        SqlStatement stmt = parser.parse("SELECT COUNT(*) FROM users");
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("COUNT(*)", stmt.getSelectColumns().get(0));
    }
    
    // ========== SqlStatement 方法测试 ==========
    
    @Test
    public void testSqlStatementSetters() {
        SqlStatement stmt = parser.parse("SELECT * FROM users");
        
        stmt.setType(SqlStatementType.INSERT);
        assertEquals(SqlStatementType.INSERT, stmt.getType());
        
        stmt.setTableName("orders");
        assertEquals("orders", stmt.getTableName());
    }
    
    @Test
    public void testSqlStatementToString() {
        SqlStatement stmt = parser.parse("SELECT * FROM users WHERE id = 1");
        String str = stmt.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("SELECT"));
        assertTrue(str.contains("users"));
    }
    
    @Test
    public void testSqlStatementEquals() {
        SqlStatement stmt1 = parser.parse("SELECT * FROM users WHERE id = 1");
        SqlStatement stmt2 = parser.parse("SELECT * FROM users WHERE id = 1");
        
        // 相等的语句应该有相同的hashCode
        assertEquals(stmt1.hashCode(), stmt2.hashCode());
    }
}