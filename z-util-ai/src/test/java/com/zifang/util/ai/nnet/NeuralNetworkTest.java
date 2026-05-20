package com.zifang.util.ai.nnet;

import org.junit.Test;

import java.util.Arrays;

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
    }

    @Test
    public void testAddLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        Layer layer = new HiddenLayerImpl(2, 3, new SigmoidActivation());
        NeuralNetwork result = nn.addLayer(layer);

        assertSame(nn, result);
        assertEquals(1, nn.getLayers().size());
        assertSame(layer, nn.getLayers().get(0));
    }

    @Test
    public void testAddMultipleLayers() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.addLayer(new HiddenLayerImpl(2, 3, new SigmoidActivation()));
        nn.addLayer(new HiddenLayerImpl(3, 1, new SigmoidActivation()));

        assertEquals(2, nn.getLayers().size());
    }

    @Test
    public void testAddLayerMethodChaining() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.addLayer(new HiddenLayerImpl(2, 3, new SigmoidActivation()))
          .addLayer(new HiddenLayerImpl(3, 1, new SigmoidActivation()))
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
        nn.addLayer(new Layer() {
            @Override
            public double[] forward(double[] inputs) {
                return inputs;
            }

            @Override
            public double[] backward(double[] gradients) {
                return gradients;
            }

            @Override
            public double[] getOutput() {
                return new double[0];
            }

            @Override
            public Layer.LayerType getLayerType() {
                return Layer.LayerType.HIDDEN;
            }

            @Override
            public int getNeuronCount() {
                return 2;
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
        nn.addLayer(new HiddenLayerImpl(2, 2, new SigmoidActivation()));

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
        nn.addLayer(new HiddenLayerImpl(2, 2, new SigmoidActivation()));

        double[] inputs = {1.0, 2.0};
        double[] targets = {3.0, 4.0};

        double loss1 = nn.train(inputs, targets);
        double loss2 = nn.train(inputs, targets);
        double loss3 = nn.train(inputs, targets);

        assertTrue(loss1 >= 0);
        assertTrue(loss2 >= 0);
        assertTrue(loss3 >= 0);
    }

    @Test
    public void testGetLayersReturnsCopy() {
        NeuralNetwork nn = new NeuralNetwork();
        Layer layer = new HiddenLayerImpl(2, 3, new SigmoidActivation());
        nn.addLayer(layer);

        // getLayers returns a copy, modifying it does not affect original
        nn.getLayers().clear();

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

        double[] inputs = {1.0};
        double[] targets = {2.0};

        double loss = nn.train(inputs, targets);
        assertTrue(loss >= 0);
    }

    @Test
    public void testPredictWithHiddenLayer() {
        NeuralNetwork nn = new NeuralNetwork();
        nn.addLayer(new HiddenLayerImpl(2, 2, new SigmoidActivation()));

        double[] inputs = {1.0, 0.5};
        double[] outputs = nn.predict(inputs);

        assertNotNull(outputs);
        assertEquals(2, outputs.length);
    }

    @Test
    public void testLossFunctionSetter() {
        NeuralNetwork nn = new NeuralNetwork();
        MSELoss lossFn = new MSELoss();
        nn.lossFunction(lossFn);

        double[] inputs = {1.0, 2.0};
        double[] targets = {3.0, 4.0};

        double loss = nn.train(inputs, targets);
        assertTrue(loss >= 0);
    }
}
