package com.zifang.util.ml.nnet;

/**
 * Sigmoid激活函数
 * <p>
 * f(x) = 1 / (1 + e^(-x))
 */
/**
 * SigmoidActivation类。
 */
public class SigmoidActivation implements ActivationFunction {

    /**
     * 计算Sigmoid激活值
     *
     * @param input 输入值
     * @return 激活值，范围在(0, 1)之间
     */
    @Override
    /**
     * activate方法。
     *      * @param input double类型参数
     * @return double类型返回值
     */
    public double activate(double input) {
        return 1.0 / (1.0 + Math.exp(-input));
    }

    /**
     * 计算Sigmoid激活函数的导数
     *
     * @param input 输入值（通常是激活函数的输出）
     * @return 导数值，f'(x) = f(x) * (1 - f(x))
     */
    @Override
    /**
     * derivative方法。
     *      * @param input double类型参数
     * @return double类型返回值
     */
    public double derivative(double input) {
        double sigmoid = activate(input);
        return sigmoid * (1 - sigmoid);
    }
}