package minecraft_simulator.v1_8_9.player;

import minecraft_simulator.v1_8_9.world.AbstractXYZBlockGrid;

public interface IPushedByWater {
  public void handleWaterMovement(AbstractXYZBlockGrid blockGrid);
}
