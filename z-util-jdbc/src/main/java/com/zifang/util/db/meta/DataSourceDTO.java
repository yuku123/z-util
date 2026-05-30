package com.zifang.util.db.meta;

import com.zifang.util.core.meta.Description;

import java.util.Objects;

/**
 * 数据源信息传输对象
 *
 * @author zifang
 */
/**
 * DataSourceDTO类。
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

    /**
     * 获取数据源id
     *
     * @return 数据源id
     */
    /**
     * getId方法。
     * @return long类型返回值
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置数据源id
     *
     * @param id 数据源id
     */
    /**
     * setId方法。
     *      * @param id long类型参数
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取数据标识
     *
     * @return 数据标识
     */
    /**
     * getDatasourceCode方法。
     * @return String类型返回值
     */
    public String getDatasourceCode() {
        return datasourceCode;
    }

    /**
     * 设置数据标识
     *
     * @param datasourceCode 数据标识
     */
    /**
     * setDatasourceCode方法。
     *      * @param datasourceCode String类型参数
     */
    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }

    /**
     * 获取数据源名称
     *
     * @return 数据源名称
     */
    /**
     * getDatasourceName方法。
     * @return String类型返回值
     */
    public String getDatasourceName() {
        return datasourceName;
    }

    /**
     * 设置数据源名称
     *
     * @param datasourceName 数据源名称
     */
    /**
     * setDatasourceName方法。
     *      * @param datasourceName String类型参数
     */
    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    /**
     * 获取数据源地址
     *
     * @return 数据源地址
     */
    /**
     * getDatasourceUrl方法。
     * @return String类型返回值
     */
    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    /**
     * 设置数据源地址
     *
     * @param datasourceUrl 数据源地址
     */
    /**
     * setDatasourceUrl方法。
     *      * @param datasourceUrl String类型参数
     */
    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl;
    }

    /**
     * 获取端口号
     *
     * @return 端口号
     */
    /**
     * getPortNumber方法。
     * @return int类型返回值
     */
    public Integer getPortNumber() {
        return portNumber;
    }

    /**
     * 设置端口号
     *
     * @param portNumber 端口号
     */
    /**
     * setPortNumber方法。
     *      * @param portNumber int类型参数
     */
    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * 获取库名称
     *
     * @return 库名称
     */
    /**
     * getSchemaMark方法。
     * @return String类型返回值
     */
    public String getSchemaMark() {
        return schemaMark;
    }

    /**
     * 设置库名称
     *
     * @param schemaMark 库名称
     */
    /**
     * setSchemaMark方法。
     *      * @param schemaMark String类型参数
     */
    public void setSchemaMark(String schemaMark) {
        this.schemaMark = schemaMark;
    }

    /**
     * 获取用户名称
     *
     * @return 用户名称
     */
    /**
     * getUserName方法。
     * @return String类型返回值
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名称
     *
     * @param userName 用户名称
     */
    /**
     * setUserName方法。
     *      * @param userName String类型参数
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    /**
     * getPw方法。
     * @return String类型返回值
     */
    public String getPw() {
        return pw;
    }

    /**
     * 设置密码
     *
     * @param pw 密码
     */
    /**
     * setPw方法。
     *      * @param pw String类型参数
     */
    public void setPw(String pw) {
        this.pw = pw;
    }

    /**
     * 获取数据描述
     *
     * @return 数据描述
     */
    /**
     * getDescriptions方法。
     * @return String类型返回值
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * 设置数据描述
     *
     * @param descriptions 数据描述
     */
    /**
     * setDescriptions方法。
     *      * @param descriptions String类型参数
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * 获取数据源类型
     *
     * @return 数据源类型
     */
    /**
     * getDatasourceType方法。
     * @return String类型返回值
     */
    public String getDatasourceType() {
        return datasourceType;
    }

    /**
     * 设置数据源类型
     *
     * @param datasourceType 数据源类型
     */
    /**
     * setDatasourceType方法。
     *      * @param datasourceType String类型参数
     */
    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType;
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "DataSourceDTO{id=" + id + ", datasourceCode=" + datasourceCode + ", datasourceName=" + datasourceName + ", datasourceUrl=" + datasourceUrl + ", portNumber=" + portNumber + ", schemaMark=" + schemaMark + ", userName=" + userName + ", pw=" + pw + ", descriptions=" + descriptions + ", datasourceType=" + datasourceType + "}";
    }

    @Override
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
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
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    public int hashCode() {
        return Objects.hash(id, datasourceCode, datasourceName, datasourceUrl, portNumber, schemaMark, userName, pw, descriptions, datasourceType);
    }
}
