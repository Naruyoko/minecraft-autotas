package minecraft_simulator.v1_14.block;

import java.util.Iterator;

/**
 * See {net.minecraft.util.math.Direction}
 */
public enum Direction {
  NORTH(2, AxisDirection.NEGATIVE, Axis.Z, 0, -1), SOUTH(0, AxisDirection.POSITIVE, Axis.Z, 0, 1),
  WEST(1, AxisDirection.NEGATIVE, Axis.X, -1, 0), EAST(3, AxisDirection.POSITIVE, Axis.X, 1, 0);

  public final int idHorizontal; // {net.minecraft.util.math.Direction.idHorizontal}
  public final Axis axis; // {net.minecraft.util.math.Direction.axis}
  public final AxisDirection direction; // {net.minecraft.util.math.Direction.direction}
  public final int offsetX; // {net.minecraft.util.math.Direction.vector}
  public final int offsetZ;

  private Direction(int horizontalIndex, AxisDirection direction, Axis axis, int offsetX, int offsetZ) {
    this.idHorizontal = horizontalIndex;
    this.axis = axis;
    this.direction = direction;
    this.offsetX = offsetX;
    this.offsetZ = offsetZ;
  }

  public static final Direction[] HORIZONTAL = new Direction[4];
  static {
    for (Direction facing : values())
      HORIZONTAL[facing.idHorizontal] = facing;
  }

  // {net.minecraft.util.math.Direction.Axis}
  public enum Axis {
    X {
      @Override
      public int choose(int x, int y, int z) { return x; }

      @Override
      public double choose(double x, double y, double z) { return x; }
    },
    Y {
      @Override
      public int choose(int x, int y, int z) { return y; }

      @Override
      public double choose(double x, double y, double z) { return y; }
    },
    Z {
      @Override
      public int choose(int x, int y, int z) { return z; }

      @Override
      public double choose(double x, double y, double z) { return z; }
    };

    public abstract int choose(int x, int y, int z);

    public abstract double choose(double x, double y, double z);
  }

  // {net.minecraft.util.math.Direction.AxisDirection}
  public enum AxisDirection {
    POSITIVE, NEGATIVE;
  }

  // {net.minecraft.util.math.Direction.Type}
  public enum Type implements Iterable<Direction> {
    HORIZONTAL(new Direction[] { NORTH, EAST, SOUTH, WEST });

    private final Direction[] facingArray;

    private Type(Direction[] facingArray) { this.facingArray = facingArray; }

    @Override
    public Iterator<Direction> iterator() {
      return new Iterator<>() {
        private int index = 0;

        @Override
        public boolean hasNext() { return index < facingArray.length; }

        @Override
        public Direction next() { return facingArray[index++]; }
      };
    }
  }
}
