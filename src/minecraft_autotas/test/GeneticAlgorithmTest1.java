package minecraft_autotas.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import minecraft_autotas.genetic.AbstractGeneticAlgorithm;
import minecraft_simulator.v1_8_9.player.KeyConstants;
import minecraft_simulator.v1_8_9.player.StoneXZPlayer;
import minecraft_simulator.v1_8_9.util.Utility;
import minecraft_simulator.v1_8_9.world.AbstractXZStoneGrid;
import minecraft_simulator.v1_8_9.world.IXZMoveEntityHandler;
import minecraft_simulator.v1_8_9.world.XZMoveEntityHandlerFromCollidableSimpleGround;

public class GeneticAlgorithmTest1 {
  static final int maximumMovementPerFrame = 900;

  static class Ground extends AbstractXZStoneGrid {
    final boolean[] pathShape = new boolean[] { true, false, false, true, true, true, true, false, false, true, false,
        false, true, true, true, true, false, false };

    public Ground() {}

    @Override
    public boolean hasBlockAt(int x, int z) { return -3 <= x && x < 0 && z >= 0 && pathShape[(-1 - x) * 6 + z % 6]; }

  };

  static class Individual {
    StoneXZPlayer player;
    int[] inputs;
  }

  static class GeneticAlgorithm extends AbstractGeneticAlgorithm<Individual> {
    final int maxGeneration;
    final int inputLength;
    final float mutationChance;
    final float chooseProbabilityOffset;
    final StoneXZPlayer startingState;
    final IXZMoveEntityHandler<? super StoneXZPlayer> moveEntityHandler;
    int effectiveInputLength;

    public GeneticAlgorithm(int populationSize, int maxGeneration, int inputLength, float mutationChance,
        float chooseProbabilityOffset, StoneXZPlayer startingState,
        IXZMoveEntityHandler<? super StoneXZPlayer> moveEntityHandler) {
      super(populationSize);
      this.maxGeneration = maxGeneration;
      this.inputLength = inputLength;
      this.mutationChance = mutationChance;
      this.chooseProbabilityOffset = chooseProbabilityOffset;
      this.startingState = startingState;
      this.moveEntityHandler = moveEntityHandler;
    }

    public int getEffectiveInputLength() { return effectiveInputLength; }

    @Override
    public Individual generateRandomIndividual() {
      Individual individual = new Individual();
      individual.player = startingState.clone();
      individual.inputs = new int[inputLength];
      for (int i = 0; i < inputLength; i++)
        individual.inputs[i] = random.nextInt(-1200, 1200);
      return individual;
    }

    @Override
    public Individual generateEmptyIndividual() {
      Individual individual = new Individual();
      individual.player = startingState.clone();
      individual.inputs = new int[inputLength];
      return individual;
    }

    public void moveCameraWithDivision(StoneXZPlayer player, int pixels) {
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

    public void stepPlayer(StoneXZPlayer player, int pixels) {
      moveCameraWithDivision(player, pixels);
      player.step(moveEntityHandler, KeyConstants.RIGHT, KeyConstants.FORWARD, KeyConstants.DOWN, KeyConstants.UP);
    }

    public double immediateFitness(StoneXZPlayer player) { return player.posZ; }

    @Override
    public double evaluate(Individual individual) {
      StoneXZPlayer player = individual.player;
      StoneXZPlayer.copy(player, startingState);
      for (int t = 0; t < effectiveInputLength; t++) {
        stepPlayer(player, individual.inputs[t]);
        if (!player.onGround)
          break;
      }
      return immediateFitness(player);
    }

    @Override
    public Individual copyIndividual(Individual target, Individual source) {
      StoneXZPlayer.copy(target.player, source.player);
      System.arraycopy(source.inputs, 0, target.inputs, 0, effectiveInputLength);
      return target;
    }

    @Override
    public int randomParentIndex() {
      while (true) {
        int choice = random.nextInt(populationSize);
        float probability = chooseProbabilityOffset
            / (chooseProbabilityOffset + (float)(bestScore - fitnesses[choice]));
        if (random.nextFloat() < probability)
          return choice;
      }
    }

    @Override
    public Individual crossover(Individual parent1, Individual parent2, Individual child) {
      int pivot = random.nextInt(effectiveInputLength);
      System.arraycopy(parent1.inputs, 0, child.inputs, 0, pivot);
      System.arraycopy(parent2.inputs, pivot, child.inputs, pivot, effectiveInputLength - pivot);
      return child;
    }

    @Override
    public Individual mutate(Individual child) {
      final int fullSpinPixels = 2400;
      final int halfSpinPixels = fullSpinPixels / 2;
      for (int i = 0; i < effectiveInputLength; i++) {
        if (random.nextFloat() < mutationChance) {
          int newInput = child.inputs[i] + (int)(random.nextGaussian() * 100);
          if (newInput >= halfSpinPixels)
            newInput -= fullSpinPixels;
          else if (newInput < -halfSpinPixels)
            newInput += fullSpinPixels;
          child.inputs[i] = newInput;
        }
      }
      return child;
    }

    @Override
    public Individual stepGeneration() {
      effectiveInputLength = Math.max(Math.min(inputLength * generation * 2 / maxGeneration, inputLength),
          inputLength / 10);
      return super.stepGeneration();
    }

  }

