package com.zifang.util.ai.ga;

/**
 * 变异算子接口
 */
public interface MutationOperator {

    /**
     * 对个体进行变异操作
     */
    void mutate(Individual individual, double mutationRate);
}