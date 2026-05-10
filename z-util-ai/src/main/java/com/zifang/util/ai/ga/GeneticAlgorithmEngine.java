package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 遗传算法引擎 - 将各组件组装起来执行遗传算法
 *
 * @param <T> 基因类型
 */
public class GeneticAlgorithmEngine<T> {

    private final int populationSize;
    private final double mutationRate;
    private final double crossoverRate;
    private final int elitismCount;

    private final GenotypeFactory<T> genotypeFactory;
    private final FitnessFunction<T> fitnessFunction;
    private final CrossoverOperator<T> crossoverOperator;
    private final MutationOperator<T> mutationOperator;
    private final SelectionOperator<T> selectionOperator;
    private final TerminationCondition terminationCondition;

    private final Random random;

    public GeneticAlgorithmEngine(Builder<T> builder) {
        this.populationSize = builder.populationSize;
        this.mutationRate = builder.mutationRate;
        this.crossoverRate = builder.crossoverRate;
        this.elitismCount = builder.elitismCount;
        this.genotypeFactory = builder.genotypeFactory;
        this.fitnessFunction = builder.fitnessFunction;
        this.crossoverOperator = builder.crossoverOperator;
        this.mutationOperator = builder.mutationOperator;
        this.selectionOperator = builder.selectionOperator;
        this.terminationCondition = builder.terminationCondition;
        this.random = new Random();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * 执行遗传算法
     *
     * @param maxGenerations 最大代数
     * @return 最终种群
     */
    public Population<T> evolve(int maxGenerations) {
        Population<T> population = initializePopulation();

        for (int generation = 0; generation < maxGenerations; generation++) {
            evaluatePopulation(population);

            if (terminationCondition.isTerminated(generation, population)) {
                break;
            }

            population = nextGeneration(population);
        }

        evaluatePopulation(population);
        return population;
    }

    /**
     * 初始化种群
     */
    private Population<T> initializePopulation() {
        Population<T> population = new Population<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Individual<T> individual = genotypeFactory.create();
            individual.randomize();
            population.addIndividual(individual);
        }
        return population;
    }

    /**
     * 评估种群
     */
    private void evaluatePopulation(Population<T> population) {
        for (Individual<T> individual : population.getIndividuals()) {
            double fitness = fitnessFunction.evaluate(individual);
            individual.setFitness(fitness);
        }
        population.calculateTotalFitness();
    }

    /**
     * 产生下一代
     */
    private Population<T> nextGeneration(Population<T> population) {
        Population<T> newPopulation = new Population<>(populationSize);

        // 保留精英
        for (int i = 0; i < elitismCount && i < population.size(); i++) {
            newPopulation.addIndividual(population.getFittest(i).copy());
        }

        // 产生新个体
        while (newPopulation.size() < populationSize) {
            Individual<T> parent1 = selectionOperator.select(population);
            Individual<T> parent2 = selectionOperator.select(population);

            Individual<T> offspring;
            if (random.nextDouble() < crossoverRate) {
                @SuppressWarnings("unchecked")
                T[] childChromosome = (T[]) crossoverOperator.crossover(parent1, parent2)[0];
                offspring = new Individual<>(childChromosome);
            } else {
                offspring = parent1.copy();
            }

            if (random.nextDouble() < mutationRate) {
                mutationOperator.mutate(offspring, mutationRate);
            }

            newPopulation.addIndividual(offspring);
        }

        return newPopulation;
    }

    /**
     * 基因型工厂接口
     */
    @FunctionalInterface
    public interface GenotypeFactory<T> {
        Individual<T> create();
    }

    /**
     * 构建器
     */
    public static class Builder<T> {
        private int populationSize = 100;
        private double mutationRate = 0.01;
        private double crossoverRate = 0.9;
        private int elitismCount = 2;
        private GenotypeFactory<T> genotypeFactory;
        private FitnessFunction<T> fitnessFunction;
        private CrossoverOperator<T> crossoverOperator;
        private MutationOperator<T> mutationOperator;
        private SelectionOperator<T> selectionOperator;
        private TerminationCondition terminationCondition;

        public Builder<T> populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder<T> mutationRate(double mutationRate) {
            this.mutationRate = mutationRate;
            return this;
        }

        public Builder<T> crossoverRate(double crossoverRate) {
            this.crossoverRate = crossoverRate;
            return this;
        }

        public Builder<T> elitismCount(int elitismCount) {
            this.elitismCount = elitismCount;
            return this;
        }

        public Builder<T> genotypeFactory(GenotypeFactory<T> genotypeFactory) {
            this.genotypeFactory = genotypeFactory;
            return this;
        }

        public Builder<T> fitnessFunction(FitnessFunction<T> fitnessFunction) {
            this.fitnessFunction = fitnessFunction;
            return this;
        }

        public Builder<T> crossoverOperator(CrossoverOperator<T> crossoverOperator) {
            this.crossoverOperator = crossoverOperator;
            return this;
        }

        public Builder<T> mutationOperator(MutationOperator<T> mutationOperator) {
            this.mutationOperator = mutationOperator;
            return this;
        }

        public Builder<T> selectionOperator(SelectionOperator<T> selectionOperator) {
            this.selectionOperator = selectionOperator;
            return this;
        }

        public Builder<T> terminationCondition(TerminationCondition terminationCondition) {
            this.terminationCondition = terminationCondition;
            return this;
        }

        public GeneticAlgorithmEngine<T> build() {
            if (genotypeFactory == null) {
                throw new IllegalStateException("genotypeFactory is required");
            }
            if (fitnessFunction == null) {
                throw new IllegalStateException("fitnessFunction is required");
            }
            if (crossoverOperator == null) {
                throw new IllegalStateException("crossoverOperator is required");
            }
            if (mutationOperator == null) {
                throw new IllegalStateException("mutationOperator is required");
            }
            if (selectionOperator == null) {
                selectionOperator = pop -> pop.rouletteSelect();
            }
            if (terminationCondition == null) {
                terminationCondition = (gen, pop) -> pop.getFittest().getFitness() >= 0.999;
            }
            return new GeneticAlgorithmEngine<>(this);
        }
    }
}