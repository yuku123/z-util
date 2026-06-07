package com.zifang.util.visuallization.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * 图表数据系列
 * 用于存储和管理一个系列的数据点和标签
 */
/**
 * ChartSeries类。
 */
/**
 * ChartSeries类。
 */
public class ChartSeries {

    private final String name;
    private final List<Double> data;
    private final List<String> labels;

    /**
     * 创建数据系列
     * @param name 系列名称
     */
    /**
     * ChartSeries方法。
     *      * @param name String类型参数
     */
    public ChartSeries(String name) {
        this.name = name;
        this.data = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    /**
     * 创建数据系列
     * @param name 系列名称
     * @param data 初始数据列表
     */
    /**
     * ChartSeries方法。
     *      * @param name String类型参数
     * @param data ListDouble类型参数
     */
    public ChartSeries(String name, List<Double> data) {
        this.name = name;
        this.data = new ArrayList<>(data);
        this.labels = new ArrayList<>();
    }

    /**
     * 获取系列名称
     * @return 系列名称
     */
    /**
     * getName方法。
     * @return String类型返回值
     */
    public String getName() {
        return name;
    }

    /**
     * 获取数据副本
     * @return 数据列表副本
     */
    /**
     * getData方法。
     * @return List<Double>类型返回值
     */
    public List<Double> getData() {
        return new ArrayList<>(data);
    }

    /**
     * 获取标签副本
     * @return 标签列表副本
     */
    /**
     * getLabels方法。
     * @return List<String>类型返回值
     */
    public List<String> getLabels() {
        return new ArrayList<>(labels);
    }

    /**
     * 获取数据点数量
     * @return 数据点数量
     */
    /**
     * size方法。
     * @return int类型返回值
     */
    public int size() {
        return data.size();
    }

    /**
     * 获取指定索引的数据值
     * @param index 数据索引
     * @return 数据值
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    /**
     * getData方法。
     *      * @param index int类型参数
     * @return double类型返回值
     */
    public double getData(int index) {
        return data.get(index);
    }

    /**
     * 获取指定索引的标签
     * @param index 标签索引
     * @return 标签字符串（如果不存在则返回索引字符串）
     */
    /**
     * getLabel方法。
     *      * @param index int类型参数
     * @return String类型返回值
     */
    public String getLabel(int index) {
        if (index < labels.size()) {
            return labels.get(index);
        }
        return String.valueOf(index);
    }

    /**
     * 添加数据点
     * @param value 数据值
     */
    /**
     * addData方法。
     *      * @param value double类型参数
     */
    public void addData(double value) {
        data.add(value);
    }

    /**
     * 添加带标签的数据点
     * @param value 数据值
     * @param label 数据标签
     */
    /**
     * addData方法。
     *      * @param value double类型参数
     * @param label String类型参数
     */
    public void addData(double value, String label) {
        data.add(value);
        labels.add(label);
    }

    /**
     * 清空所有数据
     */
    /**
     * clear方法。
     */
    public void clear() {
        data.clear();
        labels.clear();
    }

    /**
     * 获取最大值
     * @return 最大值（如果数据为空返回0）
     */
    /**
     * getMax方法。
     * @return double类型返回值
     */
    public double getMax() {
        if (data.isEmpty()) return 0;
        double max = data.get(0);
        for (double v : data) {
            if (v > max) max = v;
        }
        return max;
    }

    /**
     * 获取最小值
     * @return 最小值（如果数据为空返回0）
     */
    /**
     * getMin方法。
     * @return double类型返回值
     */
    public double getMin() {
        if (data.isEmpty()) return 0;
        double min = data.get(0);
        for (double v : data) {
            if (v < min) min = v;
        }
        return min;
    }
}