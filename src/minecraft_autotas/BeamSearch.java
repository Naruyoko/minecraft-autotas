package minecraft_autotas;

import java.util.Arrays;
import java.util.Iterator;
import java.util.PriorityQueue;

import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;

public class BeamSearch {
  public static final int maximumMovementPerTick = 1500;
  public static final int maximumMovementPerFrame = 900;
  final int solutionLength;
  final int beamWidth;
  final int angleStep;
  final IJudge judge;
  PriorityQueue<Individual> finalBeam;
  Individual bestIndividual;

  public BeamSearch(int solutionLength, int beamWidth, int angleStep, IJudge judge) {
    this.solutionLength = solutionLength;
    this.beamWidth = beamWidth;
    this.angleStep = angleStep;
    this.judge = judge;
  }

  public int getSolutionLength() { return this.solutionLength; }

  public int getBeamWidth() { return this.beamWidth; }

  public int getAngleStep() { return this.angleStep; }

  public IJudge getJudge() { return this.judge; }

  public PriorityQueue<Individual> getFinalBeam() { return this.finalBeam; }

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

  public Individual search(boolean consoleOut) {
    SprintingClearStoneXZPlayer workingPlayer = judge.getStartingState();
    PriorityQueue<Individual> lastBeam = new PriorityQueue<Individual>();
    PriorityQueue<Individual> beam = new PriorityQueue<Individual>();
    beam.add(new Individual(new int[solutionLength], workingPlayer.clone(), judge.score(workingPlayer)));
    long lastNanoTime = System.nanoTime();
    for (int t = 0; t < solutionLength; t++) {
      finalBeam = lastBeam;
      lastBeam = beam;
      beam = finalBeam;
      beam.clear();
      bestIndividual = null;
      double bestScore = Double.NEGATIVE_INFINITY;
      double bestLosingScore = Double.NEGATIVE_INFINITY;
      long nanoTime = System.nanoTime();
      if (consoleOut)
        System.out
            .println(String.format("Tick %d, last beam size %d %d", t + 1, lastBeam.size(), nanoTime - lastNanoTime));
      lastNanoTime = nanoTime;
      Iterator<Individual> iterator = lastBeam.iterator();
      while (iterator.hasNext()) {
        Individual individual = iterator.next();
        for (int x = Math.max(-maximumMovementPerTick, (int)((-135F - (individual.player.yaw + 45F))
            / (SprintingClearStoneXZPlayer.mouseMult * 0.15D))); x <= maximumMovementPerTick; x += angleStep) {
          SprintingClearStoneXZPlayer.copy(workingPlayer, individual.player);
          moveCameraWithDivision(workingPlayer, x);
          if (workingPlayer.yaw + 45F > 135F)
            break;
          stepPlayer(workingPlayer);
          double score = judge.score(workingPlayer);
          if (score <= bestLosingScore)
            continue;
          if (!judge.isValid(individual.player, workingPlayer))
            continue;
          Individual newIndividual = new Individual(individual.mouseMovements.clone(), workingPlayer.clone(), score);
          newIndividual.mouseMovements[t] = x;
          beam.add(newIndividual);
          if (beam.size() > beamWidth) {
            Individual loser = beam.poll();
            if (loser.scoreCache > bestLosingScore)
              bestLosingScore = loser.scoreCache;
          }
          if (score > bestScore) {
            bestIndividual = newIndividual;
            bestScore = score;
          }
        }
      }
      if (consoleOut)
        System.out.println(
            String.format("Best: %s %s", Double.toString(bestScore), Arrays.toString(bestIndividual.mouseMovements)));
    }
    return bestIndividual;
  }

  public static void stepPlayer(SprintingClearStoneXZPlayer player) { player.step(-1.0F, 1.0F); }
}
