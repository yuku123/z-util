package com.zifang.util.ml.nn.activations;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;

/**
 * Tanh (Hyperbolic Tangent) activation function
 * f(x) = tanh(x) = (exp(x) - exp(-x)) / (exp(x) + exp(-x))
 * f'(x) = 1 - tanh^2(x)
 */
public class Tanh extends com.zifang.util.ml.nn.Module {
    
    private NdArray savedOutput;  // Store tanh(x) for backward pass
    
    @Override
    public NdArray forward(NdArray input) {
        NdArray output = NdArray.zeros(input.getShape(), DType.FLOAT32);
        Object inData = input.getData();
        Object outData = output.getData();
        int size = input.size();
        
        for (int i = 0; i < size; i++) {
            float x = (float) com.zifang.util.numpy.Array.get(inData, i);
            float result = (float) Math.tanh(x);
            com.zifang.util.numpy.Array.set(outData, i, result);
        }
        
        savedOutput = output.copy();
        return output;
    }
    
    @Override
    public NdArray backward(NdArray gradOutput) {
        NdArray gradInput = NdArray.zeros(gradOutput.getShape(), DType.FLOAT32);
        Object gInData = gradInput.getData();
        Object gOutData = gradOutput.getData();
        Object tanhData = savedOutput.getData();
        int size = gradOutput.size();
        
        for (int i = 0; i < size; i++) {
            float tanhVal = (float) com.zifang.util.numpy.Array.get(tanhData, i);
            float gOut = (float) com.zifang.util.numpy.Array.get(gOutData, i);
            
            // d/dx tanh(x) = 1 - tanh^2(x)
            float dx = 1.0f - tanhVal * tanhVal;
            
            com.zifang.util.numpy.Array.set(gInData, i, gOut * dx);
        }
        
        return gradInput;
    }
}
