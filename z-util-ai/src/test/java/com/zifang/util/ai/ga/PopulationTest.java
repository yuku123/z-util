package com.zifang.util.ai.ga;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Population 类测试
 */
public class PopulationTest {

    @Test
    public void testDefaultConstructor() {
        Population population = new Population();
        assertNotNull(population);
        assertEquals(0, population.size());
        assertEquals(-1, population.getTotalFitness(), 0.0001);
    }

    @Test
    public void testConstructorWithSize() {
        Population population = new Population(10);
        assertNotNull(population);
        assertEquals(0, population.size());
    }

    @Test
    public void testAddIndividual() {
        Population population = new Population();
        Individual individual = new Individual();

        population.addIndividual(individual);

        assertEquals(1, population.size());
    }

    @Test
    public void testAddMultipleIndividuals() {
        Population population = new Population();

        for (int i = 0; i < 5; i++) {
            population.addIndividual(new Individual());
        }

        assertEquals(5, population.size());
    }

    @Test
    public void testGetIndividual() {
        Population population = new Population();
        Individual individual1 = new Individual();
        Individual individual2 = new Individual();
        individual1.setFitness(10.0);
        individual2.setFitness(20.0);

        population.addIndividual(individual1);
        population.addIndividual(individual2);

        assertSame(individual1, population.getIndividual(0));
        assertSame(individual2, population.getIndividual(1));
    }

    @Test
    public void testSetIndividual() {
        Population population = new Population();
        Individual original = new Individual();
        Individual replacement = new Individual();
        original.setFitness(10.0);
        replacement.setFitness(20.0);

        population.addIndividual(original);
        Individual result = population.setIndividual(0, replacement);

        assertSame(original, result);
        assertSame(replacement, population.getIndividual(0));
    }

    @Test
    public void testGetIndividualsReturnsCopy() {
        Population population = new Population();
        Individual individual = new Individual();
        population.addIndividual(individual);

        var individuals = population.getIndividuals();
        individuals.clear();

        assertEquals(1, population.size());
    }

    @Test
    public void testGetTotalFitness() {
        Population population = new Population();
        assertEquals(-1, population.getTotalFitness(), 0.0001);
    }

    @Test
    public void testSetTotalFitness() {
        Population population = new Population();
        population.setTotalFitness(100.5);
        assertEquals(100.5, population.getTotalFitness(), 0.0001);
    }

    @Test
    public void testCalculateTotalFitness() {
        Population population = new Population();

        Individual ind1 = new Individual();
        ind1.setFitness(10.0);
        population.addIndividual(ind1);

        Individual ind2 = new Individual();
        ind2.setFitness(20.0);
        population.addIndividual(ind2);

        Individual ind3 = new Individual();
        ind3.setFitness(30.0);
        population.addIndividual(ind3);

        double totalFitness = population.calculateTotalFitness();

        assertEquals(60.0, totalFitness, 0.0001);
        assertEquals(60.0, population.getTotalFitness(), 0.0001);
    }

    @Test
    public void testCalculateTotalFitnessWithZeroFitness() {
        Population population = new Population();

        Individual ind1 = new Individual();
        ind1.setFitness(0.0);
        population.addIndividual(ind1);

        Individual ind2 = new Individual();
        ind2.setFitness(0.0);
        population.addIndividual(ind2);

        double totalFitness = population.calculateTotalFitness();

        assertEquals(0.0, totalFitness, 0.0001);
    }

    @Test
    public void testCalculateTotalFitnessWithNegativeFitness() {
        Population population = new Population();

        Individual ind1 = new Individual();
        ind1.setFitness(-5.0);
        population.addIndividual(ind1);

        Individual ind2 = new Individual();
        ind2.setFitness(-10.0);
        population.addIndividual(ind2);

        double totalFitness = population.calculateTotalFitness();

        assertEquals(-15.0, totalFitness, 0.0001);
    }

    @Test
    public void testGetFittest() {
        Population population = new Population();

        Individual ind1 = new Individual();
        ind1.setFitness(10.0);
        population.addIndividual(ind1);

        Individual ind2 = new Individual();
        ind2.setFitness(30.0);
        population.addIndividual(ind2);

        Individual ind3 = new Individual();
        ind3.setFitness(20.0);
        population.addIndividual(ind3);

        Individual fittest = population.getFittest();

        assertSame(ind2, fittest);
        assertEquals(30.0, fittest.getFitness(), 0.0001);
    }

