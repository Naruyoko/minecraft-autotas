package minecraft_zigzag_1_8_9;

import minecraft_simulator.v1_8_9.SprintingClearStoneXZPlayer;

public class Individual {
  public int[] mouseMovements;
  public SprintingClearStoneXZPlayer player;
  public double scoreCache;
  public Individual(int[] mouseMovements,SprintingClearStoneXZPlayer player,double scoreCache){
    this.mouseMovements=mouseMovements;
    this.player=player;
    this.scoreCache=scoreCache;
  }
}
