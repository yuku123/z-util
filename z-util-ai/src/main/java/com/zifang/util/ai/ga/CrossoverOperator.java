package com.zifang.util.ai.ga;

/**
 * 交叉算子接口 - 定义两个父代如何产生子代
 *
 * @param <T> 基因型类型
 */
public interface CrossoverOperator<T extends Genotype<?>> {

    /**
     * 对两个父代进行交叉操作，产生子代
     *
     * @param parent1 父代1
     * @param parent2 父代2
     * @return 子代基因型
     */
    T[] crossover(T parent1, T parent2);
}