package com.zifang.util.core.meta;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zifang
 */
/**
 * SortField类。
 */
/**
 * SortField类。
 */
public class SortField implements Serializable {

    private static final long serialVersionUID = -7342671270425244017L;

    /**
     * 需要进行排序的字段
     */
    private String column;
    /**
     * 是否正序排列，默认 true
     */
    private boolean asc = true;

    /**
     * SortField方法。
     *      * @param column String类型参数
     * @param asc boolean类型参数
     */
    /**
     * SortField方法。
     *      * @param column String类型参数
     * @param asc boolean类型参数
     */
    public SortField(String column, boolean asc) {
        this.column = column;
        this.asc = asc;
    }

    /**
     * SortField方法。
     */
    /**
     * SortField方法。
     */
    public SortField() {
    }

    /**
     * getColumn方法。
     * @return String类型返回值
     */
    /**
     * getColumn方法。
     * @return String类型返回值
     */
    public String getColumn() {
        return column;
    }

    /**
     * setColumn方法。
     *      * @param column String类型参数
     */
    /**
     * setColumn方法。
     *      * @param column String类型参数
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * isAsc方法。
     * @return boolean类型返回值
     */
    /**
     * isAsc方法。
     * @return boolean类型返回值
     */
    public boolean isAsc() {
        return asc;
    }

    /**
     * setAsc方法。
     *      * @param asc boolean类型参数
     */
    /**
     * setAsc方法。
     *      * @param asc boolean类型参数
     */
    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    /**
     * asc方法。
     *      * @param column String类型参数
     * @return static SortField类型返回值
     */
    /**
     * asc方法。
     *      * @param column String类型参数
     * @return static SortField类型返回值
     */
    public static SortField asc(String column) {
        return build(column, true);
    }

    /**
     * desc方法。
     *      * @param column String类型参数
     * @return static SortField类型返回值
     */
    /**
     * desc方法。
     *      * @param column String类型参数
     * @return static SortField类型返回值
     */
    public static SortField desc(String column) {
        return build(column, false);
    }

    /**
     * ascs方法。
     *      * @param columns String...类型参数
     * @return static List<SortField>类型返回值
     */
    /**
     * ascs方法。
     *      * @param columns String...类型参数
     * @return static List<SortField>类型返回值
     */
    public static List<SortField> ascs(String... columns) {
        return Arrays.stream(columns).map(SortField::asc).collect(Collectors.toList());
    }

    /**
     * descs方法。
     *      * @param columns String...类型参数
     * @return static List<SortField>类型返回值
     */
    /**
     * descs方法。
     *      * @param columns String...类型参数
     * @return static List<SortField>类型返回值
     */
    public static List<SortField> descs(String... columns) {
        return Arrays.stream(columns).map(SortField::desc).collect(Collectors.toList());
    }

    private static SortField build(String column, boolean asc) {
        return new SortField(column, asc);
    }

    @Override
    /**
     * toString方法。
     * @return String类型返回值
     */
    /**
     * toString方法。
     * @return String类型返回值
     */
    public String toString() {
        return "SortField{column=" + column + ", asc=" + asc + "}";
    }

    @Override
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    /**
     * equals方法。
     *      * @param o Object类型参数
     * @return boolean类型返回值
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortField sortField = (SortField) o;
        return asc == sortField.asc && java.util.Objects.equals(column, sortField.column);
    }

    @Override
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    /**
     * hashCode方法。
     * @return int类型返回值
     */
    public int hashCode() {
        return java.util.Objects.hash(column, asc);
    }
}