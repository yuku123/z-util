package com.zifang.util.ml.nn;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;

/**
 * 2D Batch Normalization layer
 * Normalizes inputs across batch dimension
 * Input: (batchSize, channels, height, width)
 * Output: (batchSize, channels, height, width)
 */
public class BatchNorm2d extends Module {
    
    private final int numFeatures;
    private final float eps;
    private final float momentum;
    
    private NdArray gamma;  // Scale parameter
    private NdArray beta;   // Shift parameter
    private NdArray runningMean;
    private NdArray runningVar;
    
    private NdArray savedMean;
    private NdArray savedVar;
    private NdArray savedInput;
    
    public BatchNorm2d(int numFeatures) {
        this(numFeatures, 1e-5f, 0.1f);
    }
    
    public BatchNorm2d(int numFeatures, float eps, float momentum) {
        this.numFeatures = numFeatures;
        this.eps = eps;
        this.momentum = momentum;
        
        gamma = NdArray.ones(new Shape(numFeatures), DType.FLOAT32);
        beta = NdArray.zeros(new Shape(numFeatures), DType.FLOAT32);
        runningMean = NdArray.zeros(new Shape(numFeatures), DType.FLOAT32);
        runningVar = NdArray.ones(new Shape(numFeatures), DType.FLOAT32);
        
        registerParameter("gamma", gamma);
        registerParameter("beta", beta);
    }
    
