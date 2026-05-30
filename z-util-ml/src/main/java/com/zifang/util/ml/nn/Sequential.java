package com.zifang.util.ml.nn;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequential container that chains modules in order.
 * Data flows through each module sequentially.
 */
/**
 * Sequential类。
 */
public class Sequential extends Module {
    
    private final List<Module> modules = new ArrayList<>();
    private final List<NdArray> intermediateInputs = new ArrayList<>();
    
    /**
     * Sequential方法。
     *      * @param modules Module...类型参数
     */
    public Sequential(Module... modules) {
        for (Module module : modules) {
            add(module);
        }
    }
    
    /**
     * Adds a module to the sequential container
     */
    /**
     * add方法。
     *      * @param module Module类型参数
     */
    public void add(Module module) {
        modules.add(module);
        for (NdArray param : module.parameters()) {
            registerParameter("sequential_param", param);
        }
    }
    
    @Override
    /**
     * forward方法。
     *      * @param input NdArray类型参数
     * @return NdArray类型返回值
     */
    public NdArray forward(NdArray input) {
        intermediateInputs.clear();
        NdArray current = input;
        intermediateInputs.add(current.copy());
        
        for (Module module : modules) {
            current = module.forward(current);
            intermediateInputs.add(current.copy());
        }
        return current;
    }
    
    @Override
    /**
     * backward方法。
     *      * @param gradOutput NdArray类型参数
     * @return NdArray类型返回值
     */
    public NdArray backward(NdArray gradOutput) {
        NdArray grad = gradOutput;
        
        // Backward through modules in reverse order
        for (int i = modules.size() - 1; i >= 0; i--) {
            Module module = modules.get(i);
            grad = module.backward(grad);
        }
        return grad;
    }
    
    @Override
    /**
     * train方法。
     */
    public void train() {
        super.train();
        for (Module module : modules) {
            module.train();
        }
    }
    
    @Override
    /**
     * eval方法。
     */
    public void eval() {
        super.eval();
        for (Module module : modules) {
            module.eval();
        }
    }
    
    /**
     * Returns the number of modules in the sequential container
     */
    /**
     * size方法。
     * @return int类型返回值
     */
    public int size() {
        return modules.size();
    }
    
    /**
     * Returns the module at the given index
     */
    /**
     * get方法。
     *      * @param index int类型参数
     * @return Module类型返回值
     */
    public Module get(int index) {
        return modules.get(index);
    }
}
