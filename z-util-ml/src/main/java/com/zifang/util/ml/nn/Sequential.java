package com.zifang.util.ml.nn;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequential container that chains modules in order.
 * Data flows through each module sequentially.
 */
public class Sequential extends Module {
    
    private final List<Module> modules = new ArrayList<>();
    private final List<NdArray> intermediateInputs = new ArrayList<>();
    
    public Sequential(Module... modules) {
        for (Module module : modules) {
            add(module);
        }
    }
    
    /**
     * Adds a module to the sequential container
     */
    public void add(Module module) {
        modules.add(module);
        for (NdArray param : module.parameters()) {
            registerParameter("sequential_param", param);
        }
    }
    
    @Override
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
    public void train() {
        super.train();
        for (Module module : modules) {
            module.train();
        }
    }
    
    @Override
    public void eval() {
        super.eval();
        for (Module module : modules) {
            module.eval();
        }
    }
    
    /**
     * Returns the number of modules in the sequential container
     */
    public int size() {
        return modules.size();
    }
    
    /**
     * Returns the module at the given index
     */
    public Module get(int index) {
        return modules.get(index);
    }
}
