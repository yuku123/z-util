package com.zifang.util.ml.nn.activations;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;

/**
 * Softplus activation function
 * f(x) = log(1 + exp(x))
 * f'(x) = sigmoid(x) = 1 / (1 + exp(-x))
 */
public class Softplus extends com.zifang.util.ml.nn.Module {
    
    private NdArray savedInput;
    
    @Override
    public NdArray forward(NdArray input) {
        savedInput = input.copy();
        
        NdArray output = NdArray.zeros(input.getShape(), DType.FLOAT32);
        Object inData = input.getData();
        Object outData = output.getData();
        int size = input.size();
        
        for (int i = 0; i < size; i++) {
            float x = (float) com.zifang.util.numpy.Array.get(inData, i);
            // softplus(x) = log(1 + exp(x))
            // Use numerical stability: if x > 20, return x; if x < -20, return exp(x)
            float result;
            if (x > 20) {
                result = x;
            } else if (x < -20) {
                result = (float) Math.exp(x);
            } else {
                result = (float) Math.log(1.0 + Math.exp(x));
            }
            com.zifang.util.numpy.Array.set(outData, i, result);
        }
        
        return output;
    }
    
    @Override
    public NdArray backward(NdArray gradOutput) {
        NdArray gradInput = NdArray.zeros(gradOutput.getShape(), DType.FLOAT32);
        Object gInData = gradInput.getData();
        Object gOutData = gradOutput.getData();
        Object inData = savedInput.getData();
        int size = gradOutput.size();
        
        for (int i = 0; i < size; i++) {
            float x = (float) com.zifang.util.numpy.Array.get(inData, i);
            float gOut = (float) com.zifang.util.numpy.Array.get(gOutData, i);
            
            // d/dx log(1 + exp(x)) = exp(x) / (1 + exp(x)) = sigmoid(x)
            float sigmoid;
            if (x < -20) {
                sigmoid = (float) Math.exp(x);
            } else if (x > 20) {
                sigmoid = 1.0f;
            } else {
                sigmoid = (float) (1.0 / (1.0 + Math.exp(-x)));
            }
            
            com.zifang.util.numpy.Array.set(gInData, i, gOut * sigmoid);
        }
        
        return gradInput;
    }
}
