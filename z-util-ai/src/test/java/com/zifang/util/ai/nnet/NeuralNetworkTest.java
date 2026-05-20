package com.zifang.util.ai.nnet;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * NeuralNetwork 类测试
 */
public class NeuralNetworkTest {

    @Test
    public void testDefaultConstructor() {
        NeuralNetwork nn = new NeuralNetwork();
        assertNotNull(nn);
        assertNotNull(nn.getLayers());
        assertTrue(nn.getLayers().isEmpty());
    }

    @Test
    public void testLearningRateDefault() {
        NeuralNetwork nn = new NeuralNetwork();
        assertEquals(0.01, nn.getLearningRate(), 0.0001);
    }

    @Test
    public void testLearningRateSetter() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.learningRate(0.1);
        assertEquals(0.1, nn.getLearningRate(), 0.0001);

        nn.learningRate(0.001);
        assertEquals(0.001, nn.getLearningRate(), 0.000001);
    }

    @Test
    public void testLearningRateMethodChaining() {
        NeuralNetwork nn = new NeuralNetwork();
        NeuralNetwork result = nn.learningRate(0.05);
        assertSame(nn, result);
    }

    @Test
    public void testLossFunctionSetter() {
        NeuralNetwork nn = new NeuralNetwork();
        MSELoss mseLoss = new MSELoss();
        NeuralNetwork result = nn.lossFunction(mseLoss);
        assertSame(nn, result);
    }

    @Test
    public void testAddLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        Layer layer = new HiddenLayerImpl(2, 3);
        NeuralNetwork result = nn.addLayer(layer);

        assertSame(nn, result);
        assertEquals(1, nn.getLayers().size());
        assertSame(layer, nn.getLayers().get(0));
    }

    @Test
    public void testAddMultipleLayers() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.addLayer(new HiddenLayerImpl(2, 3));
        nn.addLayer(new HiddenLayerImpl(3, 1));

        assertEquals(2, nn.getLayers().size());
    }

    @Test
    public void testAddLayerMethodChaining() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.addLayer(new HiddenLayerImpl(2, 3))
          .addLayer(new HiddenLayerImpl(3, 1))
          .learningRate(0.1);

        assertEquals(2, nn.getLayers().size());
        assertEquals(0.1, nn.getLearningRate(), 0.0001);
    }

    @Test
    public void testPredictWithNoLayers() {
        NeuralNetwork nn = new NeuralNetwork();
        double[] inputs = {1.0, 2.0};
        double[] outputs = nn.predict(inputs);

        // No layers, should return same input
        assertArrayEquals(inputs, outputs, 0.0001);
    }

    @Test
    public void testPredictWithMockLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        // Add a simple pass-through layer
        nn.addLayer(new Layer() {
            @Override
            public double[] forward(double[] inputs) {
                // Simple identity transformation
                return inputs;
            }

            @Override
            public double[] backward(double[] gradients) {
                return gradients;
            }
        });

        double[] inputs = {1.0, 2.0};
        double[] outputs = nn.predict(inputs);

        assertArrayEquals(inputs, outputs, 0.0001);
    }

    @Test
    public void testTrainWithNoLayers() {
        NeuralNetwork nn = new NeuralNetwork();
        double[] inputs = {1.0, 2.0};
        double[] targets = {3.0, 4.0};

        double loss = nn.train(inputs, targets);

        // No layers and no loss function, should return 0
        assertEquals(0.0, loss, 0.0001);
    }

    @Test
    public void testTrainWithLossFunction() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.lossFunction(new MSELoss());
        nn.addLayer(new HiddenLayerImpl(2, 2));

        double[] inputs = {1.0, 2.0};
        double[] targets = {3.0, 4.0};

        double loss = nn.train(inputs, targets);

        // Loss should be non-negative for MSE
        assertTrue(loss >= 0);
    }

    @Test
    public void testTrainMultipleTimes() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.learningRate(0.1);
        nn.lossFunction(new MSELoss());
        nn.addLayer(new HiddenLayerImpl(2, 2));

        double[] inputs = {1.0, 2.0};
        double[] targets = {3.0, 4.0};

        // Train multiple times
        double loss1 = nn.train(inputs, targets);
        double loss2 = nn.train(inputs, targets);
        double loss3 = nn.train(inputs, targets);

        // Subsequent losses might vary depending on implementation
        assertTrue(loss1 >= 0);
        assertTrue(loss2 >= 0);
        assertTrue(loss3 >= 0);
    }

    @Test
    public void testGetLayersReturnsCopy() {
        NeuralNetwork nn = new NeuralNetwork();
        Layer layer = new HiddenLayerImpl(2, 3);
        nn.addLayer(layer);

        // Get layers and modify
        nn.getLayers().clear();

        // Original should still have the layer
        assertEquals(1, nn.getLayers().size());
    }

    @Test
    public void testEmptyNetworkPredict() {
        NeuralNetwork nn = new NeuralNetwork();
        double[] inputs = {};
        double[] outputs = nn.predict(inputs);

        assertArrayEquals(inputs, outputs, 0.0001);
    }

    @Test
    public void testEmptyNetworkTrain() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.lossFunction(new MSELoss());

        double[] inputs = {};
        double[] targets = {};

        double loss = nn.train(inputs, targets);
        assertEquals(0.0, loss, 0.0001);
    }
}
