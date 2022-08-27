package minecraft_simulator.v1_14.world;

public interface ISimulationFlagCommuniator {
  public void setSimulationFlagsIn(SimulationFlagsIn t);

  public void getSimulationFlagsOut(SimulationFlagsOut flagsOut);
}
