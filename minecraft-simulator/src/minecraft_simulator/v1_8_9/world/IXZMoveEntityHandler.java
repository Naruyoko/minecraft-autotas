package minecraft_simulator.v1_8_9.world;

import minecraft_simulator.v1_8_9.player.AbstractXZPlayer;

public interface IXZMoveEntityHandler<T extends AbstractXZPlayer> {
  public SimulationFlagsOut moveEntity(T player,double x,double z,SimulationFlagsIn flagsIn,SimulationFlagsOut flagsOut);
}
