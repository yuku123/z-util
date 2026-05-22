package com.zifang.util.ml.optim;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;

/**
 * Adam and AdamW (Adam with weight decay) optimizer.
 * 
 * AdamW decouples weight decay from the gradient-based update.
 * 
 * The update rule for Adam:
 *   m = beta1 * m + (1 - beta1) * grad
 *   v = beta2 * v + (1 - beta2) * grad^2
 *   m_hat = m / (1 - beta1^t)
 *   v_hat = v / (1 - beta2^t)
 *   param = param - lr * m_hat / (sqrt(v_hat) + eps)
 * 
 * For AdamW, weight decay is applied separately:
 *   param = param - lr * (weight_decay * param + m_hat / (sqrt(v_hat) + eps))
 */
public class Adam extends Optimizer {
    
    private double beta1;
    private double beta2;
    private double eps;
    private boolean adamw;
    private int timeStep;
    
    public Adam(double learningRate) {
        this(learningRate, 0.9, 0.999, 1e-8, false);
    }
    
    public Adam(double learningRate, double beta1, double beta2, double eps, boolean adamw) {
        super(learningRate, 0.0);
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.eps = eps;
        this.adamw = adamw;
        this.timeStep = 0;
    }
    
    public static Adam AdamW(double learningRate) {
        return new Adam(learningRate, 0.9, 0.999, 1e-8, true);
    }
    
    public static Adam AdamW(double learningRate, double beta1, double beta2, double eps) {
        return new Adam(learningRate, beta1, beta2, eps, true);
    }
    
    @Override
    public void step() {
        timeStep++;
        double biasCorrection1 = 1.0 - Math.pow(beta1, timeStep);
        double biasCorrection2 = 1.0 - Math.pow(beta2, timeStep);
        double biasCorrection1Root = Math.sqrt(biasCorrection2) / biasCorrection2;
        double lr = learningRate * Math.sqrt(biasCorrection2) / biasCorrection1;
        
        for (String name : parameters.keySet()) {
            NdArray param = parameters.get(name);
            NdArray grad = gradients.get(name);
            
            if (grad == null) {
                continue;
            }
            
            String mKey = "exp_avg_" + name;
            String vKey = "exp_avg_sq_" + name;
            
            NdArray m = state.get(mKey);
            NdArray v = state.get(vKey);
            
            if (m == null) {
                m = NdArray.zeros(param.getShape(), DType.FLOAT64);
                state.put(mKey, m);
            }
            if (v == null) {
                v = NdArray.zeros(param.getShape(), DType.FLOAT64);
                state.put(vKey, v);
            }
            
            // Update biased first moment estimate
            // m = beta1 * m + (1 - beta1) * grad
            NdArray oneMinusBeta1 = scalarOp(m, 1.0 - beta1);
            NdArray oneMinusBeta1TimesGrad = scalarOp(grad, 1.0 - beta1);
            NdArray newM = add(multiply(m, beta1), oneMinusBeta1TimesGrad);
            state.put(mKey, newM);
            
            // Update biased second raw moment estimate
            // v = beta2 * v + (1 - beta2) * grad^2
            NdArray gradSquared = multiply(grad, grad);
            NdArray oneMinusBeta2TimesGradSq = scalarOp(gradSquared, 1.0 - beta2);
            NdArray newV = add(multiply(v, beta2), oneMinusBeta2TimesGradSq);
            state.put(vKey, newV);
            
            // Compute bias-corrected estimates
            // m_hat = m / (1 - beta1^t)
            NdArray mHat = scalarOp(newM, 1.0 / biasCorrection1);
            // v_hat = v / (1 - beta2^t)
            NdArray vHat = scalarOp(newV, 1.0 / biasCorrection2);
            
            // Compute update
            // update = lr * m_hat / (sqrt(v_hat) + eps)
            NdArray vHatSqrt = sqrt(vHat);
            NdArray denom = addScalar(vHatSqrt, eps);
            NdArray update = scalarOp(mHat, lr / 1.0);
            NdArray finalUpdate = divide(update, denom);
            
            if (adamw && weightDecay > 0) {
                // AdamW: apply weight decay separately
                // param = param - lr * (weight_decay * param + update)
                NdArray weightDecayTerm = scalarOp(param, weightDecay);
                NdArray totalUpdate = add(weightDecayTerm, finalUpdate);
                param = subtract(param, scalarOp(totalUpdate, lr));
            } else {
                // Standard Adam (or AdamW with weight_decay=0)
                param = subtract(param, finalUpdate);
            }
            
            parameters.put(name, param);
        }
    }
    
