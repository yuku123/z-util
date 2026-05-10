package com.zifang.util.ai.ga;

import java.util.Random;

/**
 * 遗传算法引擎
 */
public class GeneticAlgorithmEngine {

    private final int populationSize;
    private final double mutationRate;
    private final double crossoverRate;
    private final int elitismCount;

    private final IndividualFactory individualFactory;
    private final FitnessFunction fitnessFunction;
    private final CrossoverOperator crossoverOperator;
    private final MutationOperator mutationOperator;
    private final SelectionOperator selectionOperator;
    private final TerminationCondition terminationCondition;

    private final Random random;

    private GeneticAlgorithmEngine(Builder builder) {
        this.populationSize = builder.populationSize;
        this.mutationRate = builder.mutationRate;
        this.crossoverRate = builder.crossoverRate;
        this.elitismCount = builder.elitismCount;
        this.individualFactory = builder.individualFactory;
        this.fitnessFunction = builder.fitnessFunction;
        this.crossoverOperator = builder.crossoverOperator;
        this.mutationOperator = builder.mutationOperator;
        this.selectionOperator = builder.selectionOperator;
        this.terminationCondition = builder.terminationCondition;
        this.random = new Random();
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("unchecked")
    public Population evolve(int maxGenerations) {
        Population population = initializePopulation();

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

    private Population initializePopulation() {
        Population population = new Population(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Individual individual = individualFactory.create();
            individual.randomize();
            population.addIndividual(individual);
        }
        return population;
    }

    private void evaluatePopulation(Population population) {
        for (Individual individual : population.getIndividuals()) {
            double fitness = fitnessFunction.evaluate(individual);
            individual.setFitness(fitness);
        }
        population.calculateTotalFitness();
    }

    private Population nextGeneration(Population population) {
        Population newPopulation = new Population(populationSize);

        // 保留精英
        for (int i = 0; i < elitismCount && i < population.size(); i++) {
            newPopulation.addIndividual(population.getFittest(i).copy());
        }

        // 产生新个体
        while (newPopulation.size() < populationSize) {
            Individual parent1 = selectionOperator.select(population);
            Individual parent2 = selectionOperator.select(population);

            Individual offspring;
            if (random.nextDouble() < crossoverRate) {
                Individual[] children = crossoverOperator.crossover(parent1, parent2);
                offspring = children[0];
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

    @FunctionalInterface
    public interface IndividualFactory {
        Individual create();
    }

    public static class Builder {
        private int populationSize = 100;
        private double mutationRate = 0.01;
        private double crossoverRate = 0.9;
        private int elitismCount = 2;
        private IndividualFactory individualFactory;
        private FitnessFunction fitnessFunction;
        private CrossoverOperator crossoverOperator;
        private MutationOperator mutationOperator;
        private SelectionOperator selectionOperator;
        private TerminationCondition terminationCondition;

        public Builder populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder mutationRate(double mutationRate) {
            this.mutationRate = mutationRate;
            return this;
        }

        public Builder crossoverRate(double crossoverRate) {
            this.crossoverRate = crossoverRate;
            return this;
        }

        public Builder elitismCount(int elitismCount) {
            this.elitismCount = elitismCount;
            return this;
        }

        public Builder individualFactory(IndividualFactory individualFactory) {
            this.individualFactory = individualFactory;
            return this;
        }

        public Builder fitnessFunction(FitnessFunction fitnessFunction) {
            this.fitnessFunction = fitnessFunction;
            return this;
        }

        public Builder crossoverOperator(CrossoverOperator crossoverOperator) {
            this.crossoverOperator = crossoverOperator;
            return this;
        }

        public Builder mutationOperator(MutationOperator mutationOperator) {
            this.mutationOperator = mutationOperator;
            return this;
        }

        public Builder selectionOperator(SelectionOperator selectionOperator) {
            this.selectionOperator = selectionOperator;
            return this;
        }

        public Builder terminationCondition(TerminationCondition terminationCondition) {
            this.terminationCondition = terminationCondition;
            return this;
        }

        public GeneticAlgorithmEngine build() {
            if (individualFactory == null) {
                throw new IllegalStateException("individualFactory is required");
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
            return new GeneticAlgorithmEngine(this);
        }
    }
}