package com.zifang.util.sql.executor;

import com.zifang.util.sql.model.Row;
import com.zifang.util.sql.model.Table;

/**
 * SQL执行结果
 */
public class SqlResult {
    
    private boolean success;
    private String message;
    private int affectedRows;
    private Table resultTable;
    
    public SqlResult(boolean success, String message, int affectedRows) {
        this.success = success;
        this.message = message;
        this.affectedRows = affectedRows;
    }
    
    public SqlResult(boolean success, String message, int affectedRows, Table resultTable) {
        this.success = success;
        this.message = message;
        this.affectedRows = affectedRows;
        this.resultTable = resultTable;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public int getAffectedRows() {
        return affectedRows;
    }
    
    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }
    
    public Table getResultTable() {
        return resultTable;
    }
    
    public void setResultTable(Table resultTable) {
        this.resultTable = resultTable;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        if (resultTable != null) {
            sb.append("\n");
            sb.append(formatTable(resultTable));
        }
        return sb.toString();
    }
    
    /**
     * 格式化输出表
     */
    private String formatTable(Table table) {
        StringBuilder sb = new StringBuilder();
        
        // 计算每列宽度
        int colCount = table.getColumnCount();
        int[] colWidths = new int[colCount];
        
        for (int i = 0; i < colCount; i++) {
            colWidths[i] = table.getColumn(i).getName().length();
        }
        
        for (Row row : table.getRows()) {
            for (int i = 0; i < colCount; i++) {
                String val = row.get(i) != null ? row.get(i).toString() : "NULL";
                colWidths[i] = Math.max(colWidths[i], val.length());
            }
        }
        
        // 表头
        for (int i = 0; i < colCount; i++) {
            if (i > 0) sb.append(" | ");
            sb.append(String.format("%-" + colWidths[i] + "s", table.getColumn(i).getName()));
        }
        sb.append("\n");
        
        // 分隔线
        for (int i = 0; i < colCount; i++) {
            if (i > 0) sb.append("-+-");
            for (int j = 0; j < colWidths[i]; j++) sb.append("-");
        }
        sb.append("\n");
        
        // 数据行
        for (Row row : table.getRows()) {
            for (int i = 0; i < colCount; i++) {
                if (i > 0) sb.append(" | ");
                String val = row.get(i) != null ? row.get(i).toString() : "NULL";
                sb.append(String.format("%-" + colWidths[i] + "s", val));
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
