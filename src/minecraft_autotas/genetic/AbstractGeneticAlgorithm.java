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
  protected List<Individual> nextPopulation;
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
    generation = 1;
    lastPopulation = null;
    population = new ArrayList<>(populationSize);
    nextPopulation = new ArrayList<>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      population.add(generateRandomIndividual());
      nextPopulation.add(generateEmptyIndividual());
    }
    fitnesses = new double[populationSize];
    bestIndividualIndex = -1;
    bestIndividual = generateEmptyIndividual();
    bestScore = Double.NEGATIVE_INFINITY;
  }

  public abstract Individual generateRandomIndividual();

  public abstract Individual generateEmptyIndividual();

  public void scorePopulation() {
    bestIndividualIndex = -1;
    for (int i = 0; i < populationSize; i++) {
      double fitness = evaluate(population.get(i));
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

  public abstract double evaluate(Individual individual);

  public abstract Individual copyIndividual(Individual target, Individual source);

  public void generateNextGeneration() {
    nextPopulation.set(0, copyIndividual(nextPopulation.get(0), bestIndividual));
    for (int childIndex = 1; childIndex < populationSize; childIndex++)
      nextPopulation.set(childIndex, generateChild(randomParent(), randomParent(), childIndex));
    lastPopulation = population;
    population = nextPopulation;
    nextPopulation = lastPopulation;
  }

  public Individual randomParent() { return population.get(randomParentIndex()); }

  public abstract int randomParentIndex();

  public Individual generateChild(Individual parent1, Individual parent2, int childIndex) {
    Individual child = nextPopulation.get(childIndex);
    child = crossover(parent1, parent2, child);
    child = mutate(child);
    return child;
  }

  public abstract Individual crossover(Individual parent1, Individual parent2, Individual child);

  public abstract Individual mutate(Individual child);

  public Individual stepGeneration() {
    scorePopulation();
    generateNextGeneration();
    generation++;
    return bestIndividual;
  }
}
