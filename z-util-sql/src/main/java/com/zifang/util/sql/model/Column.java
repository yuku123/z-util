package com.zifang.util.sql.model;

import java.util.*;

/**
 * 列定义
 */
public class Column {
    
    private String name;
    private Class<?> type;
    
    public Column(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Class<?> getType() {
        return type;
    }
    
    public void setType(Class<?> type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return name + " (" + type.getSimpleName() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name) && Objects.equals(type, column.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
