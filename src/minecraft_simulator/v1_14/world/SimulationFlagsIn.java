package minecraft_simulator.v1_14.world;

// TODO: update
public class SimulationFlagsIn {
  public boolean onGround;
  public boolean isSneaking;
  public boolean isInWeb;
  public boolean inWater;
  public boolean checkSneaking = true;
  public boolean checkStepping = true;
  public boolean checkWater = false;

  public static void copy(SimulationFlagsIn target, SimulationFlagsIn source) {
    target.onGround = source.onGround;
    target.isSneaking = source.isSneaking;
    target.isInWeb = source.isInWeb;
    target.inWater = source.inWater;
    target.checkSneaking = source.checkSneaking;
    target.checkStepping = source.checkStepping;
    target.checkWater = source.checkWater;
  }
}