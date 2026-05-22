package com.zifang.util.sql.model;

import java.util.*;

/**
 * 内存数据库
 */
public class Database {
    
    private String name;
    private Map<String, Table> tables;
    
    public Database(String name) {
        this.name = name;
        this.tables = new LinkedHashMap<>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void addTable(Table table) {
        tables.put(table.getName().toLowerCase(), table);
    }
    
    public Table createTable(String name) {
        Table table = new Table(name);
        addTable(table);
        return table;
    }
    
    public Table createTable(String name, Column... columns) {
        Table table = new Table(name);
        for (Column col : columns) {
            table.addColumn(col.getName(), col.getType());
        }
        addTable(table);
        return table;
    }
    
    public Table getTable(String name) {
        return tables.get(name.toLowerCase());
    }
    
    public boolean hasTable(String name) {
        return tables.containsKey(name.toLowerCase());
    }
    
    public boolean dropTable(String name) {
        return tables.remove(name.toLowerCase()) != null;
    }
    
    public Set<String> getTableNames() {
        return new LinkedHashSet<>(tables.keySet());
    }
    
    public Collection<Table> getTables() {
        return tables.values();
    }
    
    public int getTableCount() {
        return tables.size();
    }
    
    public void clearAllTables() {
        tables.clear();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Database: ").append(name).append("\n");
        sb.append("Tables: ").append(tables.size()).append("\n");
        for (Table table : tables.values()) {
            sb.append("  - ").append(table.getName())
              .append(" (").append(table.getColumnCount()).append(" columns, ")
              .append(table.getRowCount()).append(" rows)\n");
        }
        return sb.toString();
    }
}