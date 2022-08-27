package minecraft_simulator.v1_14.world;

import minecraft_simulator.v1_14.player.AbstractXZPlayer;

public interface IXZMoveEntityHandler<T extends AbstractXZPlayer> {
  public SimulationFlagsOut moveEntity(T player, double x, double z, SimulationFlagsIn flagsIn,
      SimulationFlagsOut flagsOut);
}
