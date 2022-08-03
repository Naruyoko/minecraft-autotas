package minecraft_autotas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;

import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;
import minecraft_simulator.v1_8_9.util.Utility;

public class BeamSearch {
  static final int maximumMovementPerTick = 1500;
  static final int maximumMovementPerFrame = 900;
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

  private static void stepPlayer(SprintingClearStoneXZPlayer player) { player.step(-1.0F, 1.0F); }

  public static void main(String[] args) {
    Date date = new Date();
    // BeamSearch searcher=new BeamSearch(100, 1000000, 100, new SmallZigzagJudge());
    BeamSearch searcher = new BeamSearch(1, 1000000, 100, new SmallZigzagJudge2());
    Individual result = searcher.search(true);
    try {
      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      String formattedDate = dateFormat.format(date);
      BufferedWriter writer1 = new BufferedWriter(
          new FileWriter(String.format("output/result-%s.txt", formattedDate), false));
      BufferedWriter writer2 = new BufferedWriter(
          new FileWriter(String.format("output/result-%s.mcsim", formattedDate), false));
      writer1.write(Arrays.toString(result.mouseMovements));
      writer1.newLine();
      writer1.newLine();
      SprintingClearStoneXZPlayer player = searcher.judge.getStartingState();
      writer2.write("!property" + System.lineSeparator()
          + String.format("startPosition=%f,%f,%f", player.posX, 100.0, player.posZ) + System.lineSeparator()
          + String.format("startMotion=%f,%f,%f", player.velX, 0.0, player.velZ) + System.lineSeparator()
          + "startInvulnerabilityFrames=0" + System.lineSeparator() + "startGametype=NOT_SET" + System.lineSeparator()
          + String.format("mouseSensitivity=%f", SprintingClearStoneXZPlayer.mouseSensitivity) + System.lineSeparator()
          + String.format("mouseMaxSafeMovement=%d", maximumMovementPerFrame) + System.lineSeparator()
          + String.format("tickLength=%d", searcher.getSolutionLength() + 20) + System.lineSeparator() + "rerecords=0"
          + System.lineSeparator() + "predictionRerecords=0" + System.lineSeparator() + "totalRerecords=0"
          + System.lineSeparator() + "fileFormatVersion=2" + System.lineSeparator() + "editorVersion=0.0.6"
          + System.lineSeparator() + "" + System.lineSeparator() + "!input" + System.lineSeparator()
          + "###|WASD_+^|Yaw,Pitch|Flags" + System.lineSeparator());
      for (int t = -1; t < searcher.getSolutionLength(); t++) {
        if (t >= 0) {
          moveCameraWithDivision(player, result.mouseMovements[t]);
          stepPlayer(player);
        }
        writer1.write(String.format("%3d %5s %-22s %-10s %-22s %-22s %-22s %-22s", t + 1,
            t >= 0 ? result.mouseMovements[t] : "-", Utility.padSignDouble(searcher.judge.score(player)),
            Utility.padSignFloat(player.yaw), Utility.padSignDouble(player.posX), Utility.padSignDouble(player.posZ),
            Utility.padSignDouble(player.velX), Utility.padSignDouble(player.velZ)));
        writer1.newLine();
        if (t >= 0) {
          writer2.write(
              String.format("%d|W..D..^|%s,%s|", t + 1, Double.toString((double)player.yaw), Double.toString(0.0D)));
          writer2.newLine();
        }
      }
      writer1.close();
      writer2.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
