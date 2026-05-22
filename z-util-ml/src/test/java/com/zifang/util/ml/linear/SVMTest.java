package com.zifang.util.ml.linear;

import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SVMTest {

    private Random random = new Random(42);

    private NdArray createNdArray(double[][] data, int rows, int cols) {
        double[] flat = new double[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                flat[i * cols + j] = data[i][j];
            }
        }
        return NdArray.array(flat, DType.FLOAT64).reshape(rows, cols);
    }

    /**
     * Generate linearly separable data with labels +1 and -1.
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
            labels[i] = (x > 0) ? 1 : -1;
        }
        return labels;
    }

    @Test
    public void testSVMLinear() {
        int nSamples = 100;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        SVM svm = new SVM(0.01, 1000, 0.1, SVM.KernelType.LINEAR);
        svm.fit(X, y);
        
        int[] predictions = svm.predict(X);
        
        assertEquals(nSamples, predictions.length);
        
        // All predictions should be +1 or -1
        for (int pred : predictions) {
            assertTrue(pred == 1 || pred == -1, "Predictions should be +1 or -1");
        }
        
        // Calculate accuracy
        int correct = 0;
        for (int i = 0; i < nSamples; i++) {
            if (predictions[i] == y[i]) {
                correct++;
            }
        }
        double accuracy = (double) correct / nSamples;
        assertTrue(accuracy > 0.85, "Linear SVM accuracy should be > 0.85, got: " + accuracy);
    }

    @Test
    public void testSVMRBF() {
        // Create circular data where linear SVM would struggle
        int nSamples = 60;
        double[][] data = new double[nSamples][2];
        int[] labels = new int[nSamples];
        
        for (int i = 0; i < nSamples; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double radius = random.nextDouble() * 2;
            
            if (i < nSamples / 2) {
                // Inner circle - label -1
                data[i][0] = radius * Math.cos(angle);
                data[i][1] = radius * Math.sin(angle);
                labels[i] = -1;
            } else {
                // Outer circle - label +1
                data[i][0] = (radius + 3) * Math.cos(angle);
                data[i][1] = (radius + 3) * Math.sin(angle);
                labels[i] = 1;
            }
        }
        
        NdArray X = createNdArray(data, nSamples, 2);
        
        SVM svm = new SVM(0.01, 500, 0.1, SVM.KernelType.RBF);
        svm.setGamma(0.1);
        svm.fit(X, labels);
        
        int[] predictions = svm.predict(X);
        
        assertEquals(nSamples, predictions.length);
        
        // All predictions should be +1 or -1
        for (int pred : predictions) {
            assertTrue(pred == 1 || pred == -1);
        }
    }

    @Test
    public void testSVMPredict() {
        int nSamples = 40;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        SVM svm = new SVM(0.01, 500, 0.1, SVM.KernelType.LINEAR);
        svm.fit(X, y);
        
        int[] predictions = svm.predict(X);
        
        assertEquals(nSamples, predictions.length);
        
        for (int pred : predictions) {
            assertTrue(pred == 1 || pred == -1, "Predictions should be +1 or -1");
        }
    }
}
