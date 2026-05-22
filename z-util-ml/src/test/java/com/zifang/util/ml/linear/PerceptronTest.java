package com.zifang.util.ml.linear;

import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.Shape;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PerceptronTest {

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
    public void testPerceptronConvergence() {
        int nSamples = 80;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        Perceptron perceptron = new Perceptron(0.1, 100);
        perceptron.fit(X, y);
        
        int[] predictions = perceptron.predict(X);
        
        assertEquals(nSamples, predictions.length);
        
        // For linearly separable data, perceptron should achieve good accuracy
        int correct = 0;
        for (int i = 0; i < nSamples; i++) {
            if (predictions[i] == y[i]) {
                correct++;
            }
        }
        double accuracy = (double) correct / nSamples;
        assertTrue(accuracy > 0.8, "Perceptron should converge on linearly separable data");
    }

    @Test
    public void testPerceptronPredict() {
        int nSamples = 50;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        Perceptron perceptron = new Perceptron(0.1, 50);
        perceptron.fit(X, y);
        
        int[] predictions = perceptron.predict(X);
        
        assertEquals(nSamples, predictions.length);
        
        // All predictions should be 0 or 1
        for (int pred : predictions) {
            assertTrue(pred == 0 || pred == 1);
        }
    }

    @Test
    public void testPerceptronScore() {
        int nSamples = 60;
        NdArray X = generateLinearlySeparableData(nSamples);
        int[] y = generateLinearlySeparableLabels(nSamples);
        
        Perceptron perceptron = new Perceptron(0.1, 100);
        perceptron.fit(X, y);
        
        double score = perceptron.score(X, y);
        
        assertTrue(score >= 0.0 && score <= 1.0, "Score should be between 0 and 1, got: " + score);
        assertTrue(score > 0.5, "Score should be better than random for linearly separable data");
    }
}
