package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 单点交叉算子
 */
public class SinglePointCrossover implements CrossoverOperator {

    private final Random random = new Random();

    @Override
    public Individual[] crossover(Individual parent1, Individual parent2) {
        Individual[] result = new Individual[2];
        result[0] = parent1.copy();
        result[1] = parent2.copy();

        int crossoverPoint = random.nextInt(parent1.length());

        for (int i = crossoverPoint; i < parent1.length(); i++) {
            Object temp = result[0].getGene(i);
            result[0].setGene(i, result[1].getGene(i));
            result[1].setGene(i, temp);
        }

        return result;
    }
}