package com.zifang.util.sql;

import com.zifang.util.sql.executor.SqlExecutor;
import com.zifang.util.sql.executor.SqlResult;

/**
 * SQL内存数据库演示
 */
public class SqlDemo {
    
    public static void main(String[] args) {
        SqlExecutor executor = new SqlExecutor();
        
        // 创建表
        executor.execute("CREATE TABLE users (id INT, name STRING, age INT)");
        System.out.println("Created users table");
        
        // 插入数据
        executor.execute("INSERT INTO users (id, name, age) VALUES (1, 'Alice', 30)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (2, 'Bob', 25)");
        executor.execute("INSERT INTO users (id, name, age) VALUES (3, 'Charlie', 35)");
        System.out.println("Inserted 3 rows");
        
        // 查询所有
        System.out.println("\n=== SELECT * FROM users ===");
        SqlResult result = executor.execute("SELECT * FROM users");
        System.out.println(result);
        
        // 条件查询
        System.out.println("\n=== SELECT name, age FROM users WHERE age > 25 ===");
        result = executor.execute("SELECT name, age FROM users WHERE age > 25");
        System.out.println(result);
        
        // 排序
        System.out.println("\n=== SELECT * FROM users ORDER BY age DESC ===");
        result = executor.execute("SELECT * FROM users ORDER BY age DESC");
        System.out.println(result);
        
        // 限制
        System.out.println("\n=== SELECT * FROM users LIMIT 2 ===");
        result = executor.execute("SELECT * FROM users LIMIT 2");
        System.out.println(result);
        
        // 更新
        System.out.println("\n=== UPDATE users SET age = 40 WHERE id = 1 ===");
        result = executor.execute("UPDATE users SET age = 40 WHERE id = 1");
        System.out.println(result);
        
        // 验证更新
        System.out.println("\n=== SELECT * FROM users WHERE id = 1 ===");
        result = executor.execute("SELECT * FROM users WHERE id = 1");
        System.out.println(result);
        
        // 删除
        System.out.println("\n=== DELETE FROM users WHERE id = 3 ===");
        result = executor.execute("DELETE FROM users WHERE id = 3");
        System.out.println(result);
        
        // 最终数据
        System.out.println("\n=== Final data ===");
        result = executor.execute("SELECT * FROM users");
        System.out.println(result);
        
        // 创建另一个表并JOIN
        executor.execute("CREATE TABLE orders (id INT, user_id INT, amount DOUBLE)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (1, 1, 100.50)");
        executor.execute("INSERT INTO orders (id, user_id, amount) VALUES (2, 2, 200.75)");
        
        System.out.println("\n=== Tables in database ===");
        for (String tableName : executor.getTableNames()) {
            System.out.println("  " + tableName);
        }
        
        // 删除表
        executor.execute("DROP TABLE orders");
        System.out.println("\nDropped orders table");
        
        System.out.println("\n=== Remaining tables ===");
        for (String tableName : executor.getTableNames()) {
            System.out.println("  " + tableName);
        }
    }
}
