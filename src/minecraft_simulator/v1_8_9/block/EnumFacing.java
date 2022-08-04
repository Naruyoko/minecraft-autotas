package minecraft_simulator.v1_8_9.block;

public enum EnumFacing {
  NORTH(2, AxisDirection.NEGATIVE, Axis.Z), SOUTH(0, AxisDirection.POSITIVE, Axis.Z),
  WEST(1, AxisDirection.NEGATIVE, Axis.X), EAST(3, AxisDirection.POSITIVE, Axis.X);

  public final int horizontalIndex;
  public final AxisDirection axisDirection;
  public final Axis axis;

  private EnumFacing(int horizontalIndex, AxisDirection axisDirection, Axis axis) {
    this.horizontalIndex = horizontalIndex;
    this.axisDirection = axisDirection;
    this.axis = axis;
  }

  public static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
  static {
    for (EnumFacing facing : values())
      HORIZONTALS[facing.horizontalIndex] = facing;
  }

  public enum Axis {
    X, Y, Z;
  }

  public enum AxisDirection {
    POSITIVE, NEGATIVE;
  }
}
