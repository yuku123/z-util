package com.zifang.util.ai.nnet;

import java.util.ArrayList;
import java.util.List;

/**
 * 神经网络
 */
public class NeuralNetwork {

    private final List<Layer> layers;
    private LossFunction lossFunction;
    private double learningRate;

    public NeuralNetwork() {
        this.layers = new ArrayList<>();
        this.learningRate = 0.01;
    }

    public NeuralNetwork learningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    public NeuralNetwork lossFunction(LossFunction lossFunction) {
        this.lossFunction = lossFunction;
        return this;
    }

    public NeuralNetwork addLayer(Layer layer) {
        layers.add(layer);
        return this;
    }

    /**
     * 前向传播
     */
    public double[] predict(double[] inputs) {
        double[] current = inputs;
        for (Layer layer : layers) {
            current = layer.forward(current);
        }
        return current;
    }

    /**
     * 训练一步
     */
    public double train(double[] inputs, double[] targets) {
        // 前向传播
        double[] outputs = predict(inputs);

        // 计算损失
        double loss = 0;
        if (lossFunction != null) {
            loss = lossFunction.compute(outputs, targets);
        }

        // 反向传播
        double[] gradients = lossFunction != null
                ? lossFunction.gradient(outputs, targets)
                : calculateDefaultGradient(outputs, targets);

        for (int i = layers.size() - 1; i >= 0; i--) {
            gradients = layers.get(i).backward(gradients);
        }

        return loss;
    }

    private double[] calculateDefaultGradient(double[] predictions, double[] targets) {
        double[] gradient = new double[predictions.length];
        for (int i = 0; i < predictions.length; i++) {
            gradient[i] = predictions[i] - targets[i];
        }
        return gradient;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public double getLearningRate() {
        return learningRate;
    }
}