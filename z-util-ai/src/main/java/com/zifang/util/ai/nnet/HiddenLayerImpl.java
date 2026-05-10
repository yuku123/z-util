package com.zifang.util.ai.nnet;

import java.util.Arrays;

/**
 * 隐藏层实现
 */
public class HiddenLayerImpl implements Layer {

    private final int neuronCount;
    private final Neuron[] neurons;
    private final ActivationFunction activationFunction;
    private double[] inputs;
    private double[] outputs;

    public HiddenLayerImpl(int inputCount, int neuronCount, ActivationFunction activationFunction) {
        this.neuronCount = neuronCount;
        this.activationFunction = activationFunction;
        this.neurons = new Neuron[neuronCount];
        this.outputs = new double[neuronCount];

        for (int i = 0; i < neuronCount; i++) {
            neurons[i] = new Neuron(inputCount);
        }
    }

    @Override
    public double[] forward(double[] inputs) {
        this.inputs = Arrays.copyOf(inputs, inputs.length);
        this.outputs = new double[neuronCount];

        for (int i = 0; i < neuronCount; i++) {
            outputs[i] = neurons[i].calculateOutput(inputs, activationFunction);
        }

        return outputs;
    }

    @Override
    public double[] backward(double[] gradients) {
        double[] inputGradients = new double[inputs.length];

        for (int i = 0; i < neuronCount; i++) {
            double gradient = gradients[i] * activationFunction.derivative(neurons[i].getOutput());
            neurons[i].setDelta(gradient);

            double[] weights = neurons[i].getWeights();
            for (int j = 0; j < inputs.length; j++) {
                inputGradients[j] += gradient * weights[j];
            }
        }

        return inputGradients;
    }

    @Override
    public double[] getOutput() {
        return outputs;
    }

    @Override
    public LayerType getLayerType() {
        return LayerType.HIDDEN;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public int getNeuronCount() {
        return neuronCount;
    }
}