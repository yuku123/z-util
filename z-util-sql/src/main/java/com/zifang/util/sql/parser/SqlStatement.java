package com.zifang.util.sql.parser;

import java.util.*;

/**
 * SQL语句解析结果
 */
public class SqlStatement {
    
    private SqlStatementType type;
    private String tableName;
    private List<String> columns;  // SELECT columns or INSERT/CREATE columns
    private List<String> values;   // INSERT values
    private List<String> selectColumns; // for SELECT
    private String whereColumn;     // for WHERE column = value
    private Object whereValue;
    private String updateColumn;    // for UPDATE column = value
    private Object updateValue;
    private String orderByColumn;  // ORDER BY column
    private boolean orderByDesc;
    private Integer limit;
    private Integer offset;
    
    // For CREATE TABLE
    private Map<String, Class<?>> columnTypes;
    
    // For SELECT ... FROM table1 JOIN table2 ON ...
    private String joinTable;
    private String joinCondition; // e.g., "id = user_id"
    
    public SqlStatement() {
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
        this.selectColumns = new ArrayList<>();
        this.columnTypes = new LinkedHashMap<>();
        this.orderByDesc = false;
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
    
    public List<String> getValues() {
        return values;
    }
    
    public void addValue(String value) {
        this.values.add(value);
    }
    
    public void setValues(List<String> values) {
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
    
    public String getJoinCondition() {
        return joinCondition;
    }
    
    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
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
                ", updateColumn='" + updateColumn + '\'' +
                ", updateValue=" + updateValue +
                '}';
    }
}
