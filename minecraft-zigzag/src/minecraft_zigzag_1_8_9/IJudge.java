package minecraft_zigzag_1_8_9;

import minecraft_simulator.v1_8_9.SprintingClearStoneXZPlayer;

public interface IJudge {
  public SprintingClearStoneXZPlayer getStartingState();
  public boolean isValid(SprintingClearStoneXZPlayer lastPlayer,SprintingClearStoneXZPlayer currentPlayer);
  public double score(SprintingClearStoneXZPlayer player);
}
