package com.zifang.util.ai.nnet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * MSELoss 类测试
 */
public class MSELossTest {

    @Test
    public void testComputePerfectPrediction() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 2.0, 3.0};
        double[] targets = {1.0, 2.0, 3.0};

        double loss = mseLoss.compute(predictions, targets);

        assertEquals(0.0, loss, 0.0001);
    }

    @Test
    public void testComputeWithError() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 2.0, 3.0};
        double[] targets = {2.0, 3.0, 4.0};

        double loss = mseLoss.compute(predictions, targets);

        // Each element has error of 1.0, squared is 1.0, mean is 1.0
        assertEquals(1.0, loss, 0.0001);
    }

    @Test
    public void testComputeSingleElement() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {5.0};
        double[] targets = {3.0};

        double loss = mseLoss.compute(predictions, targets);

        // Error = 2.0, squared = 4.0, mean = 4.0
        assertEquals(4.0, loss, 0.0001);
    }

    @Test
    public void testComputeLargeError() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {10.0};
        double[] targets = {0.0};

        double loss = mseLoss.compute(predictions, targets);

        // Error = 10.0, squared = 100.0
        assertEquals(100.0, loss, 0.0001);
    }

    @Test
    public void testComputeNegativeValues() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {-1.0, -2.0, -3.0};
        double[] targets = {1.0, 2.0, 3.0};

        double loss = mseLoss.compute(predictions, targets);

        // Each error = -2.0, squared = 4.0, mean = 4.0
        assertEquals(4.0, loss, 0.0001);
    }

    @Test
    public void testComputeZeroPredictions() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {0.0, 0.0, 0.0};
        double[] targets = {1.0, 2.0, 3.0};

        double loss = mseLoss.compute(predictions, targets);

        // Errors = -1, -2, -3, squared = 1, 4, 9, mean = 14/3
        assertEquals(14.0/3.0, loss, 0.0001);
    }

    @Test
    public void testComputeZeroTargets() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 2.0, 3.0};
        double[] targets = {0.0, 0.0, 0.0};

        double loss = mseLoss.compute(predictions, targets);

        // Errors = 1, 2, 3, squared = 1, 4, 9, mean = 14/3
        assertEquals(14.0/3.0, loss, 0.0001);
    }

    @Test
    public void testGradientPerfectPrediction() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 2.0, 3.0};
        double[] targets = {1.0, 2.0, 3.0};

        double[] gradient = mseLoss.gradient(predictions, targets);

        assertEquals(3, gradient.length);
        assertEquals(0.0, gradient[0], 0.0001);
        assertEquals(0.0, gradient[1], 0.0001);
        assertEquals(0.0, gradient[2], 0.0001);
    }

    @Test
    public void testGradientWithError() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 2.0, 3.0};
        double[] targets = {2.0, 3.0, 4.0};

        double[] gradient = mseLoss.gradient(predictions, targets);

        assertEquals(3, gradient.length);
        // gradient[i] = 2 * (pred[i] - target[i]) / n = 2 * (-1) / 3 = -2/3
        assertEquals(-2.0/3.0, gradient[0], 0.0001);
        assertEquals(-2.0/3.0, gradient[1], 0.0001);
        assertEquals(-2.0/3.0, gradient[2], 0.0001);
    }

    @Test
    public void testGradientSingleElement() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {5.0};
        double[] targets = {3.0};

        double[] gradient = mseLoss.gradient(predictions, targets);

        assertEquals(1, gradient.length);
        // gradient = 2 * (5-3) / 1 = 4
        assertEquals(4.0, gradient[0], 0.0001);
    }

    @Test
    public void testGradientSymmetry() {
        MSELoss mseLoss = new MSELoss();

        double[] pred1 = {1.0, 2.0};
        double[] target1 = {3.0, 4.0};
        double[] gradient1 = mseLoss.gradient(pred1, target1);

        double[] pred2 = {3.0, 4.0};
        double[] target2 = {1.0, 2.0};
        double[] gradient2 = mseLoss.gradient(pred2, target2);

        // Gradients should be opposite signs
        assertEquals(-gradient1[0], gradient2[0], 0.0001);
        assertEquals(-gradient1[1], gradient2[1], 0.0001);
    }

    @Test
    public void testGradientZeroPredictions() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {0.0, 0.0};
        double[] targets = {1.0, 1.0};

        double[] gradient = mseLoss.gradient(predictions, targets);

        // gradient[i] = 2 * (0 - 1) / 2 = -1
        assertEquals(-1.0, gradient[0], 0.0001);
        assertEquals(-1.0, gradient[1], 0.0001);
    }

    @Test
    public void testGradientZeroTargets() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 1.0};
        double[] targets = {0.0, 0.0};

        double[] gradient = mseLoss.gradient(predictions, targets);

        // gradient[i] = 2 * (1 - 0) / 2 = 1
        assertEquals(1.0, gradient[0], 0.0001);
        assertEquals(1.0, gradient[1], 0.0001);
    }

    @Test
    public void testComputeAndGradientConsistency() {
        MSELoss mseLoss = new MSELoss();

        double[] predictions = {1.0, 2.0, 3.0};
        double[] targets = {0.0, 0.0, 0.0};

        double loss = mseLoss.compute(predictions, targets);
        double[] gradient = mseLoss.gradient(predictions, targets);

        // Verify gradient relates to loss computation
        // Loss = sum((pred - target)^2) / n
        // dLoss/dPred = 2 * (pred - target) / n
        for (int i = 0; i < predictions.length; i++) {
            double expectedGradient = 2.0 * (predictions[i] - targets[i]) / predictions.length;
            assertEquals(expectedGradient, gradient[i], 0.0001);
        }
    }
}
