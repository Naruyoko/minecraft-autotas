package minecraft_simulator.v1_8_9.world;

public interface ISimulationFlagCommuniator {
  public void setSimulationFlagsIn(SimulationFlagsIn t);

  public void getSimulationFlagsOut(SimulationFlagsOut flagsOut);
}
