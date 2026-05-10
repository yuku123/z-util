package com.zifang.util.visuallization.chart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络拓扑图组件 - 用于可视化神经网络
 */
public class NetworkGraph extends ChartFrame {

    private final List<LayerData> layers;
    private int nodeRadius = 20;
    private int layerSpacing = 80;
    private int nodeSpacing = 50;

    private static final int PADDING = 60;

    public NetworkGraph(String title, int width, int height) {
        super(title, width, height);
        this.layers = new ArrayList<>();
    }

    public NetworkGraph(String title) {
        this(title, 800, 600);
    }

    /**
     * 添加一层神经元
     *
     * @param layerIndex 层索引（从0开始）
     * @param nodeCount  该层神经元数量
     * @param layerName  层名称（如"Input", "Hidden", "Output"）
     */
    public void addLayer(int layerIndex, int nodeCount, String layerName) {
        while (layers.size() <= layerIndex) {
            layers.add(new LayerData());
        }
        layers.get(layerIndex).nodeCount = nodeCount;
        layers.get(layerIndex).layerName = layerName;
    }

    public void addLayer(int nodeCount, String layerName) {
        layers.add(new LayerData());
        LayerData last = layers.get(layers.size() - 1);
        last.nodeCount = nodeCount;
        last.layerName = layerName;
    }

    public void clear() {
        layers.clear();
    }

    public void setNodeRadius(int radius) {
        this.nodeRadius = radius;
    }

    @Override
    public void render() {
        canvas.repaint();
    }

    @Override
    protected ChartCanvas createCanvas() {
        return new NetworkCanvas();
    }

    private static class LayerData {
        int nodeCount;
        String layerName = "";
        List<Double> activations;
    }

    /**
     * 设置某层的激活值（用于热力图）
     */
    public void setLayerActivations(int layerIndex, List<Double> activations) {
        if (layerIndex < layers.size()) {
            layers.get(layerIndex).activations = new ArrayList<>(activations);
        }
    }

    private class NetworkCanvas extends ChartCanvas {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            if (layers.isEmpty()) {
                drawEmptyMessage(g2d);
                return;
            }

            int totalLayers = layers.size();
            int totalWidth = canvasWidth - 2 * PADDING;
            layerSpacing = totalWidth / Math.max(1, totalLayers - 1);

            drawConnections(g2d);
            drawNodes(g2d);
            drawLabels(g2d);
        }

        private void drawEmptyMessage(Graphics2D g2d) {
            g2d.setColor(Color.GRAY);
            FontMetrics metrics = g2d.getFontMetrics();
            String msg = "No network data. Call addLayer() first.";
            g2d.drawString(msg, (canvasWidth - metrics.stringWidth(msg)) / 2, canvasHeight / 2);
        }

        private void drawConnections(Graphics2D g2d) {
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(1));

            for (int l = 0; l < layers.size() - 1; l++) {
                LayerData fromLayer = layers.get(l);
                LayerData toLayer = layers.get(l + 1);

                int fromX = PADDING + l * layerSpacing;
                int toX = PADDING + (l + 1) * layerSpacing;

                int maxNodes = Math.max(fromLayer.nodeCount, toLayer.nodeCount);
                nodeSpacing = Math.min(50, (canvasHeight - 2 * PADDING) / Math.max(1, maxNodes));

                int fromCenterY = (canvasHeight - (fromLayer.nodeCount - 1) * nodeSpacing) / 2;
                int toCenterY = (canvasHeight - (toLayer.nodeCount - 1) * nodeSpacing) / 2;

                for (int i = 0; i < fromLayer.nodeCount; i++) {
                    int fromY = fromCenterY + i * nodeSpacing;
                    for (int j = 0; j < toLayer.nodeCount; j++) {
                        int toY = toCenterY + j * nodeSpacing;
                        g2d.drawLine(fromX, fromY, toX, toY);
                    }
                }
            }
        }

        private void drawNodes(Graphics2D g2d) {
            for (int l = 0; l < layers.size(); l++) {
                LayerData layer = layers.get(l);
                int x = PADDING + l * layerSpacing;
                int centerY = (canvasHeight - (layer.nodeCount - 1) * nodeSpacing) / 2;

                for (int i = 0; i < layer.nodeCount; i++) {
                    int y = centerY + i * nodeSpacing;

                    Color nodeColor = getNodeColor(layer, i);
                    g2d.setColor(nodeColor);
                    g2d.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

                    g2d.setColor(Color.DARK_GRAY);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

                    // 绘制节点内文字
                    g2d.setColor(Color.WHITE);
                    FontMetrics metrics = g2d.getFontMetrics();
                    String text = String.valueOf(i);
                    int textWidth = metrics.stringWidth(text);
                    g2d.drawString(text, x - textWidth / 2, y + metrics.getDescent());
                }
            }
        }

        private Color getNodeColor(LayerData layer, int nodeIndex) {
            if (layer.activations != null && nodeIndex < layer.activations.size()) {
                double activation = layer.activations.get(nodeIndex);
                activation = Math.max(0, Math.min(1, activation));
                return getHeatColor(activation);
            }
            return ChartColors.getColor(0);
        }

        private Color getHeatColor(double value) {
            if (value < 0.5) {
                int r = (int) (255 * value * 2);
                return new Color(r, 100, 255);
            } else {
                int g = (int) (255 * (value - 0.5) * 2);
                return new Color(255, 100 + g, 0);
            }
        }

        private void drawLabels(Graphics2D g2d) {
            FontMetrics metrics = g2d.getFontMetrics();

            for (int l = 0; l < layers.size(); l++) {
                LayerData layer = layers.get(l);
                int x = PADDING + l * layerSpacing;

                if (layer.layerName != null && !layer.layerName.isEmpty()) {
                    String label = layer.layerName;
                    int labelWidth = metrics.stringWidth(label);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(label, x - labelWidth / 2, 25);
                }

                String nodeCountLabel = "(" + layer.nodeCount + ")";
                int countWidth = metrics.stringWidth(nodeCountLabel);
                g2d.setColor(Color.GRAY);
                g2d.drawString(nodeCountLabel, x - countWidth / 2, 40);
            }
        }
    }
}