    @Test
    public void testGetFittestWithSingleIndividual() {
        Population population = new Population();
        Individual individual = new Individual();
        individual.setFitness(42.0);
        population.addIndividual(individual);

        Individual fittest = population.getFittest();

        assertSame(individual, fittest);
    }

    @Test
    public void testGetFittestInt() {
        Population population = new Population();

        Individual ind1 = new Individual();
        ind1.setFitness(10.0);
        population.addIndividual(ind1);

        Individual ind2 = new Individual();
        ind2.setFitness(30.0);
        population.addIndividual(ind2);

        Individual ind3 = new Individual();
        ind3.setFitness(20.0);
        population.addIndividual(ind3);

        // Get 2nd fittest (offset 1)
        Individual secondFittest = population.getFittest(1);

        assertEquals(20.0, secondFittest.getFitness(), 0.0001);
    }

    @Test
    public void testGetFittestIntWithInvalidOffset() {
        Population population = new Population();

        Individual ind1 = new Individual();
        ind1.setFitness(10.0);
        population.addIndividual(ind1);

        Individual ind2 = new Individual();
        ind2.setFitness(30.0);
        population.addIndividual(ind2);

        // Negative offset should return fittest
        Individual result = population.getFittest(-1);
        assertSame(ind2, result);

        // Offset too large should return first
        Individual result2 = population.getFittest(100);
        assertNotNull(result2);
    }

    @Test
    public void testRouletteSelectWithPositiveTotalFitness() {
        Population population = new Population();

        for (int i = 0; i < 10; i++) {
            Individual ind = new Individual();
            ind.setFitness(10.0);
            population.addIndividual(ind);
        }

        population.calculateTotalFitness();

        // Should return some individual
        Individual selected = population.rouletteSelect();
        assertNotNull(selected);
    }

    @Test
    public void testRouletteSelectWithZeroTotalFitness() {
        Population population = new Population();

        for (int i = 0; i < 5; i++) {
            Individual ind = new Individual();
            ind.setFitness(0.0);
            population.addIndividual(ind);
        }

        // Should still return an individual (random selection)
        Individual selected = population.rouletteSelect();
        assertNotNull(selected);
    }

    @Test
    public void testRouletteSelectWithNegativeTotalFitness() {
        Population population = new Population();

        for (int i = 0; i < 5; i++) {
            Individual ind = new Individual();
            ind.setFitness(-5.0);
            population.addIndividual(ind);
        }

        population.calculateTotalFitness();

        // With negative total fitness, should fall back to random
        Individual selected = population.rouletteSelect();
        assertNotNull(selected);
    }

    @Test
    public void testShuffle() {
        Population population = new Population();

        // Add individuals with distinct fitness values
        for (int i = 0; i < 10; i++) {
            Individual ind = new Individual();
            ind.setFitness(i);
            population.addIndividual(ind);
        }

        // Remember original order
        double[] originalOrder = new double[10];
        for (int i = 0; i < 10; i++) {
            originalOrder[i] = population.getIndividual(i).getFitness();
        }

        // Shuffle
        population.shuffle();

        // Note: shuffle might preserve order by chance, but very unlikely for 10 elements
        // Just verify all individuals still exist and have correct fitness
        for (int i = 0; i < 10; i++) {
            Individual ind = population.getIndividual(i);
            assertTrue(ind.getFitness() >= 0 && ind.getFitness() < 10);
        }
    }

    @Test
    public void testShuffleWithEmptyPopulation() {
        Population population = new Population();
        population.shuffle(); // Should not throw
        assertEquals(0, population.size());
    }

    @Test
    public void testShuffleWithSingleIndividual() {
        Population population = new Population();
        Individual ind = new Individual();
        ind.setFitness(42.0);
        population.addIndividual(ind);

        population.shuffle();

        assertEquals(1, population.size());
        assertEquals(42.0, population.getIndividual(0).getFitness(), 0.0001);
    }
}
