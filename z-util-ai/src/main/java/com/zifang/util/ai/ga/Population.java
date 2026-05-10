package com.zifang.util.ai.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 种群 - 代表一组个体
 *
 * @param <T> 基因类型
 */
public class Population<T> {

    private final List<Individual<T>> individuals;
    private double totalFitness = -1;
    private final Random random;

    public Population() {
        this.individuals = new ArrayList<>();
        this.random = new Random();
    }

    public Population(int size) {
        this.individuals = new ArrayList<>(size);
        this.random = new Random();
    }

    public void addIndividual(Individual<T> individual) {
        individuals.add(individual);
    }

    public Individual<T> getIndividual(int index) {
        return individuals.get(index);
    }

    public Individual<T> setIndividual(int index, Individual<T> individual) {
        return individuals.set(index, individual);
    }

    public int size() {
        return individuals.size();
    }

    public List<Individual<T>> getIndividuals() {
        return new ArrayList<>(individuals);
    }

    /**
     * 获取适应度最高的个体
     */
    public Individual<T> getFittest() {
        return individuals.stream()
                .max(Comparator.comparingDouble(Individual::getFitness))
                .orElse(individuals.get(0));
    }

    /**
     * 按适应度排序后获取第offset个个体
     */
    public Individual<T> getFittest(int offset) {
        individuals.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
        if (offset < 0 || offset >= individuals.size()) {
            offset = 0;
        }
        return individuals.get(offset);
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public void setTotalFitness(double totalFitness) {
        this.totalFitness = totalFitness;
    }

    /**
     * 计算总适应度
     */
    public double calculateTotalFitness() {
        this.totalFitness = individuals.stream()
                .mapToDouble(Individual::getFitness)
                .sum();
        return this.totalFitness;
    }

    /**
     * 轮盘赌选择
     */
    public Individual<T> rouletteSelect() {
        if (totalFitness <= 0) {
            return individuals.get(random.nextInt(individuals.size()));
        }
        double point = random.nextDouble() * totalFitness;
        double accumulated = 0;
        for (Individual<T> individual : individuals) {
            accumulated += individual.getFitness();
            if (accumulated >= point) {
                return individual;
            }
        }
        return individuals.get(individuals.size() - 1);
    }

    /**
     * 随机打乱种群
     */
    public void shuffle() {
        for (int i = individuals.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Individual<T> temp = individuals.get(i);
            individuals.set(i, individuals.get(j));
            individuals.set(j, temp);
        }
    }
}