package com.zifang.util.ai.ga;

import java.util.Objects;

/**
 * 个体 - 代表种群中的单个成员
 */
public class Individual {

    private final Object[] chromosome;
    private double fitness = -1;

    public Individual(int length) {
        this.chromosome = new Object[length];
    }

    public Individual(Object[] chromosome) {
        this.chromosome = Objects.requireNonNull(chromosome);
    }

    public int length() {
        return chromosome.length;
    }

    public Object getGene(int index) {
        return chromosome[index];
    }

    public void setGene(int index, Object gene) {
        chromosome[index] = gene;
    }

    public Individual copy() {
        Individual result = new Individual(this.chromosome.length);
        for (int i = 0; i < chromosome.length; i++) {
            result.setGene(i, this.chromosome[i]);
        }
        result.fitness = this.fitness;
        return result;
    }

    public void randomize() {
        throw new UnsupportedOperationException("Subclass must implement randomize()");
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object gene : chromosome) {
            sb.append(gene);
        }
        return sb.toString();
    }
}