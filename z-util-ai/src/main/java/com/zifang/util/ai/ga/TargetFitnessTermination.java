package com.zifang.util.ai.ga;

/**
 * 达到目标适应度终止条件
 */
public class TargetFitnessTermination implements TerminationCondition {

    private final double targetFitness;

    public TargetFitnessTermination(double targetFitness) {
        this.targetFitness = targetFitness;
    }

    @Override
    public boolean isTerminated(int generation, Population population) {
        return population.getFittest().getFitness() >= targetFitness;
    }
}