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
}
