package com.zifang.util.sql.parser;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * SqlStatement测试
 */
public class SqlStatementTest {

    @Test
    public void testCreateStatement() {
        SqlStatement stmt = new SqlStatement();
        assertNotNull(stmt);
        assertNull(stmt.getType());
    }

    @Test
    public void testType() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        assertEquals(SqlStatementType.SELECT, stmt.getType());
    }

    @Test
    public void testTableName() {
        SqlStatement stmt = new SqlStatement();
        stmt.setTableName("users");
        assertEquals("users", stmt.getTableName());
    }

    // ========== 列操作测试 ==========

    @Test
    public void testColumns() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumn("id");
        stmt.addColumn("name");

        List<String> cols = stmt.getColumns();
        assertEquals(2, cols.size());
        assertEquals("id", cols.get(0));
        assertEquals("name", cols.get(1));
    }

    @Test
    public void testSetColumns() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumn("a");
        stmt.setColumns(java.util.Arrays.asList("x", "y", "z"));

        assertEquals(3, stmt.getColumns().size());
    }

    // ========== 值操作测试 ==========

    @Test
    public void testValues() {
        SqlStatement stmt = new SqlStatement();
        stmt.addValue(1);
        stmt.addValue("Alice");

        List<Object> vals = stmt.getValues();
        assertEquals(2, vals.size());
        assertEquals(1, vals.get(0));
        assertEquals("Alice", vals.get(1));
    }

    @Test
    public void testValuesWithAllTypes() {
        SqlStatement stmt = new SqlStatement();
        stmt.addValue(42);
        stmt.addValue("text");
        stmt.addValue(3.14);
        stmt.addValue(true);
        stmt.addValue(100L);
        stmt.addValue(2.71f);

        List<Object> vals = stmt.getValues();
        assertEquals(6, vals.size());
        assertEquals(42, vals.get(0));
        assertEquals("text", vals.get(1));
        assertEquals(3.14, vals.get(2));
        assertEquals(true, vals.get(3));
        assertEquals(100L, vals.get(4));
        assertEquals(2.71f, vals.get(5));
    }

    @Test
    public void testClearColumnsAndValues() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumn("id");
        stmt.addValue(1);

        stmt.clearColumnsAndValues();

        assertEquals(0, stmt.getColumns().size());
        assertEquals(0, stmt.getValues().size());
    }

    // ========== SELECT 列测试 ==========

    @Test
    public void testSelectColumns() {
        SqlStatement stmt = new SqlStatement();
        stmt.addSelectColumn("id");
        stmt.addSelectColumn("name");

        List<String> cols = stmt.getSelectColumns();
        assertEquals(2, cols.size());
        assertEquals("id", cols.get(0));
        assertEquals("name", cols.get(1));
    }

    // ========== WHERE 条件测试 ==========

    @Test
    public void testWhereCondition() {
        SqlStatement stmt = new SqlStatement();
        stmt.setWhereColumn("id");
        stmt.setWhereOp("=");
        stmt.setWhereValue(1);

        assertEquals("id", stmt.getWhereColumn());
        assertEquals("=", stmt.getWhereOp());
        assertEquals(1, stmt.getWhereValue());
    }

    @Test
    public void testWhereWithDifferentOperators() {
        SqlStatement stmt = new SqlStatement();

        stmt.setWhereOp(">");
        assertEquals(">", stmt.getWhereOp());

        stmt.setWhereOp("<");
        assertEquals("<", stmt.getWhereOp());

        stmt.setWhereOp(">=");
        assertEquals(">=", stmt.getWhereOp());

        stmt.setWhereOp("<=");
        assertEquals("<=", stmt.getWhereOp());

        stmt.setWhereOp("!=");
        assertEquals("!=", stmt.getWhereOp());
    }

    // ========== UPDATE 测试 ==========

    @Test
    public void testUpdateSet() {
        SqlStatement stmt = new SqlStatement();
        stmt.setUpdateColumn("name");
        stmt.setUpdateValue("Bob");

        assertEquals("name", stmt.getUpdateColumn());
        assertEquals("Bob", stmt.getUpdateValue());
    }

    // ========== ORDER BY 测试 ==========

    @Test
    public void testOrderBy() {
        SqlStatement stmt = new SqlStatement();
        stmt.setOrderByColumn("name");
        stmt.setOrderByDesc(false);

        assertEquals("name", stmt.getOrderByColumn());
        assertFalse(stmt.isOrderByDesc());
    }

    @Test
    public void testOrderByDesc() {
        SqlStatement stmt = new SqlStatement();
        stmt.setOrderByColumn("id");
        stmt.setOrderByDesc(true);

        assertEquals("id", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
    }

    // ========== LIMIT/OFFSET 测试 ==========

    @Test
    public void testLimit() {
        SqlStatement stmt = new SqlStatement();
        stmt.setLimit(10);

        assertEquals(Integer.valueOf(10), stmt.getLimit());
    }

    @Test
    public void testOffset() {
        SqlStatement stmt = new SqlStatement();
        stmt.setOffset(20);

        assertEquals(Integer.valueOf(20), stmt.getOffset());
    }

    // ========== CREATE TABLE 测试 ==========

    @Test
    public void testColumnTypes() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumnType("id", Integer.class);
        stmt.addColumnType("name", String.class);

        assertEquals(Integer.class, stmt.getColumnTypes().get("id"));
        assertEquals(String.class, stmt.getColumnTypes().get("name"));
    }

    // ========== JOIN 测试 ==========

    @Test
    public void testJoinBasic() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinTable("orders");

        assertTrue(stmt.hasJoin());
        assertFalse(stmt.isLeftJoin());
        assertEquals("orders", stmt.getJoinTable());
    }

    @Test
    public void testLeftJoin() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinTable("orders");
        stmt.setLeftJoin(true);

        assertTrue(stmt.hasJoin());
        assertTrue(stmt.isLeftJoin());
    }

    @Test
    public void testJoinColumns() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinTable("orders");
        stmt.setJoinLeftColumn("id");
        stmt.setJoinRightColumn("user_id");

        assertEquals("orders", stmt.getJoinTable());
        assertEquals("id", stmt.getJoinLeftColumn());
        assertEquals("user_id", stmt.getJoinRightColumn());
    }

    @Test
    public void testJoinOperator() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinOp("=");

        assertEquals("=", stmt.getJoinOp());
    }

    @Test
    public void testClearJoin() {
        SqlStatement stmt = new SqlStatement();
        stmt.setJoinTable("orders");
        stmt.setLeftJoin(true);

        stmt.clearJoin();

        assertFalse(stmt.hasJoin());
        assertFalse(stmt.isLeftJoin());
        assertNull(stmt.getJoinTable());
        assertNull(stmt.getJoinLeftColumn());
        assertNull(stmt.getJoinRightColumn());
    }

    // ========== toString 测试 ==========

    @Test
    public void testToString() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");

        String str = stmt.toString();
        assertTrue(str.contains("SELECT"));
        assertTrue(str.contains("users"));
    }

    @Test
    public void testToStringWithJoin() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        stmt.setJoinTable("orders");
        stmt.setLeftJoin(true);

        String str = stmt.toString();
        assertTrue(str.contains("users"));
        assertTrue(str.contains("orders"));
    }
}
