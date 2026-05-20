package com.zifang.util.ai.nnet;

/**
 * 均方误差损失函数
 * <p>
 * MSE = (1/n) * Σ(predictions[i] - targets[i])²
 */
public class MSELoss implements LossFunction {

    /**
     * 计算均方误差
     *
     * @param predictions 预测值数组
     * @param targets     目标值数组
     * @return 均方误差值
     * @throws IllegalArgumentException 当数组长度不匹配时
     */
    @Override
    public double compute(double[] predictions, double[] targets) {
        double sum = 0;
        for (int i = 0; i < predictions.length; i++) {
            double diff = predictions[i] - targets[i];
            sum += diff * diff;
        }
        return sum / predictions.length;
    }

    /**
     * 计算均方误差关于预测值的梯度
     *
     * @param predictions 预测值数组
     * @param targets     目标值数组
     * @return 梯度数组，梯度[i] = 2 * (predictions[i] - targets[i]) / n
     * @throws IllegalArgumentException 当数组长度不匹配时
     */
    @Override
    public double[] gradient(double[] predictions, double[] targets) {
        double[] gradient = new double[predictions.length];
        for (int i = 0; i < predictions.length; i++) {
            gradient[i] = 2.0 * (predictions[i] - targets[i]) / predictions.length;
        }
        return gradient;
    }
}