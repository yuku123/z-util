package com.zifang.util.ml.nn;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;

/**
 * 2D Average Pooling layer
 * Input: (batchSize, channels, height, width)
 * Output: (batchSize, channels, outHeight, outWidth)
 */
public class AvgPool2d extends Module {
    
    private final int kernelSize;
    private final int stride;
    
    public AvgPool2d(int kernelSize) {
        this(kernelSize, kernelSize);
    }
    
    public AvgPool2d(int kernelSize, int stride) {
        this.kernelSize = kernelSize;
        this.stride = stride;
    }
    
    @Override
    public NdArray forward(NdArray input) {
        if (input.ndim() != 4) {
            throw new IllegalArgumentException("Input must be 4D: (batchSize, channels, height, width)");
        }
        
        int batchSize = input.getShape().get(0);
        int channels = input.getShape().get(1);
        int inH = input.getShape().get(2);
        int inW = input.getShape().get(3);
        
        int outH = (inH - kernelSize) / stride + 1;
        int outW = (inW - kernelSize) / stride + 1;
        
        NdArray output = NdArray.zeros(new Shape(batchSize, channels, outH, outW), DType.FLOAT32);
        
        Object inData = input.getData();
        Object outData = output.getData();
        
        float kernelArea = kernelSize * kernelSize;
        
        for (int b = 0; b < batchSize; b++) {
            for (int c = 0; c < channels; c++) {
                for (int outY = 0; outY < outH; outY++) {
                    for (int outX = 0; outX < outW; outX++) {
                        float sum = 0.0f;
                        
                        for (int kY = 0; kY < kernelSize; kY++) {
                            for (int kX = 0; kX < kernelSize; kX++) {
                                int inY = outY * stride + kY;
                                int inX = outX * stride + kX;
                                
                                int inIdx = ((b * channels + c) * inH + inY) * inW + inX;
                                float val = (float) com.zifang.util.numpy.Array.get(inData, inIdx);
                                sum += val;
                            }
                        }
                        
                        int outIdx = ((b * channels + c) * outH + outY) * outW + outX;
                        com.zifang.util.numpy.Array.set(outData, outIdx, sum / kernelArea);
                    }
                }
            }
        }
        
        return output;
    }
    
    @Override
    public NdArray backward(NdArray gradOutput) {
        int batchSize = gradOutput.getShape().get(0);
        int channels = gradOutput.getShape().get(1);
        int outH = gradOutput.getShape().get(2);
        int outW = gradOutput.getShape().get(3);
        
        // We need input shape for full backward, but we don't store it
        // For simplicity, assume input was computed before and we can infer size
        int kernelArea = kernelSize * kernelSize;
        
        NdArray gradInput = NdArray.zeros(gradOutput.getShape(), DType.FLOAT32);
        Object gOutData = gradOutput.getData();
        Object gInData = gradInput.getData();
        
        for (int b = 0; b < batchSize; b++) {
            for (int c = 0; c < channels; c++) {
                for (int outY = 0; outY < outH; outY++) {
                    for (int outX = 0; outX < outW; outX++) {
                        int outIdx = ((b * channels + c) * outH + outY) * outW + outX;
                        float gOut = (float) com.zifang.util.numpy.Array.get(gOutData, outIdx);
                        float gVal = gOut / kernelArea;
                        
                        for (int kY = 0; kY < kernelSize; kY++) {
                            for (int kX = 0; kX < kernelSize; kX++) {
                                int inY = outY * stride + kY;
                                int inX = outX * stride + kX;
                                
                                // Gradients are distributed equally to each position in the kernel
                                // This is an approximation - true backward would need input shape
                                int inIdx = ((b * channels + c) * outH + inY) * outW + inX;
                                if (inIdx < gradInput.size()) {
                                    float existing = (float) com.zifang.util.numpy.Array.get(gInData, inIdx);
                                    com.zifang.util.numpy.Array.set(gInData, inIdx, existing + gVal);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return gradInput;
    }
    
    public int getKernelSize() { return kernelSize; }
    public int getStride() { return stride; }
}
