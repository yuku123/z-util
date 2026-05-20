package com.zifang.util.ai.nnet;

/**
 * ReLU激活函数
 * <p>
 * f(x) = max(0, x)
 */
public class ReLUActivation implements ActivationFunction {

    /**
     * 计算ReLU激活值
     *
     * @param input 输入值
     * @return 激活值， Returns max(0, input)
     */
    @Override
    public double activate(double input) {
        return Math.max(0, input);
    }

    /**
     * 计算ReLU激活函数的导数
     *
     * @param input 输入值（通常是激活函数的输出）
     * @return 导数值，当input > 0时返回1，否则返回0
     */
    @Override
    public double derivative(double input) {
        return input > 0 ? 1 : 0;
    }
}