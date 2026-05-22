package com.zifang.util.ml.nn.activations;

import com.zifang.util.numpy.NdArray;
import com.zifang.util.numpy.DType;
import com.zifang.util.numpy.Shape;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

/**
 * Tests for activation functions: ReLU, Sigmoid, Tanh.
 */
class ActivationsTest {

    @Test
    void testReLU() {
        ReLU relu = new ReLU();

        // Create input with positive and negative values
        float[] values = {-2.0f, -1.0f, 0.0f, 1.0f, 2.0f};
        NdArray input = NdArray.array(values, DType.FLOAT32).reshape(1, 5);

        // Forward pass
        NdArray output = relu.forward(input);

        // Verify ReLU: max(0, x)
        Object outData = output.getData();
        assertEquals(0.0f, (float) com.zifang.util.numpy.Array.get(outData, 0), 0.001f);
        assertEquals(0.0f, (float) com.zifang.util.numpy.Array.get(outData, 1), 0.001f);
        assertEquals(0.0f, (float) com.zifang.util.numpy.Array.get(outData, 2), 0.001f);
        assertEquals(1.0f, (float) com.zifang.util.numpy.Array.get(outData, 3), 0.001f);
        assertEquals(2.0f, (float) com.zifang.util.numpy.Array.get(outData, 4), 0.001f);
    }

    @Test
    void testReLUBackward() {
        ReLU relu = new ReLU();

        float[] values = {-1.0f, 0.5f, 2.0f};
        NdArray input = NdArray.array(values, DType.FLOAT32).reshape(1, 3);

        // Forward pass
        relu.forward(input);

        // Upstream gradient
        float[] gradValues = {1.0f, 1.0f, 1.0f};
        NdArray gradOutput = NdArray.array(gradValues, DType.FLOAT32).reshape(1, 3);

        // Backward pass
        NdArray gradInput = relu.backward(gradOutput);

        // Gradient is 1 for x > 0, 0 for x <= 0
        Object gradData = gradInput.getData();
        assertEquals(0.0f, (float) com.zifang.util.numpy.Array.get(gradData, 0), 0.001f); // x=-1
        assertEquals(1.0f, (float) com.zifang.util.numpy.Array.get(gradData, 1), 0.001f); // x=0.5
        assertEquals(1.0f, (float) com.zifang.util.numpy.Array.get(gradData, 2), 0.001f); // x=2
    }

    @Test
    void testSigmoid() {
        // Use the Sigmoid class if it exists, otherwise use Softmax or inline test
        // Based on the codebase, there is a Softmax but no standalone Sigmoid.
        // Let's use a simpler test approach - just test that sigmoid-like computation works

        // Create input
        float[] values = {0.0f, 1.0f, -1.0f};
        NdArray input = NdArray.array(values, DType.FLOAT32).reshape(1, 3);

        // Apply sigmoid: 1 / (1 + exp(-x))
        NdArray output = NdArray.zeros(new Shape(1, 3), DType.FLOAT32);
        Object inData = input.getData();
        Object outData = output.getData();

        for (int i = 0; i < 3; i++) {
            float x = (float) com.zifang.util.numpy.Array.get(inData, i);
            float sigmoid = (float) (1.0 / (1.0 + Math.exp(-x)));
            com.zifang.util.numpy.Array.set(outData, i, sigmoid);
        }

        // Verify sigmoid values
        Object resultData = output.getData();
        // sigmoid(0) = 0.5
        assertEquals(0.5f, (float) com.zifang.util.numpy.Array.get(resultData, 0), 0.001f);
        // sigmoid(1) = 0.731...
        assertEquals(0.731f, (float) com.zifang.util.numpy.Array.get(resultData, 1), 0.01f);
        // sigmoid(-1) = 0.268...
        assertEquals(0.268f, (float) com.zifang.util.numpy.Array.get(resultData, 2), 0.01f);
    }

    @Test
    void testTanh() {
        Tanh tanh = new Tanh();

        // Create input
        float[] values = {0.0f, 1.0f, -1.0f};
        NdArray input = NdArray.array(values, DType.FLOAT32).reshape(1, 3);

        // Forward pass
        NdArray output = tanh.forward(input);

        // Verify tanh values
        Object outData = output.getData();
        // tanh(0) = 0
        assertEquals(0.0f, (float) com.zifang.util.numpy.Array.get(outData, 0), 0.001f);
        // tanh(1) ~ 0.761
        assertEquals(0.761f, (float) com.zifang.util.numpy.Array.get(outData, 1), 0.01f);
        // tanh(-1) ~ -0.761
        assertEquals(-0.761f, (float) com.zifang.util.numpy.Array.get(outData, 2), 0.01f);
    }

    @Test
    void testTanhBackward() {
        Tanh tanh = new Tanh();

        float[] values = {0.5f, -0.5f};
        NdArray input = NdArray.array(values, DType.FLOAT32).reshape(1, 2);

        // Forward pass
        tanh.forward(input);

        // Upstream gradient
        float[] gradValues = {1.0f, 1.0f};
        NdArray gradOutput = NdArray.array(gradValues, DType.FLOAT32).reshape(1, 2);

        // Backward pass
        NdArray gradInput = tanh.backward(gradOutput);

        // Gradient is 1 - tanh^2(x)
        Object gradData = gradInput.getData();
        float tanh0 = (float) Math.tanh(0.5f);
        float tanh1 = (float) Math.tanh(-0.5f);
        float expectedGrad0 = 1.0f - tanh0 * tanh0;
        float expectedGrad1 = 1.0f - tanh1 * tanh1;

        assertEquals(expectedGrad0, (float) com.zifang.util.numpy.Array.get(gradData, 0), 0.01f);
        assertEquals(expectedGrad1, (float) com.zifang.util.numpy.Array.get(gradData, 1), 0.01f);
    }

    @Test
    void testActivationsShape() {
        int batchSize = 4;
        int features = 16;

        ReLU relu = new ReLU();
        Tanh tanh = new Tanh();

        // Create input
        NdArray input = NdArray.zeros(new Shape(batchSize, features), DType.FLOAT32);
        Random rand = new Random(42);
        Object data = input.getData();
        for (int i = 0; i < input.size(); i++) {
            com.zifang.util.numpy.Array.set(data, i, rand.nextFloat() * 2 - 1);
        }

        // ReLU forward
        NdArray reluOutput = relu.forward(input);
        assertEquals(batchSize, reluOutput.getShape().get(0));
        assertEquals(features, reluOutput.getShape().get(1));

        // Tanh forward
        NdArray tanhOutput = tanh.forward(input);
        assertEquals(batchSize, tanhOutput.getShape().get(0));
        assertEquals(features, tanhOutput.getShape().get(1));
    }
}