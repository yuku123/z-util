package com.zifang.util.ml.optim;

import com.zifang.util.numpy.NdArray;

/**
 * Represents a single parameter with its gradient for optimization.
 */
public class Parameter {
    private final String name;
    private final NdArray data;
    private NdArray gradient;

    public Parameter(String name, NdArray data) {
        this.name = name;
        this.data = data;
        this.gradient = null;
    }

    public String getName() {
        return name;
    }

    public NdArray getData() {
        return data;
    }

    public NdArray getGradient() {
        return gradient;
    }

    public void setGradient(NdArray gradient) {
        this.gradient = gradient;
    }

    public NdArray getData(NdArray grad) {
        return data;
    }
}
