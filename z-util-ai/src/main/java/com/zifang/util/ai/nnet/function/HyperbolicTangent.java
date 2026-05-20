package com.zifang.util.ai.nnet.function;

/**
 * 双曲正切函数
 * <p>
 * f(x) = (e^x - e^(-x)) / (e^x + e^(-x))
 */
public class HyperbolicTangent implements IActivationFunction {

    /**
     * 计算双曲正切激活值
     *
     * @param x 输入值
     * @return 激活值，范围在(-1, 1)之间
     */
    @Override
    public Double calculate(Double x) {
        return (1D - Math.pow(E, -x)) / (1D + Math.pow(E, x));
    }
}
