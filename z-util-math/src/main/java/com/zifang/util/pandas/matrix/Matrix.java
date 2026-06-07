package com.zifang.util.pandas.matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 矩阵类
 */
/**
 * Matrix类。
 */
/**
 * Matrix类。
 */
public class Matrix {

    private List<List<Double>> data = new ArrayList<>();

    /**
     * 矩阵乘法
     */
    /**
     * multiply方法。
     *      * @param another Matrix类型参数
     */
    /**
     * multiply方法。
     *      * @param another Matrix类型参数
     */
    public void multiply(Matrix another) {

    }

    /**
     * 矩阵的一行
     */
    /**
     * set方法。
     *      * @param arrays Double...类型参数
     */
    /**
     * set方法。
     *      * @param arrays Double...类型参数
     */
    public void set(Double... arrays) {
        data.add(Arrays.asList(arrays));
    }

    /**
     * 美化输出
     */
    /**
     * format方法。
     */
    /**
     * format方法。
     */
    public void format() {

    }

    private Integer analysisPadding() {
        Integer max = 0;
        for (List<Double> row : data) {
            for (Double col : row) {
                Integer cu = String.valueOf(col).length();
                if (cu > max) {
                    max = cu;
                }
            }
        }
        return max;
    }

    /**
     * shape方法。
     */
    /**
     * shape方法。
     */
    public void shape() {
    }

    /**
     * dtype方法。
     */
    /**
     * dtype方法。
     */
    public void dtype() {
    }

    /**
     * ndim方法。
     */
    /**
     * ndim方法。
     */
    public void ndim() {
    }

    /**
     * 切片方法
     */
    /**
     * slice方法。
     * @return List<List<Double>>类型返回值
     */
    /**
     * slice方法。
     * @return List<List<Double>>类型返回值
     */
    public List<List<Double>> slice() {
        return null;
    }

    /**
     * size方法。
     * @return int类型返回值
     */
    /**
     * size方法。
     * @return int类型返回值
     */
    public int size() {
        throw new RuntimeException();
    }
}
