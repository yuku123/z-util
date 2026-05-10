package com.zifang.util.ai.ga;

/**
 * 适应度评估器接口
 */
public interface FitnessFunction {

    /**
     * 计算个体的适应度
     */
    double evaluate(Individual individual);
}