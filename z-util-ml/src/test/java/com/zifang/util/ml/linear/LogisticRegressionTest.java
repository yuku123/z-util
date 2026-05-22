package com.zifang.util.ml.linear;

import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class LogisticRegressionTest {

    private Random random = new Random(42);

    private NdArray createNdArray(double[][] data, int rows, int cols) {
        double[] flat = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flat[i * cols + j] = data[i][j];
            }
        }
        return NdArray.array(flat, DType.FLOAT32).reshape(rows, cols);
    }

    /**
     * Generate linearly separable data (two classes separated by x > 0).
     */
    private NdArray generateLinearlySeparableData(int nSamples) {
        double[][] data = new double[nSamples][2];
        for (int i = 0; i < nSamples; i++) {
            data[i][0] = random.nextDouble() * 10 - 5;
            data[i][1] = random.nextDouble() * 10 - 5;
        }
        return createNdArray(data, nSamples, 2);
    }

    private int[] generateLinearlySeparableLabels(int nSamples) {
        int[] labels = new int[nSamples];
        for (int i = 0; i < nSamples; i++) {
            double x = random.nextDouble() * 10 - 5;
            labels[i] = (x > 0) ? 1 : 0;
        }
        return labels;
    }

    @Test
    public void testLogisticRegressionBinary() {
        int nSamples = 100;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        LogisticRegression lr = new LogisticRegression(0.1, 1000, 0.01);
        lr.fit(X, y);
        
        int[] predictions = lr.predict(X);
        
        assertEquals(nSamples, predictions.length);
        
        // Calculate accuracy
        int correct = 0;
        for (int i = 0; i < nSamples; i++) {
            if (predictions[i] == y[i]) {
                correct++;
            }
        }
        double accuracy = (double) correct / nSamples;
        assertTrue(accuracy > 0.9, "Accuracy should be > 0.9 for linearly separable data, got: " + accuracy);
    }

    @Test
    public void testLogisticRegressionProba() {
        int nSamples = 50;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        LogisticRegression lr = new LogisticRegression(0.1, 500, 0.01);
        lr.fit(X, y);
        
        NdArray proba = lr.predictProba(X);
        
        assertEquals(nSamples, proba.getShape().get(0));
        assertEquals(2, proba.getShape().get(1));
        
        // Verify probabilities are valid and sum to 1
        for (int i = 0; i < nSamples; i++) {
            double p0 = ((Number) proba.get(i, 0)).doubleValue();
            double p1 = ((Number) proba.get(i, 1)).doubleValue();
            
            assertTrue(p0 >= 0 && p0 <= 1, "p0 should be in [0,1]");
            assertTrue(p1 >= 0 && p1 <= 1, "p1 should be in [0,1]");
            assertEquals(1.0, p0 + p1, 1e-5, "Probabilities should sum to 1");
        }
    }

    @Test
    public void testLogisticRegressionL2Regularization() {
        int nSamples = 60;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        // Different regularization strengths
        LogisticRegression lrLowReg = new LogisticRegression(0.1, 500, 0.001);
        LogisticRegression lrHighReg = new LogisticRegression(0.1, 500, 10.0);
        
        lrLowReg.fit(X, y);
        lrHighReg.fit(X, y);
        
        int[] predLow = lrLowReg.predict(X);
        int[] predHigh = lrHighReg.predict(X);
        
        assertEquals(nSamples, predLow.length);
        assertEquals(nSamples, predHigh.length);
        
        // Both should achieve reasonable accuracy
        int correctLow = 0, correctHigh = 0;
        for (int i = 0; i < nSamples; i++) {
            if (predLow[i] == y[i]) correctLow++;
            if (predHigh[i] == y[i]) correctHigh++;
        }
        
        assertTrue(correctLow > 0);
        assertTrue(correctHigh > 0);
    }
}
