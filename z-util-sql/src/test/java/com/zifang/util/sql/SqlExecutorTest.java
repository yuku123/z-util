package com.zifang.util.sql;

import com.zifang.util.sql.executor.SqlExecutor;
import com.zifang.util.sql.executor.SqlResult;
import com.zifang.util.sql.model.Table;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SQL执行器测试
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
    }
    
    @Test
    public void testDropTable() {
        executor.execute("CREATE TABLE users (id INT)");
        assertTrue(executor.hasTable("users"));
        
        SqlResult result = executor.execute("DROP TABLE users");
        assertTrue(result.isSuccess());
        assertFalse(executor.hasTable("users"));
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
    
    // ========== 辅助方法测试 ==========
    
    @Test
    public void testCreateTableWithHelper() {
        Table table = executor.createTable("users", "id:INT", "name:STRING", "age:INT");
        assertNotNull(table);
        assertEquals(3, table.getColumnCount());
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
    
    // ========== 错误处理测试 ==========
    
    @Test
    public void testSelectFromNonExistentTable() {
        SqlResult result = executor.execute("SELECT * FROM nonexistent");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testInsertIntoNonExistentTable() {
        SqlResult result = executor.execute("INSERT INTO nonexistent (id) VALUES (1)");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testUpdateNonExistentTable() {
        SqlResult result = executor.execute("UPDATE nonexistent SET id = 1 WHERE id = 1");
        assertFalse(result.isSuccess());
    }
    
    @Test
    public void testDeleteFromNonExistentTable() {
        SqlResult result = executor.execute("DELETE FROM nonexistent");
        assertFalse(result.isSuccess());
    }
}
