package minecraft_simulator.v1_8_9.world;

public class SimulationFlagsIn {
  public boolean onGround;
  public boolean isSneaking;
  public boolean isInWeb;
  public boolean checkSneaking = true;
  public boolean checkStepping = true;

  public static void copy(SimulationFlagsIn target, SimulationFlagsIn source) {
    target.onGround = source.onGround;
    target.isSneaking = source.isSneaking;
    target.isInWeb = source.isInWeb;
    target.checkSneaking = source.checkSneaking;
    target.checkStepping = source.checkStepping;
  }
}