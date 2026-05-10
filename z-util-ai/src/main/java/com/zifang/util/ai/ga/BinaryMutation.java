package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 二进制翻转变异算子
 */
public class BinaryMutation implements MutationOperator {

    private final Random random = new Random();

    @Override
    public void mutate(Individual individual, double mutationRate) {
        for (int i = 0; i < individual.length(); i++) {
            if (random.nextDouble() < mutationRate) {
                if (individual instanceof BinaryGenotype) {
                    BinaryGenotype bg = (BinaryGenotype) individual;
                    int currentGene = bg.getGene(i);
                    bg.setGene(i, currentGene == 1 ? 0 : 1);
                }
            }
        }
    }
}