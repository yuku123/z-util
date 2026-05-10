package com.zifang.util.db.meta;

import java.util.Objects;


public class ColumnDTO {
    private final String columnName;
    private final String javaFieldName;
    private final String dataType;
    public final String javaType;
    private final int columnSize;
    private final boolean nullable;
    private final boolean isPrimaryKey;
    private final String comment;

    public ColumnDTO(String columnName, String dataType, int columnSize, boolean nullable, boolean isPrimaryKey, String comment) {
        this.columnName = Objects.requireNonNull(columnName, "字段名不能为空").trim();
        this.dataType = dataType == null ? "VARCHAR" : dataType.trim();
        this.columnSize = Math.max(columnSize, 0);
        this.nullable = nullable;
        this.isPrimaryKey = isPrimaryKey; // 赋值给isPrimaryKey
        this.comment = comment == null ? "" : comment.trim();
        this.javaFieldName = underlineToCamel(this.columnName);
        this.javaType = mapDbTypeToJavaType(this.dataType);
    }

    // 下划线转驼峰
    private String underlineToCamel(String str) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (char c : str.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                sb.append(nextUpper ? Character.toUpperCase(c) : c);
                nextUpper = false;
            }
        }
        return sb.length() == 0 ? "unknownField" : sb.toString();
    }

    // 数据库类型映射（Java 8兼容）
    private String mapDbTypeToJavaType(String dbType) {
        if (dbType == null) {
            return "Object";
        }
        String upperDbType = dbType.toUpperCase();
        switch (upperDbType) {
            case "INT":
            case "INTEGER":
                return "Integer";
            case "BIGINT":
                return "Long";
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
            case "LONGTEXT":
                return "String";
            case "DATETIME":
            case "TIMESTAMP":
                return "LocalDateTime";
            case "DATE":
                return "LocalDate";
            case "DECIMAL":
            case "NUMERIC":
                return "BigDecimal";
            case "DOUBLE":
            case "FLOAT":
                return "Double";
            case "BOOLEAN":
            case "BIT":
                return "Boolean";
            default:
                return "Object";
        }
    }

    // Getter方法（严格遵循JavaBean规范，isXXX形式）
    public String getColumnName() {
        return columnName;
    }

    public String getJavaFieldName() {
        return javaFieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getJavaType() {
        // 最后一层保障：极端情况返回Object
        return this.javaType == null ? "Object" : this.javaType;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public boolean isNullable() {
        return nullable;
    }

    // 关键修复：布尔类型属性使用isXXX形式的getter（模板会自动识别）
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "ColumnDTO{columnName=" + columnName + ", javaFieldName=" + javaFieldName + ", dataType=" + dataType + ", javaType=" + javaType + ", columnSize=" + columnSize + ", nullable=" + nullable + ", isPrimaryKey=" + isPrimaryKey + ", comment=" + comment + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnDTO that = (ColumnDTO) o;
        return columnSize == that.columnSize &&
                nullable == that.nullable &&
                isPrimaryKey == that.isPrimaryKey &&
                Objects.equals(columnName, that.columnName) &&
                Objects.equals(javaFieldName, that.javaFieldName) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(javaType, that.javaType) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, javaFieldName, dataType, javaType, columnSize, nullable, isPrimaryKey, comment);
    }
}
