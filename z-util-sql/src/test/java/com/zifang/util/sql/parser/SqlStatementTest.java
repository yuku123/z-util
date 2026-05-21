package com.zifang.util.sql.parser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SqlStatement测试
 */
public class SqlStatementTest {
    
    @Test
    public void testCreateStatement() {
        SqlStatement stmt = new SqlStatement();
        assertNotNull(stmt.getColumns());
        assertNotNull(stmt.getValues());
        assertNotNull(stmt.getSelectColumns());
        assertNotNull(stmt.getColumnTypes());
        assertFalse(stmt.isOrderByDesc());
        assertFalse(stmt.hasJoin());
    }
    
    @Test
    public void testSettersAndGetters() {
        SqlStatement stmt = new SqlStatement();
        
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        stmt.addSelectColumn("id");
        stmt.addSelectColumn("name");
        stmt.setWhereColumn("age");
        stmt.setWhereValue(30);
        stmt.setWhereOp(">");
        stmt.setOrderByColumn("name");
        stmt.setOrderByDesc(true);
        stmt.setLimit(10);
        stmt.setOffset(5);
        
        assertEquals(SqlStatementType.SELECT, stmt.getType());
        assertEquals("users", stmt.getTableName());
        assertEquals(2, stmt.getSelectColumns().size());
        assertEquals("id", stmt.getSelectColumns().get(0));
        assertEquals("name", stmt.getSelectColumns().get(1));
        assertEquals("age", stmt.getWhereColumn());
        assertEquals(30, stmt.getWhereValue());
        assertEquals(">", stmt.getWhereOp());
        assertEquals("name", stmt.getOrderByColumn());
        assertTrue(stmt.isOrderByDesc());
        assertEquals(10, stmt.getLimit().intValue());
        assertEquals(5, stmt.getOffset().intValue());
    }
    
    @Test
    public void testColumnTypes() {
        SqlStatement stmt = new SqlStatement();
        
        stmt.addColumnType("id", Integer.class);
        stmt.addColumnType("name", String.class);
        stmt.addColumnType("salary", Double.class);
        
        assertEquals(3, stmt.getColumnTypes().size());
        assertEquals(Integer.class, stmt.getColumnTypes().get("id"));
        assertEquals(String.class, stmt.getColumnTypes().get("name"));
        assertEquals(Double.class, stmt.getColumnTypes().get("salary"));
    }
    
    @Test
    public void testJoinFields() {
        SqlStatement stmt = new SqlStatement();
        
        stmt.setJoinTable("orders");
        stmt.setJoinLeftColumn("user_id");
        stmt.setJoinRightColumn("id");
        stmt.setJoinOp("=");
        
        assertTrue(stmt.hasJoin());
        assertEquals("orders", stmt.getJoinTable());
        assertEquals("user_id", stmt.getJoinLeftColumn());
        assertEquals("id", stmt.getJoinRightColumn());
        assertEquals("=", stmt.getJoinOp());
    }
    
    @Test
    public void testHasJoinFalse() {
        SqlStatement stmt = new SqlStatement();
        assertFalse(stmt.hasJoin());
    }
    
    @Test
    public void testToString() {
        SqlStatement stmt = new SqlStatement();
        stmt.setType(SqlStatementType.SELECT);
        stmt.setTableName("users");
        stmt.addSelectColumn("*");
        
        String str = stmt.toString();
        assertTrue(str.contains("SELECT"));
        assertTrue(str.contains("users"));
    }
}
