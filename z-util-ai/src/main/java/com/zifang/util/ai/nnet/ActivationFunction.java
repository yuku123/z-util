package com.zifang.util.ai.nnet;

/**
 * 激活函数接口
 */
public interface ActivationFunction {

    /**
     * 计算激活值
     *
     * @param input 输入值
     * @return 激活后的值
     */
    double activate(double input);

    /**
     * 计算激活函数的导数
     *
     * @param input 输入值
     * @return 导数值
     */
    double derivative(double input);
}