  public static void main(String[] args) {
    Date startDate = new Date();
    int populationSize = 1000;
    int maxGeneration = 10000;
    int inputLength = 100;
    float mutationChance = 0.01F;
    float chooseProbabilityOffset = 1.0F;
    StoneXZPlayer startingState = new StoneXZPlayer(-0.5, 0.5, 0, 0, 0);
    Ground ground = new Ground();
    IXZMoveEntityHandler<? super StoneXZPlayer> moveEntityHandler = new XZMoveEntityHandlerFromCollidableSimpleGround(
        ground);
    GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(populationSize, maxGeneration, inputLength, mutationChance,
        chooseProbabilityOffset, startingState, moveEntityHandler);
    geneticAlgorithm.init();
    long lastNanoTime = System.nanoTime();
    while (geneticAlgorithm.getGeneration() <= maxGeneration) {
      geneticAlgorithm.stepGeneration();
      if (geneticAlgorithm.getBestIndividualIndex() >= 0)
        System.out.println(String.format("New best! %f : %s", geneticAlgorithm.getBestScore(), Arrays.toString(Arrays
            .copyOfRange(geneticAlgorithm.getBestIndividual().inputs, 0, geneticAlgorithm.getEffectiveInputLength()))));
      long nanoTime = System.nanoTime();
      System.out.println(String.format("Generation %d took %d ns. Best: %f", geneticAlgorithm.getGeneration() - 1,
          nanoTime - lastNanoTime, geneticAlgorithm.getBestScore()));
      lastNanoTime = nanoTime;
    }
    Individual bestIndividual = geneticAlgorithm.getBestIndividual();
    try {
      DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
      String formattedDate = dateFormat.format(startDate);
      BufferedWriter writer1 = new BufferedWriter(
          new FileWriter(String.format("output/result-%s.txt", formattedDate), false));
      BufferedWriter writer2 = new BufferedWriter(
          new FileWriter(String.format("output/result-%s.mcsim", formattedDate), false));
      writer1.write(Arrays.toString(bestIndividual.inputs));
      writer1.newLine();
      writer1.newLine();
      StoneXZPlayer player = startingState.clone();
      writer2.write("!property" + System.lineSeparator()
          + String.format("startPosition=%f,%f,%f", player.posX, 110.0, player.posZ) + System.lineSeparator()
          + String.format("startMotion=%f,%f,%f", player.velX, 0.0, player.velZ) + System.lineSeparator()
          + "startInvulnerabilityFrames=0" + System.lineSeparator() + "startGametype=NOT_SET" + System.lineSeparator()
          + String.format("mouseSensitivity=%f", player.getMouseSensitivity()) + System.lineSeparator()
          + String.format("mouseMaxSafeMovement=%d", maximumMovementPerFrame) + System.lineSeparator()
          + String.format("tickLength=%d", inputLength + 1) + System.lineSeparator() + "rerecords=0"
          + System.lineSeparator() + "predictionRerecords=0" + System.lineSeparator() + "totalRerecords=0"
          + System.lineSeparator() + "fileFormatVersion=2" + System.lineSeparator() + "editorVersion=0.0.6"
          + System.lineSeparator() + "" + System.lineSeparator() + "!input" + System.lineSeparator()
          + "###|WASD_+^|Yaw,Pitch|Flags" + System.lineSeparator());
      for (int t = -1; t < inputLength; t++) {
        if (t >= 0)
          geneticAlgorithm.stepPlayer(player, bestIndividual.inputs[t]);
        writer1.write(String.format("%3d %5s %-22s %-10s %-22s %-22s %-22s %-22s", t + 1,
            t >= 0 ? bestIndividual.inputs[t] : "-", Utility.padSignDouble(geneticAlgorithm.immediateFitness(player)),
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
