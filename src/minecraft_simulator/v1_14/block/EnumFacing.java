package minecraft_simulator.v1_14.block;

// TODO: update
public enum EnumFacing {
  NORTH(2, AxisDirection.NEGATIVE, Axis.Z, 0, -1), SOUTH(0, AxisDirection.POSITIVE, Axis.Z, 0, 1),
  WEST(1, AxisDirection.NEGATIVE, Axis.X, -1, 0), EAST(3, AxisDirection.POSITIVE, Axis.X, 1, 0);

  public final int horizontalIndex;
  public final AxisDirection axisDirection;
  public final Axis axis;
  public final int offsetX;
  public final int offsetZ;

  private EnumFacing(int horizontalIndex, AxisDirection axisDirection, Axis axis, int offsetX, int offsetZ) {
    this.horizontalIndex = horizontalIndex;
    this.axisDirection = axisDirection;
    this.axis = axis;
    this.offsetX = offsetX;
    this.offsetZ = offsetZ;
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

  public enum Plane {
    HORIZONTAL;

    public EnumFacing[] asArray() {
      switch (this) {
      case HORIZONTAL:
        return new EnumFacing[] { NORTH, EAST, SOUTH, WEST };
      default:
        return null;
      }
    }
  }
}
