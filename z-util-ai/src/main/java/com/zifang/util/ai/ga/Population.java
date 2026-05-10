package com.zifang.util.ai.ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 种群
 */
public class Population {

    private final List<Individual> individuals;
    private double totalFitness = -1;
    private final Random random;

    public Population() {
        this.individuals = new ArrayList<>();
        this.random = new Random();
    }

    public Population(int size) {
        this.individuals = new ArrayList<>(size);
        this.random = new Random();
    }

    public void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    public Individual getIndividual(int index) {
        return individuals.get(index);
    }

    public Individual setIndividual(int index, Individual individual) {
        return individuals.set(index, individual);
    }

    public int size() {
        return individuals.size();
    }

    public List<Individual> getIndividuals() {
        return new ArrayList<>(individuals);
    }

    public Individual getFittest() {
        return individuals.stream()
                .max(Comparator.comparingDouble(Individual::getFitness))
                .orElse(individuals.get(0));
    }

    public Individual getFittest(int offset) {
        individuals.sort(Comparator.comparingDouble(Individual::getFitness).reversed());
        if (offset < 0 || offset >= individuals.size()) {
            offset = 0;
        }
        return individuals.get(offset);
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public void setTotalFitness(double totalFitness) {
        this.totalFitness = totalFitness;
    }

    public double calculateTotalFitness() {
        this.totalFitness = individuals.stream()
                .mapToDouble(Individual::getFitness)
                .sum();
        return this.totalFitness;
    }

    public Individual rouletteSelect() {
        if (totalFitness <= 0) {
            return individuals.get(random.nextInt(individuals.size()));
        }
        double point = random.nextDouble() * totalFitness;
        double accumulated = 0;
        for (Individual individual : individuals) {
            accumulated += individual.getFitness();
            if (accumulated >= point) {
                return individual;
            }
        }
        return individuals.get(individuals.size() - 1);
    }

    public void shuffle() {
        for (int i = individuals.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Individual temp = individuals.get(i);
            individuals.set(i, individuals.get(j));
            individuals.set(j, temp);
        }
    }
}