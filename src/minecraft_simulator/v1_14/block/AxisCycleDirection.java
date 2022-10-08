package minecraft_simulator.v1_14.block;

/**
 * See {net.minecraft.util.math.AxisCycleDirection}
 * 
 * Rotates axis along X->Y->Z cycle
 */
public enum AxisCycleDirection {
  NONE {
    @Override
    public int choose(int x, int y, int z, Direction.Axis axis) { return axis.choose(x, y, z); }

    @Override
    public Direction.Axis cycle(Direction.Axis axis) { return axis; }

    @Override
    public AxisCycleDirection opposite() { return this; }
  },
  FORWARD {
    @Override
    public int choose(int x, int y, int z, Direction.Axis axis) { return axis.choose(z, x, y); }

    @Override
    public Direction.Axis cycle(Direction.Axis axis) { return AXES[Math.floorMod(axis.ordinal() + 1, 3)]; }

    @Override
    public AxisCycleDirection opposite() { return BACKWARD; }
  },
  BACKWARD {
    @Override
    public int choose(int x, int y, int z, Direction.Axis axis) { return axis.choose(y, z, x); }

    @Override
    public Direction.Axis cycle(Direction.Axis axis) { return AXES[Math.floorMod(axis.ordinal() - 1, 3)]; }

    @Override
    public AxisCycleDirection opposite() { return FORWARD; }
  };

  public static final Direction.Axis[] AXES;
  public static final AxisCycleDirection[] VALUES;

  /**
   * See {net.minecraft.util.math.AxisCycleDirection.choose(int, int, int, Axis)}
   * 
   * @param x
   * @param y
   * @param z
   * @param axis
   * @return Chosen input with axis cycled
   */
  public abstract int choose(int x, int y, int z, Direction.Axis axis);

  /**
   * See {net.minecraft.util.math.AxisCycleDirection.cycle(Axis)}
   * 
   * @param axis
   * @return Cycled axis
   */
  public abstract Direction.Axis cycle(Direction.Axis axis);

  /**
   * See {net.minecraft.util.math.AxisCycleDirection.opposite()}
   * 
   * @return The oppposite cycle direction
   */
  public abstract AxisCycleDirection opposite();

  /**
   * See {net.minecraft.util.math.AxisCycleDirection.between(Axis, Axis)}
   * 
   * @param axis
   * @param axis2
   * @return The cycle direction that maps the first axis to the second
   */
  public static AxisCycleDirection between(Direction.Axis axis, Direction.Axis axis2) {
    return VALUES[Math.floorMod(axis2.ordinal() - axis.ordinal(), 3)];
  }

  static {
    AXES = Direction.Axis.values();
    VALUES = AxisCycleDirection.values();
  }
}
