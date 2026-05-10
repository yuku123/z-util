package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 翻转变异算子 - 随机翻转基因位
 */
public class FlipBitMutation<T> implements MutationOperator<T> {

    private final Random random = new Random();

    @Override
    public void mutate(T genotype, double mutationRate) {
        if (!(genotype instanceof Genotype)) {
            throw new IllegalArgumentException("Genotype must implement Genotype interface");
        }

        Genotype<?> g = (Genotype<?>) genotype;
        for (int i = 0; i < g.length(); i++) {
            if (random.nextDouble() < mutationRate) {
                if (genotype instanceof BinaryGenotype) {
                    @SuppressWarnings("unchecked")
                    BinaryGenotype bg = (BinaryGenotype) genotype;
                    bg.setGene(i, bg.getGene(i) == 1 ? 0 : 1);
                }
            }
        }
    }
}