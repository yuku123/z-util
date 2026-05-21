package com.zifang.util.sql.executor;

import com.zifang.util.sql.model.*;
import com.zifang.util.sql.parser.SqlParser;
import com.zifang.util.sql.parser.SqlStatement;
import com.zifang.util.sql.parser.SqlStatementType;

import java.util.*;

/**
 * SQL执行器
 */
public class SqlExecutor {
    
    private Database database;
    private SqlParser parser;
    
    public SqlExecutor() {
        this.database = new Database("default");
        this.parser = new SqlParser();
    }
    
    public SqlExecutor(Database database) {
        this.database = database;
        this.parser = new SqlParser();
    }
    
    /**
     * 执行SQL语句
     * @param sql SQL语句
     * @return 执行结果
     */
    public SqlResult execute(String sql) {
        SqlStatement stmt = parser.parse(sql);
        return execute(stmt);
    }
    
    /**
     * 执行解析后的SQL语句
     */
    public SqlResult execute(SqlStatement stmt) {
        switch (stmt.getType()) {
            case SELECT:
                return executeSelect(stmt);
            case INSERT:
                return executeInsert(stmt);
            case UPDATE:
                return executeUpdate(stmt);
            case DELETE:
                return executeDelete(stmt);
            case CREATE_TABLE:
                return executeCreateTable(stmt);
            case DROP_TABLE:
                return executeDropTable(stmt);
            default:
                return new SqlResult(false, "Unknown SQL statement type", 0);
        }
    }
    
    /**
     * 创建表
     */
    private SqlResult executeCreateTable(SqlStatement stmt) {
        if (!stmt.getColumnTypes().isEmpty()) {
            Table table = database.createTable(stmt.getTableName());
            for (Map.Entry<String, Class<?>> entry : stmt.getColumnTypes().entrySet()) {
                table.addColumn(entry.getKey(), entry.getValue());
            }
            // 创建索引
            table.rebuildIndexes();
            return new SqlResult(true, "Table '" + stmt.getTableName() + "' created", 0);
        }
        return new SqlResult(false, "No columns specified for table", 0);
    }
    
    /**
     * 删除表
     */
    private SqlResult executeDropTable(SqlStatement stmt) {
        if (database.hasTable(stmt.getTableName())) {
            database.dropTable(stmt.getTableName());
            return new SqlResult(true, "Table '" + stmt.getTableName() + "' dropped", 0);
        }
        return new SqlResult(false, "Table not found: " + stmt.getTableName(), 0);
    }
    
    /**
     * 执行INSERT
     */
    private SqlResult executeInsert(SqlStatement stmt) {
        Table table = database.getTable(stmt.getTableName());
        if (table == null) {
            return new SqlResult(false, "Table not found: " + stmt.getTableName(), 0);
        }
        
        if (stmt.getColumns().size() != stmt.getValues().size()) {
            return new SqlResult(false, "Column count does not match value count", 0);
        }
        
        Row row = new Row(table.getColumnCount());
        for (int i = 0; i < stmt.getColumns().size(); i++) {
            String colName = stmt.getColumns().get(i);
            String val = stmt.getValues().get(i);
            int colIndex = table.getColumnIndex(colName);
            if (colIndex < 0) {
                return new SqlResult(false, "Column not found: " + colName, 0);
            }
            Object value = convertValue(val, table.getColumn(colIndex).getType());
            row.set(colIndex, value);
        }
        
        table.addRow(row);
        return new SqlResult(true, "1 row inserted", 1);
    }
    
