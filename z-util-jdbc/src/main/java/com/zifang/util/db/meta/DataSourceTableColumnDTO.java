package com.zifang.util.db.meta;

import com.zifang.util.core.meta.Description;

import java.util.Objects;

/**
 * 数据源表字段数据传输对象
 * 用于描述数据源中某个表的字段信息
 *
 * @author zifang
 */
public class DataSourceTableColumnDTO {

    @Description("列id")
    private Long datasourceTableColumnId;

    @Description("数据标记")
    private String datasourceCode;

    @Description("当前字段归属表名称")
    private String tableName;

    @Description("列名称")
    private String columnName;

    @Description("列类型")
    private String columnType;

    @Description("列长度")
    private String columnLength;

    @Description("列的注解")
    private String columnComment;

    public String nativeSignature() {
        return tableName + ":" + columnType + ":" + columnComment;
    }

    public Long getDatasourceTableColumnId() {
        return datasourceTableColumnId;
    }

    public void setDatasourceTableColumnId(Long datasourceTableColumnId) {
        this.datasourceTableColumnId = datasourceTableColumnId;
    }

    public String getDatasourceCode() {
        return datasourceCode;
    }

    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    @Override
    public String toString() {
        return "DataSourceTableColumnDTO{datasourceTableColumnId=" + datasourceTableColumnId + ", datasourceCode=" + datasourceCode + ", tableName=" + tableName + ", columnName=" + columnName + ", columnType=" + columnType + ", columnLength=" + columnLength + ", columnComment=" + columnComment + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceTableColumnDTO that = (DataSourceTableColumnDTO) o;
        return Objects.equals(datasourceTableColumnId, that.datasourceTableColumnId) &&
                Objects.equals(datasourceCode, that.datasourceCode) &&
                Objects.equals(tableName, that.tableName) &&
                Objects.equals(columnName, that.columnName) &&
                Objects.equals(columnType, that.columnType) &&
                Objects.equals(columnLength, that.columnLength) &&
                Objects.equals(columnComment, that.columnComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasourceTableColumnId, datasourceCode, tableName, columnName, columnType, columnLength, columnComment);
    }
}
