package com.zifang.util.ai.nnet;

/**
 * 损失函数接口
 */
public interface LossFunction {

    /**
     * 计算损失
     */
    double compute(double[] predictions, double[] targets);

    /**
     * 计算损失梯度
     */
    double[] gradient(double[] predictions, double[] targets);
}