package com.zifang.util.db.meta;

import com.zifang.util.core.meta.Description;

import java.util.Objects;

/**
 * @author zifang
 */
public class DataSourceDTO {

    @Description("数据源id")
    private Long id;

    @Description("数据标识")
    private String datasourceCode;

    @Description("数据源名称")
    private String datasourceName;

    @Description("数据源地址")
    private String datasourceUrl;

    @Description("端口")
    private Integer portNumber;

    @Description("库名称")
    private String schemaMark;

    @Description("用户名称")
    private String userName;

    @Description("密码")
    private String pw;

    @Description("当前数据描述")
    private String descriptions;

    @Description("数据源类型")
    private String datasourceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatasourceCode() {
        return datasourceCode;
    }

    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    public String getSchemaMark() {
        return schemaMark;
    }

    public void setSchemaMark(String schemaMark) {
        this.schemaMark = schemaMark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType;
    }

    @Override
    public String toString() {
        return "DataSourceDTO{id=" + id + ", datasourceCode=" + datasourceCode + ", datasourceName=" + datasourceName + ", datasourceUrl=" + datasourceUrl + ", portNumber=" + portNumber + ", schemaMark=" + schemaMark + ", userName=" + userName + ", pw=" + pw + ", descriptions=" + descriptions + ", datasourceType=" + datasourceType + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSourceDTO that = (DataSourceDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(datasourceCode, that.datasourceCode) &&
                Objects.equals(datasourceName, that.datasourceName) &&
                Objects.equals(datasourceUrl, that.datasourceUrl) &&
                Objects.equals(portNumber, that.portNumber) &&
                Objects.equals(schemaMark, that.schemaMark) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(pw, that.pw) &&
                Objects.equals(descriptions, that.descriptions) &&
                Objects.equals(datasourceType, that.datasourceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datasourceCode, datasourceName, datasourceUrl, portNumber, schemaMark, userName, pw, descriptions, datasourceType);
    }
}
