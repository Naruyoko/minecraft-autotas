package minecraft_autotas.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import minecraft_autotas.BeamSearch;
import minecraft_autotas.IJudge;
import minecraft_autotas.Individual;
import minecraft_autotas.SmallZigzagJudge2;
import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;
import minecraft_simulator.v1_8_9.util.Utility;

public class BeamSearchTest1 {
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
      IJudge judge = searcher.getJudge();
      SprintingClearStoneXZPlayer player = judge.getStartingState();
      writer2.write("!property" + System.lineSeparator()
          + String.format("startPosition=%f,%f,%f", player.posX, 100.0, player.posZ) + System.lineSeparator()
          + String.format("startMotion=%f,%f,%f", player.velX, 0.0, player.velZ) + System.lineSeparator()
          + "startInvulnerabilityFrames=0" + System.lineSeparator() + "startGametype=NOT_SET" + System.lineSeparator()
          + String.format("mouseSensitivity=%f", SprintingClearStoneXZPlayer.mouseSensitivity) + System.lineSeparator()
          + String.format("mouseMaxSafeMovement=%d", BeamSearch.maximumMovementPerFrame) + System.lineSeparator()
          + String.format("tickLength=%d", searcher.getSolutionLength() + 20) + System.lineSeparator() + "rerecords=0"
          + System.lineSeparator() + "predictionRerecords=0" + System.lineSeparator() + "totalRerecords=0"
          + System.lineSeparator() + "fileFormatVersion=2" + System.lineSeparator() + "editorVersion=0.0.6"
          + System.lineSeparator() + "" + System.lineSeparator() + "!input" + System.lineSeparator()
          + "###|WASD_+^|Yaw,Pitch|Flags" + System.lineSeparator());
      for (int t = -1; t < searcher.getSolutionLength(); t++) {
        if (t >= 0) {
          BeamSearch.moveCameraWithDivision(player, result.mouseMovements[t]);
          BeamSearch.stepPlayer(player);
        }
        writer1.write(String.format("%3d %5s %-22s %-10s %-22s %-22s %-22s %-22s", t + 1,
            t >= 0 ? result.mouseMovements[t] : "-", Utility.padSignDouble(judge.score(player)),
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
