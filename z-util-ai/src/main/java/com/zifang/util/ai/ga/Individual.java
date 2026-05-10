package com.zifang.util.ai.ga;

import java.util.Objects;

/**
 * 个体 - 代表种群中的单个成员
 *
 * @param <T> 基因类型
 */
public class Individual<T> implements Genotype<T> {

    private final T[] chromosome;
    private double fitness = -1;

    @SuppressWarnings("unchecked")
    public Individual(int length) {
        this.chromosome = (T[]) new Object[length];
    }

    public Individual(T[] chromosome) {
        this.chromosome = Objects.requireNonNull(chromosome);
    }

    @Override
    public int length() {
        return chromosome.length;
    }

    @Override
    public T getGene(int index) {
        return chromosome[index];
    }

    @Override
    public void setGene(int index, T gene) {
        chromosome[index] = gene;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Individual<T> copy() {
        Individual<T> copy = new Individual<>(this.chromosome.length);
        for (int i = 0; i < chromosome.length; i++) {
            copy.setGene(i, this.chromosome[i]);
        }
        copy.fitness = this.fitness;
        return copy;
    }

    @Override
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
        for (T gene : chromosome) {
            sb.append(gene);
        }
        return sb.toString();
    }
}