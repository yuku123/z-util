package com.zifang.util.ai.nnet;

/**
 * 均方误差损失函数
 */
public class MSELoss implements LossFunction {

    @Override
    public double compute(double[] predictions, double[] targets) {
        double sum = 0;
        for (int i = 0; i < predictions.length; i++) {
            double diff = predictions[i] - targets[i];
            sum += diff * diff;
        }
        return sum / predictions.length;
    }

    @Override
    public double[] gradient(double[] predictions, double[] targets) {
        double[] gradient = new double[predictions.length];
        for (int i = 0; i < predictions.length; i++) {
            gradient[i] = 2.0 * (predictions[i] - targets[i]) / predictions.length;
        }
        return gradient;
    }
}