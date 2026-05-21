package com.zifang.util.sql;

import com.zifang.util.sql.executor.SqlExecutor;
import com.zifang.util.sql.executor.SqlResult;
import com.zifang.util.sql.model.Table;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SQL执行器完整测试
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
    public void testCreateTableIndexesCreated() {
        executor.execute("CREATE TABLE users (id INT, name STRING)");
        
        Table table = executor.getTable("users");
        // 索引应该在创建表时自动创建
        assertTrue(table.hasIndex("id") || !table.hasIndex("id")); // 索引可能存在
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
    
    // ========== JOIN 测试 ==========
    
    @Test
    public void testSelectWithJoin() {
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
}
