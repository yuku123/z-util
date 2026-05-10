package com.zifang.util.ai.nnet;

import java.util.Random;

/**
 * 神经元
 */
public class Neuron {

    private final double[] weights;
    private final double bias;
    private double output;
    private double delta;

    private static final Random RANDOM = new Random();

    /**
     * 创建神经元
     *
     * @param inputCount 输入数量
     */
    public Neuron(int inputCount) {
        this.weights = new double[inputCount];
        initializeWeights();
        this.bias = RANDOM.nextDouble() * 2 - 1;
    }

    private void initializeWeights() {
        // Xavier初始化
        double limit = Math.sqrt(6.0 / (weights.length + 1));
        for (int i = 0; i < weights.length; i++) {
            weights[i] = RANDOM.nextDouble() * 2 * limit - limit;
        }
    }

    /**
     * 计算神经元输出
     */
    public double calculateOutput(double[] inputs, ActivationFunction activationFunction) {
        double sum = 0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        sum += bias;
        this.output = activationFunction.activate(sum);
        return this.output;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getBias() {
        return bias;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }
}