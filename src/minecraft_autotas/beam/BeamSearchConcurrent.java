package minecraft_autotas.beam;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;

public class BeamSearchConcurrent {
  public static final int maximumMovementPerTick = 1500;
  public static final int maximumMovementPerFrame = 900;
  private final int solutionLength;
  private final int beamWidth;
  private final int angleStep;
  final IJudge judge;
  private final ArrayBlockingQueue<Individual> lastBeam;
  private final PriorityQueue<Individual> beam;
  private final CalculationService calculationService;
  private Individual bestIndividual;
  private double bestScore;
  private double bestLosingScore;
  private long lastNanoTime;

  public BeamSearchConcurrent(int solutionLength, int beamWidth, int angleStep, IJudge judge) {
    this.solutionLength = solutionLength;
    this.beamWidth = beamWidth;
    this.angleStep = angleStep;
    this.judge = judge;
    lastBeam = new ArrayBlockingQueue<Individual>(beamWidth);
    beam = new PriorityQueue<Individual>();
    calculationService = new CalculationService(this);
  }

  public int getSolutionLength() { return this.solutionLength; }

  public int getBeamWidth() { return this.beamWidth; }

  public int getAngleStep() { return this.angleStep; }

  public IJudge getJudge() { return this.judge; }

  public ArrayBlockingQueue<Individual> getLastBeam() { return this.lastBeam; }

  public Individual getBestIndividual() { return this.bestIndividual; }

  public double getBestScore() { return this.bestIndividual.scoreCache; }

  public static void moveCameraWithDivision(SprintingClearStoneXZPlayer player, int pixels) {
    if (pixels < -maximumMovementPerFrame) {
      player.moveCamera(-maximumMovementPerFrame);
      player.moveCamera(pixels + maximumMovementPerFrame);
    } else if (pixels > maximumMovementPerFrame) {
      player.moveCamera(maximumMovementPerFrame);
      player.moveCamera(pixels - maximumMovementPerFrame);
    } else {
      player.moveCamera(pixels);
    }
  }

  public static void stepPlayer(SprintingClearStoneXZPlayer player) { player.step(-1.0F, 1.0F); }

  public synchronized double offerBeam(Individual newIndividual) {
    final double score = newIndividual.scoreCache;
    if (score <= bestLosingScore)
      return bestLosingScore;
    beam.add(newIndividual);
    double loserScore = Double.NEGATIVE_INFINITY;
    if (beam.size() > beamWidth) {
      Individual loser = beam.poll();
      if (loser.scoreCache > bestLosingScore)
        loserScore = bestLosingScore = loser.scoreCache;
    }
    if (score > bestScore) {
      bestIndividual = newIndividual;
      bestScore = score;
    }
    return loserScore;
  }

  public void init(boolean consoleOut) {
    SprintingClearStoneXZPlayer workingPlayer = judge.getStartingState();
    lastBeam.clear();
    beam.clear();
    beam.add(new Individual(new int[solutionLength], workingPlayer.clone(), judge.score(workingPlayer)));
    lastNanoTime = System.nanoTime();
  }

  class CalculationService implements Runnable {
    private final BeamSearchConcurrent searcher;
    private ExecutorService threadPool;
    private int t;
    private final int NTHREAD = 10;

    public CalculationService(BeamSearchConcurrent searcher) { this.searcher = searcher; }

    public void run() {
      threadPool = Executors.newCachedThreadPool();
      for (int i = 0; i < NTHREAD; i++) {
        threadPool.submit(new Handler(searcher, t));
      }
      threadPool.shutdown();
    }

    public void run(int t) {
      this.t = t;
      run();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      return threadPool.awaitTermination(timeout, unit);
    }
  }

  class Handler implements Runnable {
    private final BeamSearchConcurrent searcher;
    private final int t;
    private final SprintingClearStoneXZPlayer workingPlayer;
    private double bestLosingScoreCopy;

    public Handler(BeamSearchConcurrent searcher, int t) {
      this.searcher = searcher;
      this.t = t;
      this.workingPlayer = new SprintingClearStoneXZPlayer();
      this.bestLosingScoreCopy = Double.NEGATIVE_INFINITY;
    }

    public void run() {
      IJudge judge = searcher.getJudge();
      ArrayBlockingQueue<Individual> lastBeam = searcher.getLastBeam();
      while (true) {
        Individual individual = lastBeam.poll();
        if (individual == null)
          break;
        for (int x = Math.max(-maximumMovementPerTick, (int)((-135F - (individual.player.yaw + 45F))
            / (SprintingClearStoneXZPlayer.mouseMult * 0.15D))); x <= maximumMovementPerTick; x += angleStep) {
          SprintingClearStoneXZPlayer.copy(workingPlayer, individual.player);
          moveCameraWithDivision(workingPlayer, x);
          if (workingPlayer.yaw + 45F > 135F)
            break;
          stepPlayer(workingPlayer);
          double score = judge.score(workingPlayer);
          if (score <= bestLosingScoreCopy)
            continue;
          if (!judge.isValid(individual.player, workingPlayer))
            continue;
          Individual newIndividual = new Individual(individual.mouseMovements.clone(), workingPlayer.clone(), score);
          newIndividual.mouseMovements[t] = x;
          double newBestLosingScore = offerBeam(newIndividual);
          if (newBestLosingScore > bestLosingScoreCopy)
            bestLosingScoreCopy = newBestLosingScore;
        }
      }
    }
  }

  public Individual searchNextLevel(int t, boolean consoleOut) throws InterruptedException {
    lastBeam.clear();
    lastBeam.addAll(beam);
    bestIndividual = null;
    bestScore = Double.NEGATIVE_INFINITY;
    bestLosingScore = Double.NEGATIVE_INFINITY;
    long nanoTime = System.nanoTime();
    if (consoleOut)
      System.out
          .println(String.format("Tick %d, last beam size %d %d", t + 1, lastBeam.size(), nanoTime - lastNanoTime));
    lastNanoTime = nanoTime;
    calculationService.run(t);
    while (!calculationService.awaitTermination(10, TimeUnit.SECONDS)) {
      if (consoleOut)
        System.out.println(String.format("Best: %s %s (Remaining: %d)", Double.toString(bestScore),
            Arrays.toString(bestIndividual.mouseMovements), lastBeam.size()));
    }
    if (consoleOut)
      System.out.println(
          String.format("Best: %s %s", Double.toString(bestScore), Arrays.toString(bestIndividual.mouseMovements)));
    return bestIndividual;
  }

  public Individual search(boolean consoleOut) {
    init(consoleOut);
    try {
      for (int t = 0; t < solutionLength; t++)
        searchNextLevel(t, consoleOut);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return bestIndividual;
  }
}
