package com.zifang.util.ai.ga;

/**
 * 变异算子接口 - 定义个体如何变异
 *
 * @param <T> 基因型类型
 */
public interface MutationOperator<T extends Genotype<?>> {

    /**
     * 对个体进行变异操作
     *
     * @param genotype 待变异的基因型
     * @param mutationRate 变异概率
     */
    void mutate(T genotype, double mutationRate);
}