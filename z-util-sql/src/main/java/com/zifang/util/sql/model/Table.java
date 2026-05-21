package com.zifang.util.sql.model;

import java.util.*;

/**
 * 内存表
 */
public class Table {
    
    private String name;
    private List<Column> columns;
    private List<Row> rows;
    
    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }
    
    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = new ArrayList<>(columns);
        this.rows = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Column> getColumns() {
        return columns;
    }
    
    public void addColumn(Column column) {
        columns.add(column);
    }
    
    public void addColumn(String name, Class<?> type) {
        columns.add(new Column(name, type));
    }
    
    public void setColumns(List<Column> columns) {
        this.columns = new ArrayList<>(columns);
    }
    
    public List<Row> getRows() {
        return rows;
    }
    
    public void addRow(Row row) {
        if (row.size() != columns.size()) {
            throw new IllegalArgumentException("Row size does not match column count");
        }
        rows.add(row);
    }
    
    public void addRow(Object... values) {
        if (values.length != columns.size()) {
            throw new IllegalArgumentException("Values count does not match column count");
        }
        rows.add(new Row(values));
    }
    
    public int getColumnCount() {
        return columns.size();
    }
    
    public int getRowCount() {
        return rows.size();
    }
    
    public Column getColumn(int index) {
        return columns.get(index);
    }
    
    public Column getColumn(String name) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getName().equalsIgnoreCase(name)) {
                return columns.get(i);
            }
        }
        return null;
    }
    
    public int getColumnIndex(String name) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
    
    public Row getRow(int index) {
        return rows.get(index);
    }
    
    /**
     * 创建一个新表，包含相同的列结构
     */
    public Table createEmptyCopy() {
        Table copy = new Table(name);
        copy.setColumns(new ArrayList<>(columns));
        return copy;
    }
    
    /**
     * 清空所有行数据
     */
    public void clearRows() {
        rows.clear();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(name).append("\n");
        sb.append("Columns: ").append(columns).append("\n");
        sb.append("Rows: ").append(rows.size()).append("\n");
        return sb.toString();
    }
}
