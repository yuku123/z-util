package com.zifang.util.ai.ga;

/**
 * 适应度评估器接口
 *
 * @param <T> 基因型类型
 */
public interface FitnessFunction<T extends Genotype<?>> {

    /**
     * 计算个体的适应度
     *
     * @param genotype 个体的基因型
     * @return 适应度值，越高越好
     */
    double evaluate(T genotype);
}