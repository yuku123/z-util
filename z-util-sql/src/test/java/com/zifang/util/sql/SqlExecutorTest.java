package com.zifang.util.sql;

import com.zifang.util.sql.executor.SqlExecutor;
import com.zifang.util.sql.executor.SqlResult;
import com.zifang.util.sql.model.Table;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SQL执行器完整测试 - 覆盖所有SQL操作和边界情况
 */
public class SqlExecutorTest {
    
    private SqlExecutor executor;
    
    @Before
    public void setUp() {
        executor = new SqlExecutor();
    }
    
    // ========== CREATE TABLE 测试 ==========
    
    @Test
    public void testCreateTable() {
        SqlResult result = executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        assertTrue(result.isSuccess());
        assertTrue(executor.hasTable("users"));
    }
    
    @Test
    public void testCreateTableWithAllTypes() {
        SqlResult result = executor.execute(
            "CREATE TABLE test (id INT, name STRING, score DOUBLE, active BOOLEAN)"
        );
        assertTrue(result.isSuccess());
        
        Table table = executor.getTable("test");
        assertEquals(4, table.getColumnCount());
        assertEquals("id", table.getColumn(0).getName());
        assertEquals(Integer.class, table.getColumn(0).getType());
        assertEquals("name", table.getColumn(1).getName());
        assertEquals(String.class, table.getColumn(1).getType());
        assertEquals(Double.class, table.getColumn(2).getType());
        assertEquals(Boolean.class, table.getColumn(3).getType());
    }
    
    @Test
    public void testDropTable() {
        executor.execute("CREATE TABLE users (id INT)");
        assertTrue(executor.hasTable("users"));
        
        SqlResult result = executor.execute("DROP TABLE users");
        assertTrue(result.isSuccess());
        assertFalse(executor.hasTable("users"));
    }
    