    @Override
    public NdArray forward(NdArray input) {
        if (input.ndim() != 4) {
            throw new IllegalArgumentException("Input must be 4D: (batchSize, channels, height, width)");
        }
        
        int batchSize = input.getShape().get(0);
        int channels = input.getShape().get(1);
        int height = input.getShape().get(2);
        int width = input.getShape().get(3);
        int size = batchSize * channels * height * width;
        
        savedInput = input.copy();
        
        NdArray output = NdArray.zeros(input.getShape(), DType.FLOAT32);
        Object inData = input.getData();
        Object outData = output.getData();
        Object gData = gamma.getData();
        Object bData = beta.getData();
        Object rmData = runningMean.getData();
        Object rvData = runningVar.getData();
        
        if (training) {
            // Compute batch mean: E[x] = mean over batch
            float[] batchMean = new float[channels];
            for (int c = 0; c < channels; c++) {
                float sum = 0.0f;
                for (int b = 0; b < batchSize; b++) {
                    for (int h = 0; h < height; h++) {
                        for (int w = 0; w < width; w++) {
                            int idx = ((b * channels + c) * height + h) * width + w;
                            float val = (float) com.zifang.util.numpy.Array.get(inData, idx);
                            sum += val;
                        }
                    }
                }
                batchMean[c] = sum / (batchSize * height * width);
            }
            
            // Compute batch variance: Var[x] = mean((x - E[x])^2)
            float[] batchVar = new float[channels];
            for (int c = 0; c < channels; c++) {
                float sum = 0.0f;
                for (int b = 0; b < batchSize; b++) {
                    for (int h = 0; h < height; h++) {
                        for (int w = 0; w < width; w++) {
                            int idx = ((b * channels + c) * height + h) * width + w;
                            float val = (float) com.zifang.util.numpy.Array.get(inData, idx);
                            float diff = val - batchMean[c];
                            sum += diff * diff;
                        }
                    }
                }
                batchVar[c] = sum / (batchSize * height * width);
            }
            
            // Save for backward
            savedMean = NdArray.array(batchMean, DType.FLOAT32);
            savedVar = NdArray.array(batchVar, DType.FLOAT32);
            
            // Update running statistics
            for (int c = 0; c < channels; c++) {
                float rm = (float) com.zifang.util.numpy.Array.get(rmData, c);
                float rv = (float) com.zifang.util.numpy.Array.get(rvData, c);
                com.zifang.util.numpy.Array.set(rmData, c, momentum * rm + (1 - momentum) * batchMean[c]);
                com.zifang.util.numpy.Array.set(rvData, c, momentum * rv + (1 - momentum) * batchVar[c]);
            }
            
            // Normalize and scale
            for (int c = 0; c < channels; c++) {
                float g = (float) com.zifang.util.numpy.Array.get(gData, c);
                float b = (float) com.zifang.util.numpy.Array.get(bData, c);
                float std = (float) Math.sqrt(batchVar[c] + eps);
                
                for (int bb = 0; bb < batchSize; bb++) {
                    for (int h = 0; h < height; h++) {
                        for (int w = 0; w < width; w++) {
                            int idx = ((bb * channels + c) * height + h) * width + w;
                            float val = (float) com.zifang.util.numpy.Array.get(inData, idx);
                            float normalized = (val - batchMean[c]) / std;
                            float outVal = g * normalized + b;
                            com.zifang.util.numpy.Array.set(outData, idx, outVal);
                        }
                    }
                }
            }
        } else {
            // Inference mode: use running statistics
            for (int c = 0; c < channels; c++) {
                float g = (float) com.zifang.util.numpy.Array.get(gData, c);
                float b = (float) com.zifang.util.numpy.Array.get(bData, c);
                float rm = (float) com.zifang.util.numpy.Array.get(rmData, c);
                float rv = (float) com.zifang.util.numpy.Array.get(rvData, c);
                float std = (float) Math.sqrt(rv + eps);
                
                for (int bb = 0; bb < batchSize; bb++) {
                    for (int h = 0; h < height; h++) {
                        for (int w = 0; w < width; w++) {
                            int idx = ((bb * channels + c) * height + h) * width + w;
                            float val = (float) com.zifang.util.numpy.Array.get(inData, idx);
                            float normalized = (val - rm) / std;
                            float outVal = g * normalized + b;
                            com.zifang.util.numpy.Array.set(outData, idx, outVal);
                        }
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
        int height = gradOutput.getShape().get(2);
        int width = gradOutput.getShape().get(3);
        
        Object gOutData = gradOutput.getData();
        Object inData = savedInput.getData();
        
        float[] mean = new float[savedMean.size()];
        Object mData = savedMean.getData();
        for (int i = 0; i < mean.length; i++) {
            mean[i] = (float) com.zifang.util.numpy.Array.get(mData, i);
        }
        
        float[] var = new float[savedVar.size()];
        Object vData = savedVar.getData();
        for (int i = 0; i < var.length; i++) {
            var[i] = (float) com.zifang.util.numpy.Array.get(vData, i);
        }
        
        // Gradient w.r.t. gamma and beta
        Object gData = gamma.getData();
        Object bData = beta.getData();
        
        float[] gammaGrad = new float[channels];
        float[] betaGrad = new float[channels];
        
        for (int c = 0; c < channels; c++) {
            float gg = 0.0f;
            float bg = 0.0f;
            float std = (float) Math.sqrt(var[c] + eps);
            
            for (int b = 0; b < batchSize; b++) {
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int idx = ((b * channels + c) * height + h) * width + w;
                        float xNorm = ((float) com.zifang.util.numpy.Array.get(inData, idx) - mean[c]) / std;
                        float dOut = (float) com.zifang.util.numpy.Array.get(gOutData, idx);
                        gg += dOut * xNorm;
                        bg += dOut;
                    }
                }
            }
            gammaGrad[c] = gg;
            betaGrad[c] = bg;
        }
        
        // Gradient w.r.t. input
        NdArray gradInput = NdArray.zeros(savedInput.getShape(), DType.FLOAT32);
        Object gInData = gradInput.getData();
        Object gmData = gamma.getData();
        
        for (int c = 0; c < channels; c++) {
            float g = (float) com.zifang.util.numpy.Array.get(gmData, c);
            float std = (float) Math.sqrt(var[c] + eps);
            float varGrad = 0.0f;
            float meanGrad = 0.0f;
            
            // Compute gradient w.r.t. variance and mean
            for (int b = 0; b < batchSize; b++) {
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int idx = ((b * channels + c) * height + h) * width + w;
                        float x = (float) com.zifang.util.numpy.Array.get(inData, idx);
                        float xCentered = x - mean[c];
                        float dOut = (float) com.zifang.util.numpy.Array.get(gOutData, idx);
                        meanGrad -= dOut * g / std;
                        varGrad -= dOut * g * xCentered / (std * std * std);
                    }
                }
            }
            
            meanGrad /= 2 * std;
            
            // Apply gradient
            int n = batchSize * height * width;
            for (int b = 0; b < batchSize; b++) {
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        int idx = ((b * channels + c) * height + h) * width + w;
                        float x = (float) com.zifang.util.numpy.Array.get(inData, idx);
                        float xCentered = x - mean[c];
                        float dOut = (float) com.zifang.util.numpy.Array.get(gOutData, idx);
                        float dXNorm = dOut * g / std;
                        float dVar = varGrad * xCentered;
                        float dMean = meanGrad / n;
                        
                        float grad = dXNorm + (dVar * 2 * xCentered / n) + dMean;
                        com.zifang.util.numpy.Array.set(gInData, idx, grad);
                    }
                }
            }
        }
        
        return gradInput;
    }
    
    public NdArray getRunningMean() { return runningMean; }
    public NdArray getRunningVar() { return runningVar; }
    public NdArray getGamma() { return gamma; }
    public NdArray getBeta() { return beta; }
}
