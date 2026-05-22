package com.zifang.util.ml.ga;

/**
 * 达到目标适应度终止条件
 */
public class TargetFitnessTermination implements TerminationCondition {

    private final double targetFitness;

    /**
     * 构造一个目标适应度终止条件
     *
     * @param targetFitness 目标适应度值
     */
    public TargetFitnessTermination(double targetFitness) {
        this.targetFitness = targetFitness;
    }

    @Override
    public boolean isTerminated(int generation, Population population) {
        return population.getFittest().getFitness() >= targetFitness;
    }
}