    /**
     * Set beta1 coefficient.
     */
    public void setBeta1(double beta1) {
        this.beta1 = beta1;
    }
    
    /**
     * Get beta1 coefficient.
     */
    public double getBeta1() {
        return beta1;
    }
    
    /**
     * Set beta2 coefficient.
     */
    public void setBeta2(double beta2) {
        this.beta2 = beta2;
    }
    
    /**
     * Get beta2 coefficient.
     */
    public double getBeta2() {
        return beta2;
    }
    
    /**
     * Set epsilon for numerical stability.
     */
    public void setEps(double eps) {
        this.eps = eps;
    }
    
    /**
     * Get epsilon.
     */
    public double getEps() {
        return eps;
    }
    
    @Override
    public void clearState() {
        super.clearState();
        timeStep = 0;
    }
    
    // Helper methods for element-wise operations on NdArray
    private NdArray multiply(NdArray a, NdArray b) {
        return elementWiseOp2(a, b, (x, y) -> ((Number) x).doubleValue() * ((Number) y).doubleValue());
    }
    
    private NdArray multiply(NdArray a, double scalar) {
        return scalarOp(a, scalar);
    }
    
    private NdArray divide(NdArray a, NdArray b) {
        return elementWiseOp2(a, b, (x, y) -> ((Number) x).doubleValue() / ((Number) y).doubleValue());
    }
    
    private NdArray add(NdArray a, NdArray b) {
        return elementWiseOp2(a, b, (x, y) -> ((Number) x).doubleValue() + ((Number) y).doubleValue());
    }
    
    private NdArray subtract(NdArray a, NdArray b) {
        return elementWiseOp2(a, b, (x, y) -> ((Number) x).doubleValue() - ((Number) y).doubleValue());
    }
    
    private NdArray sqrt(NdArray a) {
        return elementWiseOp(a, 0, (x, s) -> Math.sqrt(((Number) x).doubleValue()));
    }
    
    private NdArray scalarOp(NdArray input, double scalar) {
        return elementWiseOp(input, scalar, (x, s) -> ((Number) x).doubleValue() * s);
    }
    
    private NdArray addScalar(NdArray a, double scalar) {
        return elementWiseOp(a, scalar, (x, s) -> ((Number) x).doubleValue() + s);
    }
    
    private NdArray elementWiseOp(NdArray input, double scalar, Op op) {
        Object data = input.getData();
        DType dtype = input.getDtype();
        int size = input.size();
        
        if (data instanceof double[]) {
            double[] arr = (double[]) data;
            double[] result = new double[size];
            for (int i = 0; i < size; i++) {
                result[i] = op.apply(arr[i], scalar);
            }
            return NdArray.create(result, dtype, input.getShape());
        }
        
        throw new IllegalArgumentException("Unsupported dtype: " + dtype);
    }
    
    private NdArray elementWiseOp2(NdArray a, NdArray b, Op2 op) {
        Object dataA = a.getData();
        Object dataB = b.getData();
        DType dtype = a.getDtype();
        int size = a.size();
        
        if (dataA instanceof double[] && dataB instanceof double[]) {
            double[] arrA = (double[]) dataA;
            double[] arrB = (double[]) dataB;
            double[] result = new double[size];
            for (int i = 0; i < size; i++) {
                result[i] = op.apply(arrA[i], arrB[i]);
            }
            return NdArray.create(result, dtype, a.getShape());
        }
        
        throw new IllegalArgumentException("Unsupported dtype combination");
    }
    
    @FunctionalInterface
    private interface Op {
        double apply(double x, double s);
    }
    
    @FunctionalInterface
    private interface Op2 {
        double apply(double x, double y);
    }
}
