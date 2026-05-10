package com.zifang.util.ai.ga;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 遗传算法集成测试 - 使用GA解决OneMax问题（全1问题）
 */
public class GeneticAlgorithmIntegrationTest {

    /**
     * OneMax问题：找出全是1的染色体
     */
    @Test
    void testSolveOneMaxProblem() {
        int chromosomeLength = 20;
        int populationSize = 100;
        int maxGenerations = 500;

        // 使用Builder构建遗传算法引擎
        GeneticAlgorithmEngine<Integer> engine = GeneticAlgorithmEngine.<Integer>builder()
                .populationSize(populationSize)
                .mutationRate(0.02)
                .crossoverRate(0.9)
                .elitismCount(5)
                .genotypeFactory(() -> new BinaryGenotype(chromosomeLength))
                .fitnessFunction(ind -> {
                    // OneMax适应度：计算染色体中1的数量
                    int count = 0;
                    for (int i = 0; i < ind.length(); i++) {
                        if (ind.getGene(i) == 1) count++;
                    }
                    return (double) count / ind.length();
                })
                .crossoverOperator(new SinglePointCrossover<>())
                .mutationOperator(new FlipBitMutation<>())
                .terminationCondition(new TargetFitnessTermination(0.999))
                .build();

        // 执行遗传算法
        Population<Integer> result = engine.evolve(maxGenerations);

        // 验证结果
        Individual<Integer> fittest = result.getFittest();
        assertTrue(fittest.getFitness() > 0.9, "Should find near-optimal solution");

        // 打印结果用于可视化验证
        System.out.println("OneMax Problem Result:");
        System.out.println("Best chromosome: " + fittest);
        System.out.println("Best fitness: " + fittest.getFitness());
    }

    @Test
    void testSolveSimpleOptimization() {
        // 优化问题：最大化 f(x) = sum of genes
        // 约束：每个基因在0-9之间

        int chromosomeLength = 5;
        int populationSize = 50;

        GeneticAlgorithmEngine<Integer> engine = GeneticAlgorithmEngine.<Integer>builder()
                .populationSize(populationSize)
                .mutationRate(0.1)
                .crossoverRate(0.8)
                .elitismCount(2)
                .genotypeFactory(() -> new Individual<>(chromosomeLength) {
                    @Override
                    public void randomize() {
                        for (int i = 0; i < length(); i++) {
                            setGene(i, (int) (Math.random() * 10));
                        }
                    }
                })
                .fitnessFunction(ind -> {
                    int sum = 0;
                    for (int i = 0; i < ind.length(); i++) {
                        sum += (int) ind.getGene(i);
                    }
                    return sum;
                })
                .crossoverOperator((p1, p2) -> {
                    int length = p1.length();
                    @SuppressWarnings("unchecked")
                    Integer[] child = new Integer[length];
                    int crossoverPoint = (int) (Math.random() * length);
                    for (int i = 0; i < length; i++) {
                        child[i] = i < crossoverPoint ? p1.getGene(i) : p2.getGene(i);
                    }
                    @SuppressWarnings("unchecked")
                    Integer[][] result = (Integer[][]) new Object[1];
                    result[0] = child;
                    return result[0];
                })
                .mutationOperator((ind, rate) -> {
                    for (int i = 0; i < ind.length(); i++) {
                        if (Math.random() < rate) {
                            ind.setGene(i, (int) (Math.random() * 10));
                        }
                    }
                })
                .terminationCondition((gen, pop) -> gen >= 100)
                .build();

        Population<Integer> result = engine.evolve(100);

        System.out.println("Simple Optimization Result:");
        System.out.println("Best genes: ");
        for (int i = 0; i < result.getFittest().length(); i++) {
            System.out.print(result.getFittest().getGene(i) + " ");
        }
        System.out.println("\nBest fitness: " + result.getFittest().getFitness());
    }
}