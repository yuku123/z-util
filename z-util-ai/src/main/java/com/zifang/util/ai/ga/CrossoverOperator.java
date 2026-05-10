package com.zifang.util.ai.ga;

/**
 * 交叉算子接口
 */
public interface CrossoverOperator {

    /**
     * 对两个父代进行交叉操作，产生两个子代
     */
    Individual[] crossover(Individual parent1, Individual parent2);
}