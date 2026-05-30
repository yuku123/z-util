package com.zifang.util.ml.optim;

import com.zifang.util.numpy.NdArray;

import java.util.*;

/**
 * Base abstract class for all optimizers.
 * Provides parameter management, gradient zeroing, and common functionality.
 */
public abstract class Optimizer {
    
    protected double learningRate;
    protected double weightDecay;
    protected Map<String, NdArray> parameters;
    protected Map<String, NdArray> gradients;
    protected Map<String, NdArray> state;
    
    /**
     * Optimizer方法。
     *      * @param learningRate double类型参数
     * @param weightDecay double类型参数
     */
    public Optimizer(double learningRate, double weightDecay) {
        this.learningRate = learningRate;
        this.weightDecay = weightDecay;
        this.parameters = new HashMap<>();
        this.gradients = new HashMap<>();
        this.state = new HashMap<>();
    }
    
    /**
     * Register a parameter for optimization.
     * 
     * @param name Parameter name (e.g., "layer1.weight")
     * @param param The parameter NdArray
     */
    /**
     * addParameter方法。
     *      * @param name String类型参数
     * @param param NdArray类型参数
     */
    public void addParameter(String name, NdArray param) {
        parameters.put(name, param);
        gradients.put(name, null);
    }
    
    /**
     * Set the gradient for a parameter.
     * 
     * @param name Parameter name
     * @param gradient The gradient NdArray
     */
    /**
     * setGradient方法。
     *      * @param name String类型参数
     * @param gradient NdArray类型参数
     */
    public void setGradient(String name, NdArray gradient) {
        gradients.put(name, gradient);
    }
    
    /**
     * Zero the gradients for all parameters.
     */
    /**
     * zeroGrad方法。
     */
    public void zeroGrad() {
        for (String key : gradients.keySet()) {
            gradients.put(key, null);
        }
    }
    
    /**
     * Perform a single optimization step.
     * Subclasses must implement this method.
     */
    /**
     * step方法。
     * @return abstract void类型返回值
     */
    public abstract void step();
    
    /**
     * Perform a step with optional closure for algorithms that need multiple evaluations.
     * Default implementation just calls step().
     * 
     * @param closure Optional closure to compute loss
     */
    /**
     * step方法。
     *      * @param closure Runnable类型参数
     */
    public void step(Runnable closure) {
        step();
    }
    
    /**
     * Get the learning rate.
     */
    /**
     * getLearningRate方法。
     * @return double类型返回值
     */
    public double getLearningRate() {
        return learningRate;
    }
    
    /**
     * Set the learning rate.
     */
    /**
     * setLearningRate方法。
     *      * @param learningRate double类型参数
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
    
    /**
     * Get the weight decay coefficient.
     */
    /**
     * getWeightDecay方法。
     * @return double类型返回值
     */
    public double getWeightDecay() {
        return weightDecay;
    }
    
    /**
     * Set the weight decay coefficient.
     */
    /**
     * setWeightDecay方法。
     *      * @param weightDecay double类型参数
     */
    public void setWeightDecay(double weightDecay) {
        this.weightDecay = weightDecay;
    }
    
    /**
     * Get a parameter by name.
     */
    /**
     * getParameter方法。
     *      * @param name String类型参数
     * @return NdArray类型返回值
     */
    public NdArray getParameter(String name) {
        return parameters.get(name);
    }
    
    /**
     * Get all parameter names.
     */
    /**
     * getParameterNames方法。
     * @return Set<String>类型返回值
     */
    public Set<String> getParameterNames() {
        return parameters.keySet();
    }
    
    /**
     * Get optimizer state (for momentum, Adam buffers, etc.).
     */
    /**
     * getState方法。
     * @return Map<String, NdArray>类型返回值
     */
    protected Map<String, NdArray> getState() {
        return state;
    }
    
    /**
     * Clear the optimizer state.
     */
    /**
     * clearState方法。
     */
    public void clearState() {
        state.clear();
    }
    
    /**
     * Get the number of parameters.
     */
    /**
     * numParameters方法。
     * @return int类型返回值
     */
    public int numParameters() {
        return parameters.size();
    }
}
