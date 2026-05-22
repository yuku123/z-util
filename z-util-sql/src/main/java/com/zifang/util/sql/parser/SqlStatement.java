package com.zifang.util.sql.parser;

import java.util.*;

/**
 * SQL语句解析结果
 */
public class SqlStatement {
    
    private SqlStatementType type;
    private String tableName;
    private List<String> columns;  // SELECT columns or INSERT/CREATE columns
    private List<Object> values;   // INSERT values
    private List<String> selectColumns; // for SELECT
    private String whereColumn;     // for WHERE column op value
    private Object whereValue;
    private String whereOp;         // WHERE 操作符: =, !=, >, <, >=, <=
    private String updateColumn;    // for UPDATE column = value
    private Object updateValue;
    private String orderByColumn;  // ORDER BY column
    private boolean orderByDesc;
    private Integer limit;
    private Integer offset;
    
    // For CREATE TABLE
    private Map<String, Class<?>> columnTypes;
    
    // For SELECT ... FROM table1 JOIN table2 ON table1.col = table2.col
    private String joinTable;
    private String joinLeftColumn;  // e.g., "user_id" from "user_id = orders.user_id"
    private String joinRightColumn; // the right side of join condition
    private String joinOp;          // join operator, default "="
    private boolean leftJoin;       // true for LEFT JOIN, false for INNER JOIN
    
    public SqlStatement() {
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
        this.selectColumns = new ArrayList<>();
        this.columnTypes = new LinkedHashMap<>();
        this.orderByDesc = false;
        this.whereOp = "=";
        this.joinOp = "=";
    }
    
    public SqlStatementType getType() {
        return type;
    }
    
    public void setType(SqlStatementType type) {
        this.type = type;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public List<String> getColumns() {
        return columns;
    }
    
    public void addColumn(String column) {
        this.columns.add(column);
    }
    
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
    
    public List<Object> getValues() {
        return values;
    }
    
    public void addValue(Object value) {
        this.values.add(value);
    }
    
    public void setValues(List<Object> values) {
        this.values = values;
    }
    
    public List<String> getSelectColumns() {
        return selectColumns;
    }
    
    public void addSelectColumn(String column) {
        this.selectColumns.add(column);
    }
    
    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }
    
    public String getWhereColumn() {
        return whereColumn;
    }
    
    public void setWhereColumn(String whereColumn) {
        this.whereColumn = whereColumn;
    }
    
    public Object getWhereValue() {
        return whereValue;
    }
    
    public void setWhereValue(Object whereValue) {
        this.whereValue = whereValue;
    }
    
    public String getWhereOp() {
        return whereOp;
    }
    
    public void setWhereOp(String whereOp) {
        this.whereOp = whereOp;
    }
    
    public String getUpdateColumn() {
        return updateColumn;
    }
    
    public void setUpdateColumn(String updateColumn) {
        this.updateColumn = updateColumn;
    }
    
    public Object getUpdateValue() {
        return updateValue;
    }
    
    public void setUpdateValue(Object updateValue) {
        this.updateValue = updateValue;
    }
    
    public String getOrderByColumn() {
        return orderByColumn;
    }
    
    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }
    
    public boolean isOrderByDesc() {
        return orderByDesc;
    }
    
    public void setOrderByDesc(boolean orderByDesc) {
        this.orderByDesc = orderByDesc;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public Integer getOffset() {
        return offset;
    }
    
    public void setOffset(Integer offset) {
        this.offset = offset;
    }
    
    public Map<String, Class<?>> getColumnTypes() {
        return columnTypes;
    }
    
    public void addColumnType(String name, Class<?> type) {
        this.columnTypes.put(name, type);
    }
    
    public void setColumnTypes(Map<String, Class<?>> columnTypes) {
        this.columnTypes = columnTypes;
    }
    
    public String getJoinTable() {
        return joinTable;
    }
    
    public void setJoinTable(String joinTable) {
        this.joinTable = joinTable;
    }
    
    public String getJoinLeftColumn() {
        return joinLeftColumn;
    }
    
    public void setJoinLeftColumn(String joinLeftColumn) {
        this.joinLeftColumn = joinLeftColumn;
    }
    
    public String getJoinRightColumn() {
        return joinRightColumn;
    }
    
    public void setJoinRightColumn(String joinRightColumn) {
        this.joinRightColumn = joinRightColumn;
    }
    
    public String getJoinOp() {
        return joinOp;
    }
    
    public void setJoinOp(String joinOp) {
        this.joinOp = joinOp;
    }
    
    public boolean isLeftJoin() {
        return leftJoin;
    }
    
    public void setLeftJoin(boolean leftJoin) {
        this.leftJoin = leftJoin;
    }
    
    /**
     * 检查是否有JOIN子句
     */
    public boolean hasJoin() {
        return joinTable != null;
    }

    /**
     * 清除JOIN子句的所有数据
     */
    public void clearJoin() {
        this.joinTable = null;
        this.joinLeftColumn = null;
        this.joinRightColumn = null;
        this.joinOp = "=";
        this.leftJoin = false;
    }

    /**
     * 清除列和值
     */
    public void clearColumnsAndValues() {
        this.columns.clear();
        this.values.clear();
    }

    @Override
    public String toString() {
        return "SqlStatement{" +
                "type=" + type +
                ", tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", values=" + values +
                ", selectColumns=" + selectColumns +
                ", whereColumn='" + whereColumn + '\'' +
                ", whereValue=" + whereValue +
                ", whereOp='" + whereOp + '\'' +
                ", updateColumn='" + updateColumn + '\'' +
                ", updateValue=" + updateValue +
                ", joinTable='" + joinTable + '\'' +
                ", joinLeftColumn='" + joinLeftColumn + '\'' +
                ", joinRightColumn='" + joinRightColumn + '\'' +
                '}';
    }
}