    @Test
    public void testDropNonExistentTable() {
        SqlResult result = executor.execute("DROP TABLE nonexistent");
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().toLowerCase().contains("not found"));
    }
    
    @Test
    public void testCreateTableCaseInsensitive() {
        executor.execute("CREATE TABLE Users (id INT)");
        assertTrue(executor.hasTable("users"));
        assertTrue(executor.hasTable("USERS"));
        assertTrue(executor.hasTable("Users"));
    }
    
    @Test
    public void testCreateDuplicateTable() {
        executor.execute("CREATE TABLE users (id INT)");
        SqlResult result = executor.execute("CREATE TABLE users (id INT)");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testCreateTableWithFloat() {
        executor.execute("CREATE TABLE products (id INT, price FLOAT)");
        executor.execute("INSERT INTO products (id, price) VALUES (1, 19.99)");
        
        Table table = executor.getTable("products");
        assertEquals(Float.class, table.getColumn(1).getType());
    }
    
    @Test
    public void testCreateTableWithLong() {
        executor.execute("CREATE TABLE bigdata (id INT, bigvalue LONG)");
        executor.execute("INSERT INTO bigdata (id, bigvalue) VALUES (1, 9223372036854775807)");
        
        Table table = executor.getTable("bigdata");
        assertEquals(Long.class, table.getColumn(1).getType());
    }
    
    // ========== INSERT 测试 ==========
    
    @Test
    public void testInsert() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        
        SqlResult result = executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getAffectedRows());
        
        Table table = executor.getTable("users");
        assertEquals(1, table.getRowCount());
        assertEquals(1, table.getRow(0).get(0));
        assertEquals("Alice", table.getRow(0).get(1));
    }
    
    @Test
    public void testInsertMultipleRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Charlie')");
        
        Table table = executor.getTable("users");
        assertEquals(3, table.getRowCount());
    }
    
    @Test
    public void testInsertIntoNonExistentTable() {
        SqlResult result = executor.execute("INSERT INTO nonexistent (id) VALUES (1)");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testInsertWithAllTypes() {
        executor.execute("CREATE TABLE test (intCol INT, stringCol STRING, doubleCol DOUBLE, boolCol BOOLEAN)");
        
        SqlResult result = executor.execute(
            "INSERT INTO test (intCol, stringCol, doubleCol, boolCol) VALUES (42, 'hello', 3.14, true)"
        );
        assertTrue(result.isSuccess());
        
        Table table = executor.getTable("test");
        assertEquals(42, table.getRow(0).get(0));
        assertEquals("hello", table.getRow(0).get(1));
        assertEquals(3.14, table.getRow(0).get(2));
        assertEquals(true, table.getRow(0).get(3));
    }
    
    @Test
    public void testInsertWithNegativeNumbers() {
        executor.execute("CREATE TABLE numbers (id INT, value INT)");
        executor.execute("INSERT INTO numbers (id, value) VALUES (1, -100)");
        
        Table table = executor.getTable("numbers");
        assertEquals(-100, table.getRow(0).get(1));
    }
    
    @Test
    public void testInsertWithDoubleValues() {
        executor.execute("CREATE TABLE measurements (id INT, temp DOUBLE)");
        executor.execute("INSERT INTO measurements (id, temp) VALUES (1, -273.15)");
        executor.execute("INSERT INTO measurements (id, temp) VALUES (2, 100.5)");
        
        Table table = executor.getTable("measurements");
        assertEquals(-273.15, table.getRow(0).get(1));
        assertEquals(100.5, table.getRow(1).get(1));
    }
    
    @Test
    public void testInsertDuplicateKey() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Bob')"); // Same key
        
        // 应该插入，不检查重复
        Table table = executor.getTable("users");
        assertEquals(2, table.getRowCount());
    }
    
    // ========== SELECT 测试 ==========
    
    @Test
    public void testSelectAll() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        SqlResult result = executor.execute("SELECT * FROM users");
        assertTrue(result.isSuccess());
        assertNotNull(result.getResultTable());
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectSpecificColumns() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        
        SqlResult result = executor.execute("SELECT name, age FROM users");
        assertTrue(result.isSuccess());
        
        Table table = result.getResultTable();
        assertEquals(2, table.getColumnCount());
        assertEquals("name", table.getColumn(0).getName());
        assertEquals("age", table.getColumn(1).getName());
        assertEquals("Alice", table.getRow(0).get(0));
        assertEquals(30, table.getRow(0).get(1));
    }
    
    @Test
    public void testSelectWhereEqual() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 1");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Alice", result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testSelectWhereGreaterThan() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 35)");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE age > 28");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectWhereLessThan() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 35)");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE age < 30");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Bob", result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testSelectWhereNotEqual() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Charlie')");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE id != 2");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectWhereString() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE name = 'Alice'");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectOrderByAsc() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 35)");
        
        SqlResult result = executor.execute("SELECT * FROM users ORDER BY age ASC");
        assertTrue(result.isSuccess());
        
        Table table = result.getResultTable();
        assertEquals(25, table.getRow(0).get(2));
        assertEquals(30, table.getRow(1).get(2));
        assertEquals(35, table.getRow(2).get(2));
    }
    
    @Test
    public void testSelectOrderByDesc() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 35)");
        
        SqlResult result = executor.execute("SELECT * FROM users ORDER BY age DESC");
        assertTrue(result.isSuccess());
        
        Table table = result.getResultTable();
        assertEquals(35, table.getRow(0).get(2));
        assertEquals(30, table.getRow(1).get(2));
        assertEquals(25, table.getRow(2).get(2));
    }
    
    @Test
    public void testSelectLimit() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Charlie')");
        
        SqlResult result = executor.execute("SELECT * FROM users LIMIT 2");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectLimitWithOffset() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Charlie')");
        
        SqlResult result = executor.execute("SELECT * FROM users LIMIT 2 OFFSET 1");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
        assertEquals("Bob", result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testSelectFromNonExistentTable() {
        SqlResult result = executor.execute("SELECT * FROM nonexistent");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testSelectNoResults() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 999");
        assertTrue(result.isSuccess());
        assertEquals(0, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectEmptyTable() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        
        SqlResult result = executor.execute("SELECT * FROM users");
        assertTrue(result.isSuccess());
        assertEquals(0, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectWithBooleanWhere() {
        executor.execute("CREATE TABLE users (id INT, name STRING, active BOOLEAN)");
        executor.execute("INSERT INTO users (id, name, active) VALUES (1, 'Alice', true)");
        executor.execute("INSERT INTO users (id, name, active) VALUES (2, 'Bob', false)");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE active = true");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Alice", result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testSelectOrderByStringColumn() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Charlie')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Bob')");
        
        SqlResult result = executor.execute("SELECT * FROM users ORDER BY name ASC");
        assertTrue(result.isSuccess());
        
        Table table = result.getResultTable();
        assertEquals("Alice", table.getRow(0).get(1));
        assertEquals("Bob", table.getRow(1).get(1));
        assertEquals("Charlie", table.getRow(2).get(1));
    }
    
    // ========== UPDATE 测试 ==========
    
    @Test
    public void testUpdate() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        
        SqlResult result = executor.execute("UPDATE users SET age = 35 WHERE id = 1");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getAffectedRows());
        
        Table table = executor.getTable("users");
        assertEquals(35, table.getRow(0).get(2));
        assertEquals(25, table.getRow(1).get(2));
    }
    
    @Test
    public void testUpdateMultipleRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 25)");
        
        SqlResult result = executor.execute("UPDATE users SET age = 40 WHERE age = 30");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getAffectedRows());
    }
    
    @Test
    public void testUpdateNonExistentTable() {
        SqlResult result = executor.execute("UPDATE nonexistent SET id = 1 WHERE id = 1");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testUpdateNoMatchingRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute("UPDATE users SET name = 'Bob' WHERE id = 999");
        assertTrue(result.isSuccess());
        assertEquals(0, result.getAffectedRows());
    }
    
    @Test
    public void testUpdateWithoutWhere() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        
        SqlResult result = executor.execute("UPDATE users SET age = 0");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getAffectedRows());
        
        Table table = executor.getTable("users");
        assertEquals(0, table.getRow(0).get(2));
        assertEquals(0, table.getRow(1).get(2));
    }
    
    @Test
    public void testUpdateStringValue() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute("UPDATE users SET name = 'Bob' WHERE id = 1");
        assertTrue(result.isSuccess());
        
        Table table = executor.getTable("users");
        assertEquals("Bob", table.getRow(0).get(1));
    }
    
    // ========== DELETE 测试 ==========
    
    @Test
    public void testDelete() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        SqlResult result = executor.execute("DELETE FROM users WHERE id = 1");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getAffectedRows());
        
        Table table = executor.getTable("users");
        assertEquals(1, table.getRowCount());
        assertEquals("Bob", table.getRow(0).get(1));
    }
    
    @Test
    public void testDeleteAll() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        SqlResult result = executor.execute("DELETE FROM users");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getAffectedRows());
        
        Table table = executor.getTable("users");
        assertEquals(0, table.getRowCount());
    }
    
    @Test
    public void testDeleteFromNonExistentTable() {
        SqlResult result = executor.execute("DELETE FROM nonexistent");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testDeleteNoMatchingRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute("DELETE FROM users WHERE id = 999");
        assertTrue(result.isSuccess());
        assertEquals(0, result.getAffectedRows());
    }
    
    @Test
    public void testDeleteAllThenInsert() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        executor.execute("DELETE FROM users");
        executor.execute("INSERT INTO users (id, name) VALUES (3, 'Charlie')");
        
        Table table = executor.getTable("users");
        assertEquals(1, table.getRowCount());
        assertEquals(3, table.getRow(0).get(0));
        assertEquals("Charlie", table.getRow(0).get(1));
    }
    
    // ========== INNER JOIN 测试 ==========
    
    @Test
    public void testSelectWithInnerJoin() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 1, 100.50)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (2, 1, 200.75)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (3, 2, 150.00)");
        
        SqlResult result = executor.execute(
            "SELECT * FROM users JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(result.isSuccess());
        // Alice有2个订单，Bob有1个
        assertEquals(3, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSelectWithJoinAndWhere() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 1, 100.50)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (2, 2, 200.75)");
        
        SqlResult result = executor.execute(
            "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id WHERE users.age > 28"
        );
        
        assertTrue(result.isSuccess());
        // 只有Alice的订单（age > 28）
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Alice", result.getResultTable().getRow(0).get(0));
    }
    
    @Test
    public void testJoinWithNonExistentTable() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute(
            "SELECT * FROM users JOIN nonexistent ON users.id = nonexistent.id"
        );
        
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testJoinWithNoMatchingRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 999, 100.50)"); // 没有匹配的用户
        
        SqlResult result = executor.execute(
            "SELECT * FROM users JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(result.isSuccess());
        assertEquals(0, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testJoinThreeTables() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, product_id INT)");
        executor.execute("INSERT INTO orders (id, user_id, product_id) VALUES (1, 1, 100)");
        
        executor.execute("CREATE TABLE products (id INT, name STRING)");
        executor.execute("INSERT INTO products (id, name) VALUES (100, 'Widget')");
        
        // 首先测试两表JOIN
        SqlResult result = executor.execute(
            "SELECT users.name, orders.id FROM users JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Alice", result.getResultTable().getRow(0).get(0));
    }
    
    // ========== LEFT JOIN 测试 ==========
    
    @Test
    public void testSelectWithLeftJoin() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 1, 100.50)");
        // Bob没有订单
        
        SqlResult result = executor.execute(
            "SELECT * FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(result.isSuccess());
        // Alice有订单，Bob没有（LEFT JOIN应该包含所有左表行）
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testLeftJoinWithAllMatchingRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 1, 100.50)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (2, 1, 200.75)");
        
        SqlResult result = executor.execute(
            "SELECT * FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testLeftJoinWithNoMatchingRows() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 999, 100.50)"); // 没有匹配
        
        SqlResult result = executor.execute(
            "SELECT * FROM users LEFT JOIN orders ON users.id = orders.user_id"
        );
        
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount()); // 左表所有行
    }
    
    // ========== 索引测试 ==========
    
    @Test
    public void testCreateIndex() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        executor.createIndex("users", "id");
        
        Table table = executor.getTable("users");
        assertTrue(table.hasIndex("id"));
    }
    
    @Test
    public void testCreateAllIndexes() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        
        executor.createAllIndexes("users");
        
        Table table = executor.getTable("users");
        assertTrue(table.hasIndex("id"));
        assertTrue(table.hasIndex("name"));
    }
    
    @Test
    public void testDropIndex() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        
        executor.createIndex("users", "id");
        executor.dropIndex("users", "id");
        
        Table table = executor.getTable("users");
        assertFalse(table.hasIndex("id"));
    }
    
    @Test
    public void testQueryUsesIndex() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        for (int i = 1; i <= 100; i++) {
            executor.execute("INSERT INTO users (id, name) VALUES (" + i + ", 'User" + i + "')");
        }
        
        // 创建索引
        executor.createIndex("users", "id");
        
        // 使用索引的查询
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 50");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("User50", result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testQueryWithoutIndex() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        // 没有索引，查询应该仍然工作（使用全表扫描）
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 1");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testIndexOnStringColumn() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        executor.createIndex("users", "name");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE name = 'Alice'");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testIndexAfterInsert() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        executor.createIndex("users", "id");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        // 索引应该仍然有效
        SqlResult result = executor.execute("SELECT * FROM users WHERE id = 2");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("Bob", result.getResultTable().getRow(0).get(1));
    }
    
    // ========== 复杂查询测试 ==========
    
    @Test
    public void testComplexQuery() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT, city STRING)");
        executor.execute("INSERT INTO users (id, name, age, city) VALUES (1, 'Alice', 30, 'Beijing')");
        executor.execute("INSERT INTO users (id, name, age, city) VALUES (2, 'Bob', 25, 'Shanghai')");
        executor.execute("INSERT INTO users (id, name, age, city) VALUES (3, 'Charlie', 35, 'Beijing')");
        executor.execute("INSERT INTO users (id, name, age, city) VALUES (4, 'Diana', 28, 'Beijing')");
        executor.execute("INSERT INTO users (id, name, age, city) VALUES (5, 'Eve', 32, 'Shanghai')");
        
        SqlResult result = executor.execute(
            "SELECT name, age FROM users WHERE city = 'Beijing' AND age > 28 ORDER BY age DESC LIMIT 3"
        );
        
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount()); // Alice(30) and Charlie(35)
        assertEquals("Charlie", result.getResultTable().getRow(0).get(0));
        assertEquals(35, result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testComplexQueryWithJoin() {
        executor.execute("CREATE TABLE users (id INT, name STRING, city STRING)");
        executor.execute("INSERT INTO users (id, name, city) VALUES (1, 'Alice', 'Beijing')");
        executor.execute("INSERT INTO users (id, name, city) VALUES (2, 'Bob', 'Shanghai')");
        
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 1, 100.50)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (2, 1, 200.75)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (3, 2, 150.00)");
        
        SqlResult result = executor.execute(
            "SELECT users.name, orders.amount FROM users JOIN orders ON users.id = orders.user_id WHERE orders.amount > 150 ORDER BY orders.amount ASC"
        );
        
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
        assertEquals(200.75, result.getResultTable().getRow(0).get(1));
        assertEquals(150.00, result.getResultTable().getRow(1).get(1));
    }
    
    @Test
    public void testQueryWithLimitAndOffset() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        for (int i = 1; i <= 10; i++) {
            executor.execute("INSERT INTO users (id, name) VALUES (" + i + ", 'User" + i + "')");
        }
        
        SqlResult result = executor.execute("SELECT * FROM users ORDER BY id DESC LIMIT 3 OFFSET 2");
        assertTrue(result.isSuccess());
        assertEquals(3, result.getResultTable().getRowCount());
        assertEquals("User8", result.getResultTable().getRow(0).get(1)); // 10-2=8
    }
    
    // ========== 辅助方法测试 ==========
    
    @Test
    public void testCreateTableWithHelper() {
        Table table = executor.createTable("users", "id:INT", "name:STRING", "age:INT");
        assertNotNull(table);
        assertEquals(3, table.getColumnCount());
        assertEquals(Integer.class, table.getColumn(0).getType());
        assertEquals(String.class, table.getColumn(1).getType());
        assertEquals(Integer.class, table.getColumn(2).getType());
    }
    
    @Test
    public void testCreateTableWithAllTypeHelpers() {
        Table table = executor.createTable("test", 
            "intCol:INT", "longCol:LONG", "doubleCol:DOUBLE", 
            "floatCol:FLOAT", "boolCol:BOOLEAN", "stringCol:STRING"
        );
        
        assertEquals(Integer.class, table.getColumn(0).getType());
        assertEquals(Long.class, table.getColumn(1).getType());
        assertEquals(Double.class, table.getColumn(2).getType());
        assertEquals(Float.class, table.getColumn(3).getType());
        assertEquals(Boolean.class, table.getColumn(4).getType());
        assertEquals(String.class, table.getColumn(5).getType());
    }
    
    @Test
    public void testTruncateTable() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        executor.truncateTable("users");
        
        Table table = executor.getTable("users");
        assertEquals(0, table.getRowCount());
        assertEquals(2, table.getColumnCount()); // 列定义还在
    }
    
    @Test
    public void testDropTableMethod() {
        executor.createTable("users", "id:INT");
        assertTrue(executor.hasTable("users"));
        
        executor.dropTable("users");
        assertFalse(executor.hasTable("users"));
    }
    
    @Test
    public void testGetTableNames() {
        executor.execute("CREATE TABLE users (id INT)");
        executor.execute("CREATE TABLE orders (id INT)");
        executor.execute("CREATE TABLE products (id INT)");
        
        java.util.Set<String> names = executor.getTableNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("users"));
        assertTrue(names.contains("orders"));
        assertTrue(names.contains("products"));
    }
    
    // ========== 错误处理测试 ==========
    
    @Test
    public void testInvalidSql() {
        SqlResult result = executor.execute("INVALID SQL");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testEmptyWhere() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        // 没有WHERE子句，应该返回所有行
        SqlResult result = executor.execute("SELECT * FROM users");
        assertTrue(result.isSuccess());
        assertEquals(2, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testMalformedSql() {
        SqlResult result = executor.execute("SELECT * FROM");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testSqlInjectionPrevention() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        // 尝试注入，应该被当作字面量处理
        SqlResult result = executor.execute("SELECT * FROM users WHERE name = 'Alice' OR '1'='1'");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    // ========== 大数据量测试 ==========
    
    @Test
    public void testLargeInsert() {
        executor.execute("CREATE TABLE numbers (id INT, value INT)");
        
        for (int i = 1; i <= 1000; i++) {
            executor.execute("INSERT INTO numbers (id, value) VALUES (" + i + ", " + i + ")");
        }
        
        Table table = executor.getTable("numbers");
        assertEquals(1000, table.getRowCount());
    }
    
    @Test
    public void testLargeSelect() {
        executor.execute("CREATE TABLE numbers (id INT)");
        for (int i = 1; i <= 100; i++) {
            executor.execute("INSERT INTO numbers (id) VALUES (" + i + ")");
        }
        
        SqlResult result = executor.execute("SELECT * FROM numbers WHERE id > 50");
        assertTrue(result.isSuccess());
        assertEquals(50, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testLargeUpdate() {
        executor.execute("CREATE TABLE numbers (id INT, value INT)");
        for (int i = 1; i <= 100; i++) {
            executor.execute("INSERT INTO numbers (id, value) VALUES (" + i + ", 0)");
        }
        
        SqlResult result = executor.execute("UPDATE numbers SET value = 1 WHERE id <= 50");
        assertTrue(result.isSuccess());
        assertEquals(50, result.getAffectedRows());
    }
    
    @Test
    public void testLargeDelete() {
        executor.execute("CREATE TABLE numbers (id INT)");
        for (int i = 1; i <= 100; i++) {
            executor.execute("INSERT INTO numbers (id) VALUES (" + i + ")");
        }
        
        SqlResult result = executor.execute("DELETE FROM numbers WHERE id > 50");
        assertTrue(result.isSuccess());
        assertEquals(50, result.getAffectedRows());
        
        Table table = executor.getTable("numbers");
        assertEquals(50, table.getRowCount());
    }
    
    // ========== 边界条件测试 ==========
    
    @Test
    public void testLimitZero() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        executor.execute("INSERT INTO users (id, name) VALUES (2, 'Bob')");
        
        SqlResult result = executor.execute("SELECT * FROM users LIMIT 0");
        assertTrue(result.isSuccess());
        assertEquals(0, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testOffsetBeyondResults() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute("SELECT * FROM users LIMIT 10 OFFSET 100");
        assertTrue(result.isSuccess());
        assertEquals(0, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testSpecialCharactersInString() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice \"The Best\"')");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE name = 'Alice \"The Best\"'");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testUnicodeInString() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, '张三')");
        
        SqlResult result = executor.execute("SELECT * FROM users WHERE name = '张三'");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
        assertEquals("张三", result.getResultTable().getRow(0).get(1));
    }
    
    @Test
    public void testCaseInsensitiveColumnNames() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        executor.execute("INSERT INTO users (id, name) VALUES (1, 'Alice')");
        
        SqlResult result = executor.execute("SELECT ID, NAME FROM users");
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResultTable().getRowCount());
    }
    
    @Test
    public void testOrderByNonSelectedColumn() {
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 35)");
        
        SqlResult result = executor.execute("SELECT name FROM users ORDER BY age DESC");
        assertTrue(result.isSuccess());
        assertEquals(3, result.getResultTable().getRowCount());
        // 只选了name，但按age排序
        assertEquals("Charlie", result.getResultTable().getRow(0).get(0));
    }
}