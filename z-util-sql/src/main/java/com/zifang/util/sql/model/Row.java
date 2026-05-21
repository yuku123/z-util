package com.zifang.util.sql.model;

import java.util.*;

/**
 * 行数据
 */
public class Row {
    
    private Object[] values;
    
    public Row(int size) {
        this.values = new Object[size];
    }
    
    public Row(Object[] values) {
        this.values = values;
    }
    
    public Object get(int index) {
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        }
        return values[index];
    }
    
    public void set(int index, Object value) {
        if (index < 0 || index >= values.length) {
            throw new IndexOutOfBoundsException("Column index out of bounds: " + index);
        }
        values[index] = value;
    }
    
    public Object[] getValues() {
        return values;
    }
    
    public void setValues(Object[] values) {
        this.values = values;
    }
    
    public int size() {
        return values.length;
    }
    
    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
