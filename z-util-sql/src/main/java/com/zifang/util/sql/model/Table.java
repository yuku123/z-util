package com.zifang.util.sql.model;

import java.util.*;

/**
 * 内存表，支持索引加速查询
 */
public class Table {
    
    private String name;
    private List<Column> columns;
    private List<Row> rows;
    
    // 索引结构: columnName -> (value -> rowIndices)
    private Map<String, Map<Object, List<Integer>>> indexMap;
    // 索引是否有效的标记
    private boolean indexesValid;
    
    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.indexMap = new HashMap<>();
        this.indexesValid = true;
    }
    
    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = new ArrayList<>(columns);
        this.rows = new ArrayList<>();
        this.indexMap = new HashMap<>();
        this.indexesValid = true;
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
        invalidateIndexes();
    }
    
    public void addColumn(String name, Class<?> type) {
        columns.add(new Column(name, type));
        invalidateIndexes();
    }
    
    public void setColumns(List<Column> columns) {
        this.columns = new ArrayList<>(columns);
        invalidateIndexes();
    }
    
    public List<Row> getRows() {
        return rows;
    }
    
    public void addRow(Row row) {
        if (row.size() != columns.size()) {
            throw new IllegalArgumentException("Row size does not match column count");
        }
        int rowIndex = rows.size();
        rows.add(row);
        // 更新索引
        updateIndexesOnInsert(row, rowIndex);
    }
    
    public void addRow(Object... values) {
        if (values.length != columns.size()) {
            throw new IllegalArgumentException("Values count does not match column count");
        }
        Row row = new Row(values);
        int rowIndex = rows.size();
        rows.add(row);
        updateIndexesOnInsert(row, rowIndex);
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

    public boolean hasColumn(String name) {
        return getColumnIndex(name) >= 0;
    }

    public Row getRow(int index) {
        if (index < 0 || index >= rows.size()) {
            return null;
        }
        return rows.get(index);
    }

    public Row removeRow(int index) {
        if (index < 0 || index >= rows.size()) {
            return null;
        }
        Row removed = rows.remove(index);
        invalidateIndexes();
        return removed;
    }

    /**
     * 根据索引值查找行号列表
     */
    public List<Integer> findRowsByIndexValue(String columnName, Object value) {
        return getRowIndicesByIndex(columnName, value);
    }

    /**
     * 复制表结构和数据
     */
    public Table copy() {
        Table copy = new Table(name);
        copy.setColumns(new ArrayList<>(columns));
        for (Row row : rows) {
            copy.addRow(new Row(new ArrayList<>(row.getValues())));
        }
        return copy;
    }
    
    // ========== 索引管理 ==========
    
    /**
     * 创建或更新某列的索引
     */
    public void createIndex(String columnName) {
        int colIndex = getColumnIndex(columnName);
        if (colIndex < 0) {
            throw new IllegalArgumentException("Column not found: " + columnName);
        }
        
        Map<Object, List<Integer>> indexData = new HashMap<>();
        for (int i = 0; i < rows.size(); i++) {
            Object value = rows.get(i).get(colIndex);
            indexData.computeIfAbsent(value, k -> new ArrayList<>()).add(i);
        }
        indexMap.put(columnName.toLowerCase(), indexData);
    }
    
    /**
     * 创建所有列的索引
     */
    public void createAllIndexes() {
        for (Column col : columns) {
            createIndex(col.getName());
        }
    }
    
    /**
     * 删除某列的索引
     */
    public void dropIndex(String columnName) {
        indexMap.remove(columnName.toLowerCase());
    }
    
    /**
     * 检查某列是否有索引
     */
    public boolean hasIndex(String columnName) {
        return indexMap.containsKey(columnName.toLowerCase());
    }
    
    /**
     * 获取某列索引的所有行号
     */
    public List<Integer> getRowIndicesByIndex(String columnName, Object value) {
        if (!indexesValid || !hasIndex(columnName)) {
            // 索引无效或不存在，进行全表扫描
            return scanAllRows(columnName, value);
        }
        
        Map<Object, List<Integer>> colIndex = indexMap.get(columnName.toLowerCase());
        List<Integer> indices = colIndex.get(value);
        return indices != null ? indices : Collections.emptyList();
    }
    
    /**
     * 全表扫描，查找匹配的行
     */
    private List<Integer> scanAllRows(String columnName, Object value) {
        List<Integer> result = new ArrayList<>();
        int colIndex = getColumnIndex(columnName);
        if (colIndex < 0) return result;
        
        for (int i = 0; i < rows.size(); i++) {
            Object rowValue = rows.get(i).get(colIndex);
            if (Objects.equals(rowValue, value)) {
                result.add(i);
            }
        }
        return result;
    }
    
    /**
     * 全表扫描，查找满足条件的行
     */
    public List<Integer> scanRows(String columnName, Object value, String op) {
        List<Integer> result = new ArrayList<>();
        int colIndex = getColumnIndex(columnName);
        if (colIndex < 0) return result;
        
        for (int i = 0; i < rows.size(); i++) {
            Object rowValue = rows.get(i).get(colIndex);
            if (compareValues(rowValue, value, op)) {
                result.add(i);
            }
        }
        return result;
    }
    
    private boolean compareValues(Object v1, Object v2, String op) {
        if ("=".equals(op) || "==".equals(op)) {
            return Objects.equals(v1, v2);
        }
        if ("!=".equals(op) || "<>".equals(op)) {
            return !Objects.equals(v1, v2);
        }
        
        // 数值比较
        if (v1 instanceof Number && v2 instanceof Number) {
            double n1 = ((Number) v1).doubleValue();
            double n2 = ((Number) v2).doubleValue();
            switch (op) {
                case ">": return n1 > n2;
                case "<": return n1 < n2;
                case ">=": return n1 >= n2;
                case "<=": return n1 <= n2;
            }
        }
        
        // 字符串比较
        if (v1 instanceof String && v2 instanceof String) {
            int cmp = ((String) v1).compareTo((String) v2);
            switch (op) {
                case ">": return cmp > 0;
                case "<": return cmp < 0;
                case ">=": return cmp >= 0;
                case "<=": return cmp <= 0;
            }
        }
        
        return false;
    }
    
    /**
     * 使索引失效（数据变更后调用）
     */
    private void invalidateIndexes() {
        indexesValid = false;
    }
    
    /**
     * 插入行时更新索引
     */
    private void updateIndexesOnInsert(Row row, int rowIndex) {
        if (!indexesValid) return;
        
        for (Map.Entry<String, Map<Object, List<Integer>>> entry : indexMap.entrySet()) {
            String colName = entry.getKey();
            int colIndex = getColumnIndex(colName);
            if (colIndex >= 0) {
                Object value = row.get(colIndex);
                entry.getValue().computeIfAbsent(value, k -> new ArrayList<>()).add(rowIndex);
            }
        }
    }
    
    /**
     * 重建所有索引
     */
    public void rebuildIndexes() {
        indexMap.clear();
        for (Column col : columns) {
            createIndex(col.getName());
        }
        indexesValid = true;
    }
    
    /**
     * 获取所有已索引的列名
     */
    public Set<String> getIndexedColumns() {
        return new HashSet<>(indexMap.keySet());
    }
    
    // ========== 辅助方法 ==========
    
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
        // 清空索引
        for (Map<Object, List<Integer>> colIndex : indexMap.values()) {
            colIndex.clear();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(name).append("\n");
        sb.append("Columns: ").append(columns).append("\n");
        sb.append("Rows: ").append(rows.size()).append("\n");
        if (!indexMap.isEmpty()) {
            sb.append("Indexes: ").append(indexMap.keySet()).append("\n");
        }
        return sb.toString();
    }
}
