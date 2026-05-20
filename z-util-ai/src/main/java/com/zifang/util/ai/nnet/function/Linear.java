package com.zifang.util.ai.nnet.function;

/**
 * 线性激活函数
 * <p>
 * f(x) = x
 */
public class Linear implements IActivationFunction {

    /**
     * 计算线性激活值（恒等函数）
     *
     * @param x 输入值
     * @return 输入值本身
     */
    @Override
    public Double calculate(Double x) {
        return x;
    }
}
