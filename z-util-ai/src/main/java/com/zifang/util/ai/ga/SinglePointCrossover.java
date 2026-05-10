package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 单点交叉算子
 */
public class SinglePointCrossover<T> implements CrossoverOperator<T> {

    private final Random random = new Random();

    @Override
    @SuppressWarnings("unchecked")
    public T[] crossover(T parent1, T parent2) {
        if (!(parent1 instanceof Genotype)) {
            throw new IllegalArgumentException("Parent must implement Genotype");
        }

        Genotype<T> g1 = (Genotype<T>) parent1;
        Genotype<T> g2 = (Genotype<T>) parent2;
        int length = g1.length();

        @SuppressWarnings("unchecked")
        T[] child1 = (T[]) new Object[length];
        @SuppressWarnings("unchecked")
        T[] child2 = (T[]) new Object[length];

        int crossoverPoint = random.nextInt(length);

        for (int i = 0; i < length; i++) {
            if (i < crossoverPoint) {
                child1[i] = g1.getGene(i);
                child2[i] = g2.getGene(i);
            } else {
                child1[i] = g2.getGene(i);
                child2[i] = g1.getGene(i);
            }
        }

        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Object[2];
        result[0] = child1;
        result[1] = child2;
        return result;
    }
}