package com.zifang.util.ml.nn;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all neural network modules.
 * Provides forward/backward propagation and parameter management.
 */
public abstract class Module {
    
    protected boolean training = true;
    /**
     * ArrayList<>方法。
     * @return List<NdArray> parameters = new类型返回值
     */
    protected List<NdArray> parameters = new ArrayList<>();
    
    /**
     * Forward pass - computes output from input
     */
    /**
     * forward方法。
     *      * @param input NdArray类型参数
     * @return abstract NdArray类型返回值
     */
    public abstract NdArray forward(NdArray input);
    
    /**
     * Backward pass - computes gradient with respect to input
     * @param gradOutput Gradient from upstream (dL/doutput)
     * @return Gradient with respect to input (dL/dinput)
     */
    /**
     * backward方法。
     *      * @param gradOutput NdArray类型参数
     * @return abstract NdArray类型返回值
     */
    public abstract NdArray backward(NdArray gradOutput);
    
    /**
     * Sets the module to training mode
     */
    /**
     * train方法。
     */
    public void train() {
        this.training = true;
    }
    
    /**
     * Sets the module to evaluation mode
     */
    /**
     * eval方法。
     */
    public void eval() {
        this.training = false;
    }
    
    /**
     * Returns whether the module is in training mode
     */
    /**
     * isTraining方法。
     * @return boolean类型返回值
     */
    public boolean isTraining() {
        return training;
    }
    
    /**
     * Returns all parameters of this module
     */
    /**
     * parameters方法。
     * @return List<NdArray>类型返回值
     */
    public List<NdArray> parameters() {
        return parameters;
    }
    
    /**
     * Registers a parameter tensor
     */
    /**
     * registerParameter方法。
     *      * @param name String类型参数
     * @param param NdArray类型参数
     */
    protected void registerParameter(String name, NdArray param) {
        parameters.add(param);
    }
    
    /**
     * Creates a zero-initialized parameter tensor with given shape
     */
    /**
     * createParameter方法。
     *      * @param shape int...类型参数
     * @return NdArray类型返回值
     */
    protected NdArray createParameter(int... shape) {
        NdArray param = NdArray.zeros(new Shape(shape), DType.FLOAT32);
        parameters.add(param);
        return param;
    }
    
    /**
     * Applies Xavier/Glorot uniform initialization
     */
    /**
     * initXavierUniform方法。
     *      * @param tensor NdArray类型参数
     * @param fanIn int类型参数
     * @param fanOut int类型参数
     * @return NdArray类型返回值
     */
    protected NdArray initXavierUniform(NdArray tensor, int fanIn, int fanOut) {
        double limit = Math.sqrt(6.0 / (fanIn + fanOut));
        Object data = tensor.getData();
        int size = tensor.size();
        for (int i = 0; i < size; i++) {
            double value = (Math.random() * 2 - 1) * limit;
            com.zifang.util.numpy.Array.set(data, i, value);
        }
        return tensor;
    }
    
    /**
     * Applies Xavier/Glorot normal initialization
     */
    /**
     * initXavierNormal方法。
     *      * @param tensor NdArray类型参数
     * @param fanIn int类型参数
     * @param fanOut int类型参数
     * @return NdArray类型返回值
     */
    protected NdArray initXavierNormal(NdArray tensor, int fanIn, int fanOut) {
        double std = Math.sqrt(2.0 / (fanIn + fanOut));
        Object data = tensor.getData();
        int size = tensor.size();
        for (int i = 0; i < size; i++) {
            // Box-Muller transform for Gaussian
            double u1 = Math.random();
            double u2 = Math.random();
            double value = std * Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
            com.zifang.util.numpy.Array.set(data, i, value);
        }
        return tensor;
    }
    
    /**
     * Applies Kaiming/He normal initialization
     */
    /**
     * initKaimingNormal方法。
     *      * @param tensor NdArray类型参数
     * @param fanIn int类型参数
     * @return NdArray类型返回值
     */
    protected NdArray initKaimingNormal(NdArray tensor, int fanIn) {
        double std = Math.sqrt(2.0 / fanIn);
        Object data = tensor.getData();
        int size = tensor.size();
        for (int i = 0; i < size; i++) {
            double u1 = Math.random();
            double u2 = Math.random();
            double value = std * Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
            com.zifang.util.numpy.Array.set(data, i, value);
        }
        return tensor;
    }
    
    /**
     * Resets all parameters to zero
     */
    /**
     * zeroGrad方法。
     */
    public void zeroGrad() {
        for (NdArray param : parameters) {
            Object data = param.getData();
            int size = param.size();
            for (int i = 0; i < size; i++) {
                com.zifang.util.numpy.Array.set(data, i, 0.0f);
            }
        }
    }
}
