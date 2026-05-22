package com.zifang.util.ml.nnet.function;

/**
 * Sigmoid激活函数（S型曲线）
 * <p>
 * f(x) = 1 / (1 + e^(-x))
 */
public class Sigmoid implements IActivationFunction {

    /**
     * 计算Sigmoid激活值
     *
     * @param x 输入值
     * @return 激活值，范围在(0, 1)之间
     */
    @Override
    public Double calculate(Double x) {
        return 1D / (1 + Math.pow(Math.E, -x));
    }
}
