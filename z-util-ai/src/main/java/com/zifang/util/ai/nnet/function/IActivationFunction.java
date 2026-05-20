package com.zifang.util.ai.nnet.function;

/**
 * 激活函数接口
 * <p>
 * 所有激活函数实现类都需要实现此接口
 */
public interface IActivationFunction {

    /**
     * 欧拉常数（自然对数的底）
     */
    Double E = Math.E;

    /**
     * 计算激活值
     *
     * @param x 输入值
     * @return 激活后的值
     */
    Double calculate(Double x);
}
