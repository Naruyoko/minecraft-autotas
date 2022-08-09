package minecraft_autotas.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractGeneticAlgorithm<Individual> {
  protected ThreadLocalRandom random;
  protected final int populationSize;
  protected int generation;
  protected List<Individual> lastPopulation;
  protected List<Individual> population;
  private List<Individual> nextPopulation;
  protected double[] lastFitnesses;
  protected double[] fitnesses;
  protected int bestIndividualIndex;
  protected Individual bestIndividual;
  protected double bestScore;

  public AbstractGeneticAlgorithm(int populationSize) { this.populationSize = populationSize; }

  public int getGeneration() { return generation; }

  public int getBestIndividualIndex() { return bestIndividualIndex; }

  public Individual getBestIndividual() { return bestIndividual; };

  public double getBestScore() { return bestScore; };

  public void init() {
    random = ThreadLocalRandom.current();
    generation = 0;
    lastPopulation = new ArrayList<>(populationSize);
    population = new ArrayList<>(populationSize);
    nextPopulation = null;
    for (int i = 0; i < populationSize; i++) {
      lastPopulation.add(null);
      population.add(null);
    }
    lastFitnesses = new double[populationSize];
    fitnesses = new double[populationSize];
    bestIndividualIndex = -1;
    bestIndividual = generateEmptyIndividual();
    bestScore = Double.NEGATIVE_INFINITY;
  }

  public abstract Individual generateRandomIndividual();

  public abstract Individual generateEmptyIndividual();

  public void nextGeneration() {
    generation++;
    nextPopulation = lastPopulation;
    lastPopulation = population;
    population = nextPopulation;
    System.arraycopy(fitnesses, 0, lastFitnesses, 0, populationSize);
    bestIndividualIndex = -1;
    for (int i = 0; i < populationSize; i++) {
      double fitness = generateAndScore(i);
      fitnesses[i] = fitness;
      if (fitness > bestScore) {
        bestScore = fitness;
        bestIndividualIndex = i;
      }
    }
    if (bestIndividualIndex >= 0) {
      bestIndividual = copyIndividual(bestIndividual, population.get(bestIndividualIndex));
    }
  }

  public double generateAndScore(int index) {
    Individual individual;
    if (generation == 1) {
      lastPopulation.set(index, generateEmptyIndividual());
      population.set(index, individual = generateRandomIndividual());
    } else if (index == 0) {
      population.set(index, individual = copyIndividual(population.get(index), bestIndividual));
    } else {
      population.set(index, individual = generateChild(randomParent(), randomParent(), index));
    }
    return evaluate(individual);
  }

  public abstract double evaluate(Individual individual);

  public abstract Individual copyIndividual(Individual target, Individual source);

  public Individual randomParent() { return lastPopulation.get(randomParentIndex()); }

  public abstract int randomParentIndex();

  public Individual generateChild(Individual parent1, Individual parent2, int childIndex) {
    Individual child = population.get(childIndex);
    child = crossover(parent1, parent2, child);
    child = mutate(child);
    return child;
  }

  public abstract Individual crossover(Individual parent1, Individual parent2, Individual child);

  public abstract Individual mutate(Individual child);

  public Individual stepGeneration() {
    nextGeneration();
    return bestIndividual;
  }
}
