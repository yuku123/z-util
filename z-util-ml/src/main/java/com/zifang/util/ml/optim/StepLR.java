package com.zifang.util.ml.optim;

/**
 * Step learning rate scheduler.
 * 
 * Decays the learning rate by a factor of gamma every step_size epochs.
 * 
 * Formula:
 *   lr = base_lr * gamma^(epoch / step_size)
 */
public class StepLR implements LrScheduler {
    
    private final Optimizer optimizer;
    private final double baseLearningRate;
    private final int stepSize;
    private final double gamma;
    private int epoch;
    
    public StepLR(Optimizer optimizer, int stepSize, double gamma) {
        this(optimizer, stepSize, gamma, 0);
    }
    
    public StepLR(Optimizer optimizer, int stepSize, double gamma, int lastEpoch) {
        this.optimizer = optimizer;
        this.baseLearningRate = optimizer.getLearningRate();
        this.stepSize = stepSize;
        this.gamma = gamma;
        this.epoch = lastEpoch;
    }
    
    @Override
    public void step() {
        epoch++;
        double newLr = baseLearningRate * Math.pow(gamma, epoch / stepSize);
        optimizer.setLearningRate(newLr);
    }
    
    @Override
    public double getLastLR() {
        return optimizer.getLearningRate();
    }
    
    /**
     * Get the current epoch.
     */
    public int getEpoch() {
        return epoch;
    }
    
    /**
     * Get the step size.
     */
    public int getStepSize() {
        return stepSize;
    }
    
    /**
     * Get the gamma factor.
     */
    public double getGamma() {
        return gamma;
    }
    
    /**
     * Get the base learning rate.
     */
    public double getBaseLearningRate() {
        return baseLearningRate;
    }
}
