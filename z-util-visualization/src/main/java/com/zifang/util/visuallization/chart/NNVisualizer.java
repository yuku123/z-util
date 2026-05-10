package com.zifang.util.visuallization.chart;

import com.zifang.util.ai.nnet.NeuralNetwork;
import com.zifang.util.ai.nnet.Layer;

import java.util.ArrayList;
import java.util.List;

/**
 * 神经网络可视化器
 */
public class NNVisualizer {

    private final NetworkGraph networkGraph;
    private final LineChart lossChart;
    private final List<Double> lossHistory;
    private int iteration = 0;

    public NNVisualizer() {
        this.networkGraph = new NetworkGraph("Neural Network Topology", 800, 600);
        this.lossChart = new LineChart("Training Loss", 600, 400);
        this.lossHistory = new ArrayList<>();
    }

    /**
     * 初始化网络拓扑
     */
    public void initializeNetwork(NeuralNetwork network) {
        networkGraph.clear();
        List<Layer> layers = network.getLayers();

        if (layers.isEmpty()) {
            return;
        }

        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            String layerName = layer.getLayerType().name();
            networkGraph.addLayer(i, layer.getNeuronCount(), layerName);
        }

        networkGraph.render();
    }

    /**
     * 更新训练损失
     */
    public void updateLoss(double loss) {
        lossHistory.add(loss);
        updateLossChart();
        iteration++;
    }

    /**
     * 更新神经元激活值（热力图）
     */
    public void updateActivations(int layerIndex, double[] activations) {
        List<Double> activationList = new ArrayList<>();
        for (double a : activations) {
            activationList.add(a);
        }
        networkGraph.setLayerActivations(layerIndex, activationList);
        networkGraph.render();
    }

    private void updateLossChart() {
        lossChart.clear();
        lossChart.addSeries("Loss", new ArrayList<>(lossHistory));
        lossChart.setAxisLabels("Iteration", "Loss");
        lossChart.render();
    }

    public NetworkGraph getNetworkGraph() {
        return networkGraph;
    }

    public LineChart getLossChart() {
        return lossChart;
    }

    public int getIteration() {
        return iteration;
    }

    public List<Double> getLossHistory() {
        return new ArrayList<>(lossHistory);
    }
}