    /**
     * 执行SELECT
     */
    private SqlResult executeSelect(SqlStatement stmt) {
        Table table = database.getTable(stmt.getTableName());
        if (table == null) {
            return new SqlResult(false, "Table not found: " + stmt.getTableName(), 0);
        }
        
        // 处理JOIN
        if (stmt.hasJoin()) {
            return executeSelectWithJoin(stmt, table);
        }
        
        // 创建结果表
        Table result = table.createEmptyCopy();
        result.setName("result");
        
        // 确定要查询的列
        List<Integer> colIndexes = new ArrayList<>();
        List<String> selectCols = stmt.getSelectColumns();
        
        if (selectCols.isEmpty() || selectCols.get(0).equals("*")) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                colIndexes.add(i);
            }
        } else {
            for (String col : selectCols) {
                int idx = table.getColumnIndex(col);
                if (idx >= 0) {
                    colIndexes.add(idx);
                }
            }
        }
        
        // 设置结果表的列
        List<Column> resultCols = new ArrayList<>();
        for (int idx : colIndexes) {
            resultCols.add(table.getColumn(idx));
        }
        result.setColumns(resultCols);
        
        // 获取符合条件的行（使用索引加速）
        List<Integer> matchingRowIndices = getMatchingRowIndices(table, stmt);
        
        // 排序
        if (stmt.getOrderByColumn() != null) {
            int orderIdx = table.getColumnIndex(stmt.getOrderByColumn());
            if (orderIdx >= 0) {
                final int idx = orderIdx;
                final boolean desc = stmt.isOrderByDesc();
                matchingRowIndices.sort((i1, i2) -> {
                    Comparable v1 = (Comparable) table.getRow(i1).get(idx);
                    Comparable v2 = (Comparable) table.getRow(i2).get(idx);
                    if (v1 == null && v2 == null) return 0;
                    if (v1 == null) return desc ? 1 : -1;
                    if (v2 == null) return desc ? -1 : 1;
                    int cmp = v1.compareTo(v2);
                    return desc ? -cmp : cmp;
                });
            }
        }
        
        // LIMIT / OFFSET
        if (stmt.getOffset() != null && stmt.getOffset() > 0) {
            int offset = Math.min(stmt.getOffset(), matchingRowIndices.size());
            matchingRowIndices = matchingRowIndices.subList(offset, matchingRowIndices.size());
        }
        if (stmt.getLimit() != null && stmt.getLimit() > 0) {
            int end = Math.min(stmt.getLimit(), matchingRowIndices.size());
            matchingRowIndices = matchingRowIndices.subList(0, end);
        }
        
        // 构建结果行
        for (int rowIdx : matchingRowIndices) {
            Row srcRow = table.getRow(rowIdx);
            Row dstRow = new Row(colIndexes.size());
            for (int i = 0; i < colIndexes.size(); i++) {
                dstRow.set(i, srcRow.get(colIndexes.get(i)));
            }
            result.addRow(dstRow);
        }
        
        return new SqlResult(true, "Query OK", result.getRowCount(), result);
    }
    
    /**
     * 执行带JOIN的SELECT
     */
    private SqlResult executeSelectWithJoin(SqlStatement stmt, Table leftTable) {
        Table rightTable = database.getTable(stmt.getJoinTable());
        if (rightTable == null) {
            return new SqlResult(false, "Join table not found: " + stmt.getJoinTable(), 0);
        }
        
        String leftCol = stmt.getJoinLeftColumn();
        String rightCol = stmt.getJoinRightColumn();
        String joinOp = stmt.getJoinOp();
        
        int leftColIdx = leftTable.getColumnIndex(leftCol);
        int rightColIdx = rightTable.getColumnIndex(rightCol);
        
        if (leftColIdx < 0) {
            return new SqlResult(false, "Column not found in left table: " + leftCol, 0);
        }
        if (rightColIdx < 0) {
            return new SqlResult(false, "Column not found in right table: " + rightCol, 0);
        }
        
        // 构建结果表结构
        Table result = new Table("result");
        for (int i = 0; i < leftTable.getColumnCount(); i++) {
            result.addColumn(leftTable.getColumn(i));
        }
        for (int i = 0; i < rightTable.getColumnCount(); i++) {
            result.addColumn(rightTable.getColumn(i));
        }
        
        // 获取左表符合条件的行
        List<Integer> leftRowIndices = getMatchingRowIndices(leftTable, stmt);
        
        // 构建右表索引（如果右表在该列上有索引则使用索引）
        Map<Object, List<Integer>> rightIndex = new HashMap<>();
        for (int i = 0; i < rightTable.getRowCount(); i++) {
            Object key = rightTable.getRow(i).get(rightColIdx);
            rightIndex.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
        }
        
        // 执行JOIN
        for (int leftIdx : leftRowIndices) {
            Row leftRow = leftTable.getRow(leftIdx);
            Object joinKey = leftRow.get(leftColIdx);
            
            // 查找匹配的右表行
            List<Integer> rightIndices = rightIndex.get(joinKey);
            if (rightIndices != null) {
                for (int rightIdx : rightIndices) {
                    Row rightRow = rightTable.getRow(rightIdx);
                    Row joinedRow = new Row(result.getColumnCount());
                    
                    // 复制左表数据
                    for (int i = 0; i < leftTable.getColumnCount(); i++) {
                        joinedRow.set(i, leftRow.get(i));
                    }
                    // 复制右表数据
                    for (int i = 0; i < rightTable.getColumnCount(); i++) {
                        joinedRow.set(leftTable.getColumnCount() + i, rightRow.get(i));
                    }
                    result.addRow(joinedRow);
                }
            }
        }
        
        return new SqlResult(true, "Query OK", result.getRowCount(), result);
    }
    
    /**
     * 获取匹配WHERE条件的行索引，优先使用索引
     */
    private List<Integer> getMatchingRowIndices(Table table, SqlStatement stmt) {
        List<Integer> result = new ArrayList<>();
        
        String whereCol = stmt.getWhereColumn();
        Object whereVal = stmt.getWhereValue();
        String whereOp = stmt.getWhereOp() != null ? stmt.getWhereOp() : "=";
        
        // 如果WHERE条件是等于操作且列有索引，使用索引
        if (whereCol != null && ("=".equals(whereOp) || "==".equals(whereOp)) && table.hasIndex(whereCol)) {
            List<Integer> indexed = table.getRowIndicesByIndex(whereCol, whereVal);
            result.addAll(indexed);
        } else {
            // 全表扫描
            for (int i = 0; i < table.getRowCount(); i++) {
                Row row = table.getRow(i);
                if (matchesWhere(row, stmt, table)) {
                    result.add(i);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 检查行是否匹配WHERE条件
     */
    private boolean matchesWhere(Row row, SqlStatement stmt, Table table) {
        if (stmt.getWhereColumn() == null) {
            return true;
        }
        
        int colIdx = table.getColumnIndex(stmt.getWhereColumn());
        if (colIdx < 0) {
            return false;
        }
        
        Object rowValue = row.get(colIdx);
        Object condValue = stmt.getWhereValue();
        String op = stmt.getWhereOp() != null ? stmt.getWhereOp() : "=";
        
        // 简单比较
        if (condValue == null) {
            return rowValue == null;
        }
        
        return compareValues(rowValue, condValue, op);
    }
    
    private boolean compareValues(Object v1, Object v2, String op) {
        if ("=".equals(op) || "==".equals(op)) {
            return v1 != null && v1.equals(v2) || v1 == null && v2 == null;
        }
        if ("!=".equals(op) || "<>".equals(op)) {
            return !compareValues(v1, v2, "=");
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
     * 执行UPDATE
     */
    private SqlResult executeUpdate(SqlStatement stmt) {
        Table table = database.getTable(stmt.getTableName());
        if (table == null) {
            return new SqlResult(false, "Table not found: " + stmt.getTableName(), 0);
        }
        
        int updateCount = 0;
        List<Integer> toUpdate = getMatchingRowIndices(table, stmt);
        
        for (int idx : toUpdate) {
            Row row = table.getRow(idx);
            int colIdx = table.getColumnIndex(stmt.getUpdateColumn());
            if (colIdx >= 0) {
                Object value = convertValue(
                    stmt.getUpdateValue().toString(),
                    table.getColumn(colIdx).getType()
                );
                row.set(colIdx, value);
                updateCount++;
            }
        }
        
        return new SqlResult(true, updateCount + " rows updated", updateCount);
    }
    
    /**
     * 执行DELETE
     */
    private SqlResult executeDelete(SqlStatement stmt) {
        Table table = database.getTable(stmt.getTableName());
        if (table == null) {
            return new SqlResult(false, "Table not found: " + stmt.getTableName(), 0);
        }
        
        List<Integer> toDelete = getMatchingRowIndices(table, stmt);
        
        // 按索引倒序删除，避免删除后索引错乱
        Collections.sort(toDelete, Collections.reverseOrder());
        for (int idx : toDelete) {
            table.getRows().remove(idx);
        }
        
        return new SqlResult(true, toDelete.size() + " rows deleted", toDelete.size());
    }
    
    /**
     * 转换值类型
     */
    private Object convertValue(String val, Class<?> targetType) {
        if (val == null || "NULL".equalsIgnoreCase(val)) {
            return null;
        }
        
        val = val.trim();
        
        // 去除引号
        if ((val.startsWith("'") && val.endsWith("'")) || 
            (val.startsWith("\"") && val.endsWith("\""))) {
            val = val.substring(1, val.length() - 1);
            val = val.replace("\\'", "'");
            val = val.replace("\\\"", "\"");
            val = val.replace("\\\\", "\\");
        }
        
        if (targetType == String.class) {
            return val;
        }
        
        if (targetType == Integer.class || targetType == Integer.TYPE) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        if (targetType == Long.class || targetType == Long.TYPE) {
            try {
                return Long.parseLong(val);
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        
        if (targetType == Double.class || targetType == Double.TYPE) {
            try {
                return Double.parseDouble(val);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        
        if (targetType == Float.class || targetType == Float.TYPE) {
            try {
                return Float.parseFloat(val);
            } catch (NumberFormatException e) {
                return 0.0f;
            }
        }
        
        if (targetType == Boolean.class || targetType == Boolean.TYPE) {
            return Boolean.parseBoolean(val);
        }
        
        return val;
    }
    
    // ========== 数据库管理方法 ==========
    
    public Database getDatabase() {
        return database;
    }
    
    public void setDatabase(Database database) {
        this.database = database;
    }
    
    /**
     * 获取所有表名
     */
    public Set<String> getTableNames() {
        return database.getTableNames();
    }
    
    /**
     * 获取表
     */
    public Table getTable(String name) {
        return database.getTable(name);
    }
    
    /**
     * 检查表是否存在
     */
    public boolean hasTable(String name) {
        return database.hasTable(name);
    }
    
    /**
     * 创建表
     */
    public Table createTable(String name, String... columns) {
        Table table = database.createTable(name);
        for (String col : columns) {
            String[] parts = col.split(":");
            String colName = parts[0].trim();
            Class<?> colType = String.class;
            if (parts.length > 1) {
                String typeName = parts[1].trim().toUpperCase();
                switch (typeName) {
                    case "INT": colType = Integer.class; break;
                    case "LONG": colType = Long.class; break;
                    case "DOUBLE": colType = Double.class; break;
                    case "FLOAT": colType = Float.class; break;
                    case "BOOLEAN": colType = Boolean.class; break;
                    default: colType = String.class;
                }
            }
            table.addColumn(colName, colType);
        }
        table.rebuildIndexes();
        return table;
    }
    
    /**
     * 删除表
     */
    public void dropTable(String name) {
        database.dropTable(name);
    }
    
    /**
     * 清空表数据
     */
    public void truncateTable(String name) {
        Table table = database.getTable(name);
        if (table != null) {
            table.clearRows();
        }
    }
    
    /**
     * 为表创建索引
     */
    public void createIndex(String tableName, String columnName) {
        Table table = database.getTable(tableName);
        if (table != null) {
            table.createIndex(columnName);
        }
    }
    
    /**
     * 为表创建所有索引
     */
    public void createAllIndexes(String tableName) {
        Table table = database.getTable(tableName);
        if (table != null) {
            table.createAllIndexes();
        }
    }
    
    /**
     * 删除表的索引
     */
    public void dropIndex(String tableName, String columnName) {
        Table table = database.getTable(tableName);
        if (table != null) {
            table.dropIndex(columnName);
        }
    }
}
