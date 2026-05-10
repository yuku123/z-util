package com.zifang.util.visuallization.chart;

import com.zifang.util.ai.ga.Population;
import com.zifang.util.ai.ga.Individual;

import java.util.ArrayList;
import java.util.List;

/**
 * 遗传算法可视化器
 */
public class GAVisualizer {

    private final LineChart fitnessChart;
    private final BarChart chromosomeChart;
    private final List<Double> bestFitnessHistory;
    private final List<Double> avgFitnessHistory;
    private int generation = 0;

    public GAVisualizer() {
        this.fitnessChart = new LineChart("GA - Fitness Evolution", 600, 400);
        this.chromosomeChart = new BarChart("GA - Best Chromosome", 600, 400);
        this.bestFitnessHistory = new ArrayList<>();
        this.avgFitnessHistory = new ArrayList<>();
    }

    /**
     * 更新可视化数据
     */
    public void update(Population population) {
        Individual fittest = population.getFittest();

        bestFitnessHistory.add(fittest.getFitness());
        avgFitnessHistory.add(population.getTotalFitness() / population.size());

        updateFitnessChart();
        updateChromosomeChart(fittest);

        generation++;
    }

    private void updateFitnessChart() {
        fitnessChart.clear();
        fitnessChart.addSeries("Best Fitness", new ArrayList<>(bestFitnessHistory));
        fitnessChart.addSeries("Avg Fitness", new ArrayList<>(avgFitnessHistory));
        fitnessChart.setAxisLabels("Generation", "Fitness");
        fitnessChart.render();
    }

    private void updateChromosomeChart(Individual individual) {
        chromosomeChart.clear();
        List<Double> geneValues = new ArrayList<>();
        for (int i = 0; i < individual.length(); i++) {
            Object gene = individual.getGene(i);
            if (gene instanceof Number) {
                geneValues.add(((Number) gene).doubleValue());
            } else {
                geneValues.add(gene.toString().equals("1") ? 1.0 : 0.0);
            }
        }
        chromosomeChart.addSeries("Genes", geneValues);
        chromosomeChart.setAxisLabels("Gene Index", "Value");
        chromosomeChart.render();
    }

    public LineChart getFitnessChart() {
        return fitnessChart;
    }

    public BarChart getChromosomeChart() {
        return chromosomeChart;
    }

    public int getGeneration() {
        return generation;
    }

    public List<Double> getBestFitnessHistory() {
        return new ArrayList<>(bestFitnessHistory);
    }
}