package minecraft_simulator.v1_8_9.test;

import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;
import minecraft_simulator.v1_8_9.util.Utility;

public class SprintingClearStoneXZPlayerTest1 {
  public static void main(String[] args) {
    for (int i = -900; i <= 900; i += 100) {
      for (float j : new float[] { -1F, 0F, 1F }) {
        SprintingClearStoneXZPlayer player = new SprintingClearStoneXZPlayer(0, 0, 0, 0, 0);
        player.moveCamera(i);
        player.step(j, 1F);
        System.out.println(String.format("%4d %2d %-22s %-22s %-22s %-22s", i, (int)j,
            Utility.padSignDouble(player.posX), Utility.padSignDouble(player.posZ), Utility.padSignDouble(player.velX),
            Utility.padSignDouble(player.velZ)));
      }
    }
    for (float j : new float[] { 0F, 1F }) {
      System.out.println();
      SprintingClearStoneXZPlayer player = new SprintingClearStoneXZPlayer(0, 0, 0, 0, 0);
      player.moveCamera((int)j * 300);
      for (int t = 0; t < 10; t++) {
        player.step(j, 1F);
        System.out.println(String.format("%2d %2d %-22s %-22s %-22s %-22s", (int)j, t + 1,
            Utility.padSignDouble(player.posX), Utility.padSignDouble(player.posZ), Utility.padSignDouble(player.velX),
            Utility.padSignDouble(player.velZ)));
      }
    }
  }
}
