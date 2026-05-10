package com.zifang.util.visuallization.chart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 折线图组件
 */
public class LineChart extends ChartFrame {

    private final List<ChartSeries> seriesList;
    private String xAxisLabel = "X";
    private String yAxisLabel = "Y";
    private double yMin = 0;
    private double yMax = 1;

    private static final int PADDING = 60;
    private static final int RIGHT_PADDING = 20;
    private static final int TOP_PADDING = 40;
    private static final int BOTTOM_PADDING = 60;

    public LineChart(String title, int width, int height) {
        super(title, width, height);
        this.seriesList = new ArrayList<>();
    }

    public LineChart(String title) {
        this(title, 800, 600);
    }

    public void addSeries(ChartSeries series) {
        seriesList.add(series);
        updateYRange();
    }

    public void addSeries(String name, List<Double> data) {
        addSeries(new ChartSeries(name, data));
    }

    public void addData(String seriesName, double value) {
        ChartSeries series = findSeries(seriesName);
        if (series == null) {
            series = new ChartSeries(seriesName);
            seriesList.add(series);
        }
        series.addData(value);
        updateYRange();
    }

    public void clear() {
        for (ChartSeries series : seriesList) {
            series.clear();
        }
        yMin = 0;
        yMax = 1;
    }

    private ChartSeries findSeries(String name) {
        for (ChartSeries s : seriesList) {
            if (s.getName().equals(name)) return s;
        }
        return null;
    }

    private void updateYRange() {
        if (seriesList.isEmpty()) return;

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (ChartSeries series : seriesList) {
            if (series.size() > 0) {
                min = Math.min(min, series.getMin());
                max = Math.max(max, series.getMax());
            }
        }

        if (min == max) {
            min = 0;
            max = 1;
        }

        double margin = (max - min) * 0.1;
        this.yMin = Math.max(0, min - margin);
        this.yMax = max + margin;
    }

    @Override
    public void render() {
        canvas.repaint();
    }

    public void setAxisLabels(String xAxisLabel, String yAxisLabel) {
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }

    @Override
    protected ChartCanvas createCanvas() {
        return new LineChartCanvas();
    }

    private class LineChartCanvas extends ChartCanvas {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int chartWidth = canvasWidth - PADDING - RIGHT_PADDING;
            int chartHeight = canvasHeight - TOP_PADDING - BOTTOM_PADDING;

            drawAxes(g2d, PADDING, TOP_PADDING, chartWidth, chartHeight);
            drawGrid(g2d, PADDING, TOP_PADDING, chartWidth, chartHeight);
            drawLines(g2d, PADDING, TOP_PADDING, chartWidth, chartHeight);
            drawLegend(g2d, PADDING, TOP_PADDING);
        }

        private void drawAxes(Graphics2D g2d, int x, int y, int width, int height) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));

            g2d.drawLine(x, y, x, y + height);
            g2d.drawLine(x, y + height, x + width, y + height);

            FontMetrics metrics = g2d.getFontMetrics();

            g2d.drawString(yAxisLabel, x - metrics.stringWidth(yAxisLabel) - 10, y - 10);
            g2d.drawString(xAxisLabel, x + width - metrics.stringWidth(xAxisLabel) + 10, y + height + 30);

            int tickCount = 5;
            for (int i = 0; i <= tickCount; i++) {
                double value = yMin + (yMax - yMin) * (tickCount - i) / tickCount;
                int tickY = y + (int) (height * i / tickCount);

                g2d.setColor(Color.GRAY);
                g2d.drawLine(x - 5, tickY, x, tickY);

                String tickLabel = String.format("%.2f", value);
                g2d.drawString(tickLabel, x - metrics.stringWidth(tickLabel) - 8, tickY + 5);
            }
        }

        private void drawGrid(Graphics2D g2d, int x, int y, int width, int height) {
            g2d.setColor(new Color(230, 230, 230));
            g2d.setStroke(new BasicStroke(1));

            int tickCount = 5;
            for (int i = 0; i <= tickCount; i++) {
                int tickY = y + (int) (height * i / tickCount);
                g2d.drawLine(x, tickY, x + width, tickY);
            }
        }

        private void drawLines(Graphics2D g2d, int x, int y, int width, int height) {
            for (int seriesIndex = 0; seriesIndex < seriesList.size(); seriesIndex++) {
                ChartSeries series = seriesList.get(seriesIndex);
                if (series.size() < 2) continue;

                Color color = ChartColors.getColor(seriesIndex);
                g2d.setColor(color);
                g2d.setStroke(new BasicStroke(2.5f));

                int[] xPoints = new int[series.size()];
                int[] yPoints = new int[series.size()];

                for (int i = 0; i < series.size(); i++) {
                    double value = series.getData(i);
                    xPoints[i] = x + (int) (width * i / (series.size() - 1));
                    yPoints[i] = y + (int) (height * (1 - (value - yMin) / (yMax - yMin)));
                }

                for (int i = 0; i < series.size() - 1; i++) {
                    g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }

                g2d.setColor(Color.WHITE);
                for (int i = 0; i < series.size(); i++) {
                    g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                }
                g2d.setColor(color);
                for (int i = 0; i < series.size(); i++) {
                    g2d.drawOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                }
            }
        }

        private void drawLegend(Graphics2D g2d, int x, int y) {
            int legendY = 15;
            int legendX = x + 10;

            FontMetrics metrics = g2d.getFontMetrics();

            for (int i = 0; i < seriesList.size(); i++) {
                ChartSeries series = seriesList.get(i);
                Color color = ChartColors.getColor(i);

                g2d.setColor(color);
                g2d.fillRect(legendX, legendY - 8, 20, 10);

                g2d.setColor(Color.BLACK);
                g2d.drawString(series.getName(), legendX + 28, legendY);

                legendX += 30 + metrics.stringWidth(series.getName());
            }
        }
    }
}