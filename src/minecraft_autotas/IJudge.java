package minecraft_autotas;

import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;

public interface IJudge {
  public SprintingClearStoneXZPlayer getStartingState();

  public boolean isValid(SprintingClearStoneXZPlayer lastPlayer, SprintingClearStoneXZPlayer currentPlayer);

  public double score(SprintingClearStoneXZPlayer player);
}
