package com.zifang.util.visuallization.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * 图表数据系列
 */
public class ChartSeries {

    private final String name;
    private final List<Double> data;
    private final List<String> labels;

    public ChartSeries(String name) {
        this.name = name;
        this.data = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    public ChartSeries(String name, List<Double> data) {
        this.name = name;
        this.data = new ArrayList<>(data);
        this.labels = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Double> getData() {
        return new ArrayList<>(data);
    }

    public List<String> getLabels() {
        return new ArrayList<>(labels);
    }

    public int size() {
        return data.size();
    }

    public double getData(int index) {
        return data.get(index);
    }

    public String getLabel(int index) {
        if (index < labels.size()) {
            return labels.get(index);
        }
        return String.valueOf(index);
    }

    public void addData(double value) {
        data.add(value);
    }

    public void addData(double value, String label) {
        data.add(value);
        labels.add(label);
    }

    public void clear() {
        data.clear();
        labels.clear();
    }

    public double getMax() {
        if (data.isEmpty()) return 0;
        double max = data.get(0);
        for (double v : data) {
            if (v > max) max = v;
        }
        return max;
    }

    public double getMin() {
        if (data.isEmpty()) return 0;
        double min = data.get(0);
        for (double v : data) {
            if (v < min) min = v;
        }
        return min;
    }
}