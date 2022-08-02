package minecraft_simulator.v1_8_9;

public interface IXZMoveEntityHandler<T extends AbstractXZPlayer> {
  public SimulationFlagsOut moveEntity(T player,double x,double z,SimulationFlagsIn flagsIn,SimulationFlagsOut flagsOut);
}
