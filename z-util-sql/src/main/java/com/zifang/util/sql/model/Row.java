package com.zifang.util.sql.model;

import java.util.*;

/**
 * 行数据
 */
public class Row implements Iterable<Object> {
    
    private List<Object> values;
    
    public Row() {
        this.values = new ArrayList<>();
    }
    
    public Row(int size) {
        this.values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.values.add(null);
        }
    }
    
    public Row(Object... values) {
        this.values = new ArrayList<>(Arrays.asList(values));
    }
    
    public Row(List<Object> values) {
        this.values = new ArrayList<>(values);
    }
    
    public void add(Object value) {
        values.add(value);
    }
    
    public Object get(int index) {
        if (index < 0 || index >= values.size()) {
            return null;
        }
        return values.get(index);
    }
    
    public void set(int index, Object value) {
        if (index < 0 || index >= values.size()) {
            return;
        }
        values.set(index, value);
    }
    
    public List<Object> getValues() {
        return values;
    }
    
    public void setValues(List<Object> values) {
        this.values = new ArrayList<>(values);
    }
    
    public int size() {
        return values.size();
    }
    
    public boolean isEmpty() {
        return values.isEmpty();
    }
    
    public void clear() {
        values.clear();
    }

    public boolean contains(Object value) {
        return values.contains(value);
    }

    public int indexOf(Object value) {
        return values.indexOf(value);
    }

    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public Iterator<Object> iterator() {
        return values.iterator();
    }
    
    @Override
    public String toString() {
        return values.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return Objects.equals(values, row.values);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}