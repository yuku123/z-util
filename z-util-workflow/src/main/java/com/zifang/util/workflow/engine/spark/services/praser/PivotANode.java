package com.zifang.util.workflow.engine.spark.services.praser;

import java.util.List;

/**
 * Pivot操作的单个列定义。
 * <p>
 * 用于描述数据透视表中单个需要透视的列的配置信息，
 * 包括列名、需要进行透视的值、以及统计值的聚合方式。
 *
 * @see PivotA
 */
/**
 * PivotANode类。
 */
/**
 * PivotANode类。
 */
public class PivotANode {

    /**
     * 将要被处理的column列名字。
     */
    private String columnName;

    /**
     * 定义这个列下面的哪些值需要被pivot，其他的会被省略。
     */
    private List<String> pivotColumns;

    /**
     * 统计频次的列，用于指定聚合列。
     */
    private String value;

    /**
     * 获取统计值列名。
     *
     * @return 统计值列名
     */
    /**
     * getValue方法。
     * @return String类型返回值
     */
    /**
     * getValue方法。
     * @return String类型返回值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置统计值列名。
     *
     * @param value 统计值列名
     */
    /**
     * setValue方法。
     *      * @param value String类型参数
     */
    /**
     * setValue方法。
     *      * @param value String类型参数
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取将被处理的列名。
     *
     * @return 列名
     */
    /**
     * getColumnName方法。
     * @return String类型返回值
     */
    /**
     * getColumnName方法。
     * @return String类型返回值
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 设置将被处理的列名。
     *
     * @param columnName 列名
     */
    /**
     * setColumnName方法。
     *      * @param columnName String类型参数
     */
    /**
     * setColumnName方法。
     *      * @param columnName String类型参数
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * 获取需要透视的值列表。
     *
     * @return 需要透视的值列表
     */
    /**
     * getPivotColumns方法。
     * @return List<String>类型返回值
     */
    /**
     * getPivotColumns方法。
     * @return List<String>类型返回值
     */
    public List<String> getPivotColumns() {
        return pivotColumns;
    }

    /**
     * 设置需要透视的值列表。
     *
     * @param pivotColumns 需要透视的值列表
     */
    /**
     * setPivotColumns方法。
     *      * @param pivotColumns ListString类型参数
     */
    /**
     * setPivotColumns方法。
     *      * @param pivotColumns ListString类型参数
     */
    public void setPivotColumns(List<String> pivotColumns) {
        this.pivotColumns = pivotColumns;
    }
}
