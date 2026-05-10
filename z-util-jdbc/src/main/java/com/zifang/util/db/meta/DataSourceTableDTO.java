package com.zifang.util.db.meta;

import com.zifang.util.core.meta.Description;

import java.util.List;
import java.util.Objects;

/**
 * @author zifang
 */
public class DataSourceTableDTO {

    @Description("数据源下表 id")
    private Long datasourceTableId;

    @Description("数据标识")
    private String datasourceCode;

    @Description("表名称")
    private String tableName;

    @Description("表描述")
    private String descriptions;

    @Description("表下的字段信息")
    private List<DataSourceTableColumnDTO> columns;

    public String getComponentCode() {
        return datasourceCode + ":" + tableName;
    }

    public Long getDatasourceTableId() {
        return datasourceTableId;
    }

    public void setDatasourceTableId(Long datasourceTableId) {
        this.datasourceTableId = datasourceTableId;
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

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public List<DataSourceTableColumnDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<DataSourceTableColumnDTO> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "DataSourceTableDTO{datasourceTableId=" + datasourceTableId + ", datasourceCode=" + datasourceCode + ", tableName=" + tableName + ", descriptions=" + descriptions + ", columns=" + columns + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceTableDTO that = (DataSourceTableDTO) o;
        return Objects.equals(datasourceTableId, that.datasourceTableId) &&
                Objects.equals(datasourceCode, that.datasourceCode) &&
                Objects.equals(tableName, that.tableName) &&
                Objects.equals(descriptions, that.descriptions) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasourceTableId, datasourceCode, tableName, descriptions, columns);
    }
}
