package com.zifang.util.ai.nnet;

/**
 * ReLU激活函数
 */
public class ReLUActivation implements ActivationFunction {

    @Override
    public double activate(double input) {
        return Math.max(0, input);
    }

    @Override
    public double derivative(double input) {
        return input > 0 ? 1 : 0;
    }
}