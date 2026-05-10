package com.zifang.util.ai.ga;

/**
 * 选择算子接口
 */
public interface SelectionOperator {

    /**
     * 从种群中选择一个个体
     */
    Individual select(Population population);
}