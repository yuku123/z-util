package com.zifang.util.ai.ga;

/**
 * 选择算子接口 - 定义如何从种群中选择父代
 *
 * @param <T> 基因型类型
 */
public interface SelectionOperator<T extends Genotype<?>> {

    /**
     * 从种群中选择一个个体
     *
     * @param population 种群
     * @return 被选中的个体
     */
    T select(Population<T> population);
}