package com.zifang.util.sql.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SqlStatement模型完整测试
 */
public class SqlStatementTest {
    
    @Test
    public void testCreateStatement() {
        SqlStatement stmt = new SqlStatement();
        assertEquals(SqlStatementType.UNKNOWN, stmt.getType());
        assertNull(stmt.getTableName());
    }
    
    @Test
    public void testSetAndGetType() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
    }
    
    @Test
    public void testSetAndGetTableName() {
        SqlStatement stmt = new SqlStatement();
        stmt.setTableName("users");
        
        assertEquals("users", stmt.getTableName());
    }
    
    @Test
    public void testSelectColumns() {
        SqlStatement stmt = new SqlStatement();
        stmt.addSelectColumn("id");
        stmt.addSelectColumn("name");
        
        assertEquals(2, stmt.getSelectColumns().size());
        assertEquals("id", stmt.getSelectColumns().get(0));
        assertEquals("name", stmt.getSelectColumns().get(1));
    }
    
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
    public void testWhereWithStringValue() {
        SqlStatement stmt = new SqlStatement();
        stmt.setWhereColumn("name");
        stmt.setWhereOp("=");
        stmt.setWhereValue("Alice");
        
        assertEquals("Alice", stmt.getWhereValue());
    }
    
    @Test
    public void testWhereWithDoubleValue() {
        SqlStatement stmt = new SqlStatement();
        stmt.setWhereColumn("price");
        stmt.setWhereOp(">");
        stmt.setWhereValue(99.99);
        
        assertEquals(99.99, stmt.getWhereValue());
    }
    
    @Test
    public void testWhereWithBooleanValue() {
        SqlStatement stmt = new SqlStatement();
        stmt.setWhereColumn("active");
        stmt.setWhereOp("=");
        stmt.setWhereValue(true);
        
        assertEquals(true, stmt.getWhereValue());
    }
    
    @Test
    public void testWhereWithNullValue() {
        SqlStatement stmt = new SqlStatement();
        stmt.setWhereColumn("name");
        stmt.setWhereOp("=");
        stmt.setWhereValue(null);
        
        assertNull(stmt.getWhereValue());
    }
    
    @Test
    public void testOrderBy() {
        SqlStatement stmt = new SqlStatement();
        stmt.setOrderByColumn("age");
        stmt.setOrderByDesc(false);
        
        assertEquals("age", stmt.getOrderByColumn());
        assertFalse(stmt.isOrderByDesc());
    }
    
    @Test
    public void testOrderByDesc() {
        SqlStatement stmt = new SqlStatement();
        stmt.setOrderByColumn("name");
        stmt.setOrderByDesc(true);
        
        assertEquals("name", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
    }
    
    @Test
    public void testLimitAndOffset() {
        SqlStatement stmt = new SqlStatement();
        stmt.setLimit(10);
        stmt.setOffset(5);
        
        assertEquals(10, stmt.getLimit().intValue());
        assertEquals(5, stmt.getOffset().intValue());
    }
    
    @Test
    public void testLimitOnly() {
        SqlStatement stmt = new SqlStatement();
        stmt.setLimit(10);
        
        assertEquals(10, stmt.getLimit().intValue());
        assertNull(stmt.getOffset());
    }
    
    @Test
    public void testOffsetOnly() {
        SqlStatement stmt = new SqlStatement();
        stmt.setOffset(5);
        
        assertEquals(5, stmt.getOffset().intValue());
        assertNull(stmt.getLimit());
    }
    
    // ========== JOIN 测试 ==========
    
    @Test
    public void testJoinFlags() {
        SqlStatement stmt = new SqlStatement();
        
        assertFalse(stmt.hasJoin());
        assertFalse(stmt.isLeftJoin());
        
        stmt.setJoinTable("orders");
        
        assertTrue(stmt.hasJoin());
        assertFalse(stmt.isLeftJoin());
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
    
    // ========== INSERT 测试 ==========
    
    @Test
    public void testInsertColumns() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumn("id");
        stmt.addColumn("name");
        
        assertEquals(2, stmt.getColumns().size());
        assertEquals("id", stmt.getColumns().get(0));
        assertEquals("name", stmt.getColumns().get(1));
    }
    
    @Test
    public void testInsertValues() {
        SqlStatement stmt = new SqlStatement();
        stmt.addValue(1);
        stmt.addValue("Alice");
        
        assertEquals(2, stmt.getValues().size());
        assertEquals(1, stmt.getValues().get(0));
        assertEquals("Alice", stmt.getValues().get(1));
    }
    
    @Test
    public void testInsertValuesWithAllTypes() {
        SqlStatement stmt = new SqlStatement();
        stmt.addValue(1);
        stmt.addValue("Alice");
        stmt.addValue(3.14);
        stmt.addValue(true);
        stmt.addValue(100L);
        stmt.addValue(2.71f);
        
        assertEquals(6, stmt.getValues().size());
        assertEquals(1, stmt.getValues().get(0));
        assertEquals("Alice", stmt.getValues().get(1));
        assertEquals(3.14, stmt.getValues().get(2));
        assertEquals(true, stmt.getValues().get(3));
        assertEquals(100L, stmt.getValues().get(4));
        assertEquals(2.71f, stmt.getValues().get(5));
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
    
    // ========== UPDATE 测试 ==========
    
    @Test
    public void testUpdateSet() {
        SqlStatement stmt = new SqlStatement();
        stmt.setUpdateColumn("age");
        stmt.setUpdateValue(30);
        
        assertEquals("age", stmt.getUpdateColumn());
        assertEquals(30, stmt.getUpdateValue());
    }
    
    @Test
    public void testUpdateWithStringValue() {
        SqlStatement stmt = new SqlStatement();
        stmt.setUpdateColumn("name");
        stmt.setUpdateValue("Bob");
        
        assertEquals("Bob", stmt.getUpdateValue());
    }
    
    // ========== CREATE TABLE 测试 ==========
    
    @Test
    public void testColumnTypes() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumnType("id", Integer.class);
        stmt.addColumnType("name", String.class);
        stmt.addColumnType("score", Double.class);
        
        assertEquals(Integer.class, stmt.getColumnTypes().get("id"));
        assertEquals(String.class, stmt.getColumnTypes().get("name"));
        assertEquals(Double.class, stmt.getColumnTypes().get("score"));
        assertEquals(3, stmt.getColumnTypes().size());
    }
    
    @Test
    public void testColumnTypesWithAllTypes() {
        SqlStatement stmt = new SqlStatement();
        stmt.addColumnType("intCol", Integer.class);
        stmt.addColumnType("longCol", Long.class);
        stmt.addColumnType("doubleCol", Double.class);
        stmt.addColumnType("floatCol", Float.class);
        stmt.addColumnType("boolCol", Boolean.class);
        stmt.addColumnType("stringCol", String.class);
        
        assertEquals(6, stmt.getColumnTypes().size());
        assertEquals(Integer.class, stmt.getColumnTypes().get("intCol"));
        assertEquals(Long.class, stmt.getColumnTypes().get("longCol"));
        assertEquals(Double.class, stmt.getColumnTypes().get("doubleCol"));
        assertEquals(Float.class, stmt.getColumnTypes().get("floatCol"));
        assertEquals(Boolean.class, stmt.getColumnTypes().get("boolCol"));
        assertEquals(String.class, stmt.getColumnTypes().get("stringCol"));
    }
    
    // ========== ToString 测试 ==========
    
    @Test
    public void testToString() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        
        String str = stmt.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("SELECT"));
        assertTrue(str.contains("users"));
    }
    
    @Test
    public void testToStringWithJoin() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        stmt.setJoinTable("orders");
        stmt.setJoinLeftColumn("id");
        stmt.setJoinRightColumn("user_id");
        
        String str = stmt.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("JOIN"));
    }
    
    // ========== Equals和HashCode测试 ==========
    
    @Test
    public void testEquals() {
        SqlStatement stmt1 = new SqlStatement();
        stmt1.setType(SqlStatementType.SELECT);
        stmt1.setTableName("users");
        
        SqlStatement stmt2 = new SqlStatement();
        stmt2.setType(SqlStatementType.SELECT);
        stmt2.setTableName("users");
        
        assertEquals(stmt1, stmt2);
        assertEquals(stmt1.hashCode(), stmt2.hashCode());
    }
    
    @Test
    public void testNotEqualsDifferentType() {
        SqlStatement stmt1 = new SqlStatement();
        stmt1.setType(SqlStatementType.SELECT);
        
        SqlStatement stmt2 = new SqlStatement();
        stmt2.setType(SqlStatementType.INSERT);
        
        assertNotEquals(stmt1, stmt2);
    }
    
    @Test
    public void testNotEqualsDifferentTable() {
        SqlStatement stmt1 = new SqlStatement();
        stmt1.setTableName("users");
        
        SqlStatement stmt2 = new SqlStatement();
        stmt2.setTableName("orders");
        
        assertNotEquals(stmt1, stmt2);
    }
    
    @Test
    public void testNotEqualsNull() {
        SqlStatement stmt = new SqlStatement();
        
        assertNotEquals(stmt, null);
    }
    
    @Test
    public void testNotEqualsDifferentClass() {
        SqlStatement stmt = new SqlStatement();
        
        assertNotEquals(stmt, "not a statement");
    }
    
    // ========== Builder模式测试 ==========
    
    @Test
    public void testFullSelectStatement() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        stmt.addSelectColumn("*");
        stmt.setWhereColumn("age");
        stmt.setWhereOp(">");
        stmt.setWhereValue(25);
        stmt.setOrderByColumn("name");
        stmt.setOrderByDesc(true);
        stmt.setLimit(10);
        stmt.setOffset(0);
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(1, stmt.getSelectColumns().size());
        assertEquals("*", stmt.getSelectColumns().get(0));
        assertEquals("age", stmt.getWhereColumn());
        assertEquals(">", stmt.getWhereOp());
        assertEquals(25, stmt.getWhereValue());
        assertEquals("name", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
        assertEquals(10, stmt.getLimit().intValue());
        assertEquals(0, stmt.getOffset().intValue());
    }
    
    @Test
    public void testFullJoinStatement() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        stmt.addSelectColumn("users.name");
        stmt.addSelectColumn("orders.amount");
        stmt.setJoinTable("orders");
        stmt.setJoinLeftColumn("users.id");
        stmt.setJoinRightColumn("orders.user_id");
        stmt.setLeftJoin(true);
        stmt.setWhereColumn("orders.amount");
        stmt.setWhereOp(">");
        stmt.setWhereValue(100);
        stmt.setOrderByColumn("orders.amount");
        stmt.setOrderByDesc(false);
        stmt.setLimit(50);
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertTrue(stmt.hasJoin());
        assertTrue(stmt.isLeftJoin());
        assertEquals("orders", stmt.getJoinTable());
        assertEquals("users.id", stmt.getJoinLeftColumn());
        assertEquals("orders.user_id", stmt.getJoinRightColumn());
        assertEquals(2, stmt.getSelectColumns().size());
        assertEquals(50, stmt.getLimit().intValue());
    }
}