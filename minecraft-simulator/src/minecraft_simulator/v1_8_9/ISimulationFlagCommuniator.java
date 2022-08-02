package minecraft_simulator.v1_8_9;

public interface ISimulationFlagCommuniator {
  public void setSimulationFlags(SimulationFlagsIn t);
  public void getSimulationFlagsOut(SimulationFlagsOut flagsOut);
}
