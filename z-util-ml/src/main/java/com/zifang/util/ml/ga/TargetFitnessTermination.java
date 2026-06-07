package com.zifang.util.ml.ga;

/**
 * 达到目标适应度终止条件
 */
/**
 * TargetFitnessTermination类。
 */
/**
 * TargetFitnessTermination类。
 */
public class TargetFitnessTermination implements TerminationCondition {

    private final double targetFitness;

    /**
     * 构造一个目标适应度终止条件
     *
     * @param targetFitness 目标适应度值
     */
    /**
     * TargetFitnessTermination方法。
     *      * @param targetFitness double类型参数
     */
    /**
     * TargetFitnessTermination方法。
     *      * @param targetFitness double类型参数
     */
    public TargetFitnessTermination(double targetFitness) {
        this.targetFitness = targetFitness;
    }

    @Override
    /**
     * isTerminated方法。
     *      * @param generation int类型参数
     * @param population Population类型参数
     * @return boolean类型返回值
     */
    /**
     * isTerminated方法。
     *      * @param generation int类型参数
     * @param population Population类型参数
     * @return boolean类型返回值
     */
    public boolean isTerminated(int generation, Population population) {
        return population.getFittest().getFitness() >= targetFitness;
    }
}