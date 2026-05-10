package com.zifang.util.db.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 表DTO
 */
public class TableDTO {
    public final String tableName;
    public final String entityName;
    public final String comment;
    private final List<ColumnDTO> columns = new ArrayList<>();
    public ColumnDTO primaryKey;

    public TableDTO(String tableName, String comment) {
        String trimmedName = Objects.requireNonNull(tableName, "表名不能为空").trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("表名不能为空白");
        }
        this.tableName = trimmedName;
        this.comment = comment == null ? "" : comment.trim();
        this.entityName = underlineToCamelUpper(trimmedName);
    }

    private String underlineToCamelUpper(String str) {
        String camel = new ColumnDTO(str, "", 0, false, false, "").getJavaFieldName();
        return camel.isEmpty() ? "UnknownEntity" : Character.toUpperCase(camel.charAt(0)) + camel.substring(1);
    }

    public void addColumn(ColumnDTO column) {
        if (column != null) {
            columns.add(column);
            if (column.isPrimaryKey() && primaryKey == null) {
                primaryKey = column;
            }
        }
    }

    public List<ColumnDTO> getColumns() {
        return new ArrayList<>(columns);
    }

    public void debugPrint() {
        System.out.println("=== TableDTO调试 ===");
        System.out.println("tableName: " + tableName);
        System.out.println("entityName: " + entityName);
        System.out.println("字段数: " + columns.size());
        System.out.println("===================");
    }

    public String getTableName() {
        return tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getComment() {
        return comment;
    }

    public ColumnDTO getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnDTO primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "TableDTO{tableName=" + tableName + ", entityName=" + entityName + ", comment=" + comment + ", columns=" + columns + ", primaryKey=" + primaryKey + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableDTO tableDTO = (TableDTO) o;
        return Objects.equals(tableName, tableDTO.tableName) &&
                Objects.equals(entityName, tableDTO.entityName) &&
                Objects.equals(comment, tableDTO.comment) &&
                Objects.equals(columns, tableDTO.columns) &&
                Objects.equals(primaryKey, tableDTO.primaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, entityName, comment, columns, primaryKey);
    }
}
