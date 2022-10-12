package minecraft_simulator.v1_14.block;

import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.util.BooleanBiFunction;
import minecraft_simulator.v1_14.util.MathHelper;
import minecraft_simulator.v1_14.world.AbstractXYZBlockGrid;

/**
 * See {net.minecraft.util.shape.VoxelShapes}
 */
public class VoxelShapes {
  public static final VoxelShape FULL_CUBE;
  static {
    BitSetVoxelSet voxelSet = new BitSetVoxelSet(1, 1, 1);
    voxelSet.set(0, 0, 0, true);
    FULL_CUBE = new SimpleVoxelShape(voxelSet);
  }
  public static final VoxelShape UNBOUNDED = VoxelShapes.cuboid(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
      Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
  public static final VoxelShape EMPTY = new ArrayVoxelShape(new BitSetVoxelSet(0, 0, 0), new double[] { 0 },
      new double[] { 0 }, new double[] { 0 });

  /**
   * See {net.minecraft.util.shape.VoxelShape}
   * 
   * @param minX
   * @param minY
   * @param minZ
   * @param maxX
   * @param maxY
   * @param maxZ
   * @return A shape representing the cuboid with the given bounds
   */
  public static VoxelShape cuboid(final double minX, final double minY, final double minZ, final double maxX,
      final double maxY, final double maxZ) {
    final int bitsX = VoxelShapes.findRequiredBitResolution(minX, maxX);
    final int bitsY = VoxelShapes.findRequiredBitResolution(minY, maxY);
    final int bitsZ = VoxelShapes.findRequiredBitResolution(minZ, maxZ);
    if (bitsX < 0 || bitsY < 0 || bitsZ < 0)
      return new ArrayVoxelShape(VoxelShapes.FULL_CUBE.voxels, new double[] { minX, maxX }, new double[] { minY, maxY },
          new double[] { minZ, maxZ });
    if (bitsX == 0 && bitsY == 0 && bitsZ == 0)
      return new XYZBoundingBox(minX, minY, minZ, maxX, maxY, maxZ).contains(0.5, 0.5, 0.5) ? VoxelShapes.FULL_CUBE
          : VoxelShapes.EMPTY;
    final int sizeX = 1 << bitsX;
    final int sizeY = 1 << bitsY;
    final int sizeZ = 1 << bitsZ;
    final int scaledMinX = (int)Math.round(minX * (double)sizeX);
    final int scaledMaxX = (int)Math.round(maxX * (double)sizeX);
    final int scaledMinY = (int)Math.round(minY * (double)sizeY);
    final int scaledMaxY = (int)Math.round(maxY * (double)sizeY);
    final int scaledMinZ = (int)Math.round(minZ * (double)sizeZ);
    final int scaledMaxZ = (int)Math.round(maxZ * (double)sizeZ);
    BitSetVoxelSet bitSetVoxelSet = new BitSetVoxelSet(sizeX, sizeY, sizeZ);
    for (int x = scaledMinX; x < scaledMaxX; x++)
      for (int y = scaledMinY; y < scaledMaxY; y++)
        for (int z = scaledMinZ; z < scaledMaxZ; z++)
          bitSetVoxelSet.set(x, y, z, true);
    return new SimpleVoxelShape(bitSetVoxelSet);
  }

  /**
   * See {net.minecraft.util.shape.VoxelShapes.findRequiredBitResolution(double,
   * double)}
   * 
   * @param min
   * @param max
   * @return Number of bits appropriate to contain the bounds; -1 if outside [0,1]
   *         or would require too many bits
   */
  protected static int findRequiredBitResolution(double min, double max) {
    if (min < -1.0E-7 || max > 1.0000001)
      return -1;
    for (int bits = 0; bits <= 3; bits++) {
      int size = 1 << bits;
      double scaledMin = min * (double)(1 << size);
      double scaledMax = max * (double)(1 << size);
      if (Math.abs(scaledMin - Math.floor(scaledMin)) < 1.0E-7 && Math.abs(scaledMax - Math.floor(scaledMax)) < 1.0E-7)
        return bits;
    }
    return -1;
  }

  protected static int gcd(int a, int b) {
    while (b != 0) {
      int t = b;
      b = a % b;
      a = t;
    }
    return a;
  }

  protected static long lcm(int i, int j) { return (long)i * (long)(j / gcd(i, j)); }

  /**
   * See {net.minecraft.util.shape.VoxelShapes.union(VoxelShape, VoxelShape)}
   * 
   * @param voxelShape1
   * @param voxelShape2
   * @return
   */
  public static VoxelShape union(VoxelShape voxelShape1, VoxelShape voxelShape2) {
    return VoxelShapes.combineAndSimplify(voxelShape1, voxelShape2, BooleanBiFunction.OR);
  }

  /**
   * See {net.minecraft.util.shape.VoxelShapes.combineAndSimplify(VoxelShape,
   * VoxelShape, BooleanBiFunction)}
   * 
   * @param voxelShape1
   * @param voxelShape2
   * @param booleanBiFunction
   * @return
   */
  public static VoxelShape combineAndSimplify(VoxelShape voxelShape1, VoxelShape voxelShape2,
      BooleanBiFunction booleanBiFunction) {
    return VoxelShapes.combine(voxelShape1, voxelShape2, booleanBiFunction).simplify();
  }

  /**
   * See {net.minecraft.util.shape.VoxelShapes.combine(VoxelShape, VoxelShape,
   * BooleanBiFunction)}
   * 
   * @param voxelShape1
   * @param voxelShape2
   * @param combineFunction
   * @return A new shape made by putting the shapes through the filter
   */
  public static VoxelShape combine(VoxelShape voxelShape1, VoxelShape voxelShape2, BooleanBiFunction combineFunction) {
    if (combineFunction.apply(false, false))
      throw new IllegalArgumentException();
    if (voxelShape1 == voxelShape2)
      return combineFunction.apply(true, true) ? voxelShape1 : VoxelShapes.EMPTY;
    boolean onFirst = combineFunction.apply(true, false);
    boolean onSecond = combineFunction.apply(false, true);
    if (voxelShape1.isEmpty())
      return onSecond ? voxelShape2 : VoxelShapes.EMPTY;
    if (voxelShape2.isEmpty())
      return onFirst ? voxelShape1 : VoxelShapes.EMPTY;
    double[] xPoints1 = voxelShape1.getIncludedPoints(Direction.Axis.X);
    double[] yPoints1 = voxelShape1.getIncludedPoints(Direction.Axis.Y);
    double[] zPoints1 = voxelShape1.getIncludedPoints(Direction.Axis.Z);
    double[] xPoints2 = voxelShape2.getIncludedPoints(Direction.Axis.X);
    double[] yPoints2 = voxelShape2.getIncludedPoints(Direction.Axis.Y);
    double[] zPoints2 = voxelShape2.getIncludedPoints(Direction.Axis.Z);
    if (voxelShape1 instanceof SimpleVoxelShape && voxelShape2 instanceof SimpleVoxelShape) {
      long lcmX = lcm(xPoints1.length - 1, xPoints2.length - 1);
      long lcmY = lcm(yPoints1.length - 1, yPoints2.length - 1);
      long lcmZ = lcm(zPoints1.length - 1, zPoints2.length - 1);
      if (lcmX * lcmY * lcmZ <= 256L) {
        DoubleListPair xPair = DoubleListPair.fractional(xPoints1.length - 1, xPoints2.length - 1);
        DoubleListPair yPair = DoubleListPair.fractional(yPoints1.length - 1, yPoints2.length - 1);
        DoubleListPair zPair = DoubleListPair.fractional(zPoints1.length - 1, zPoints2.length - 1);
        BitSetVoxelSet newVoxelSet = BitSetVoxelSet.combine(voxelShape1.voxels, voxelShape2.voxels, xPair, yPair, zPair,
            combineFunction);
        return new SimpleVoxelShape(newVoxelSet);
      }
    }
    DoubleListPair xPair = DoubleListPair.simple(xPoints1, xPoints2, onFirst, onSecond);
    DoubleListPair yPair = DoubleListPair.simple(yPoints1, yPoints2, onFirst, onSecond);
    DoubleListPair zPair = DoubleListPair.simple(zPoints1, zPoints2, onFirst, onSecond);
    BitSetVoxelSet newVoxelSet = BitSetVoxelSet.combine(voxelShape1.voxels, voxelShape2.voxels, xPair, yPair, zPair,
        combineFunction);
    return new ArrayVoxelShape(newVoxelSet, xPair.mergedList, yPair.mergedList, zPair.mergedList);
  }

  /**
   * See {net.minecraft.util.shape.VoxelShapes.method_17945(Axis, BoundingBox,
   * ViewableWorld, double, EntityContext, Stream<VoxelShape>)} and
   * {net.minecraft.util.shape.VoxelShapes.method_17944(BoundingBox,
   * ViewableWorld, double, EntityContext, AxisCycleDirection,
   * Stream<VoxelShape>)}
   * 
   * Note: the EntityContext only affects scaffoldings, which changes its behavior
   * if the player is sneaking
   * 
   * @param axisIn
   * @param boundingBox
   * @param blockGrid
   * @param distance
   * @return The movement amount blocked by the blocks
   */
  public static double method_17945(Direction.Axis axisIn, XYZBoundingBox boundingBox, AbstractXYZBlockGrid blockGrid,
      double distance) {
    if (Math.abs(distance) < 1.0E-7)
      return 0.0;
    AxisCycleDirection axisCycleDirection = AxisCycleDirection.between(axisIn, Direction.Axis.Z);
    // if (boundingBox.getXSize() < 1.0E-6 || boundingBox.getYSize() < 1.0E-6 || boundingBox.getZSize() < 1.0E-6) 
    //   return distance;
    AxisCycleDirection opposite = axisCycleDirection.opposite();
    Direction.Axis axis1 = opposite.cycle(Direction.Axis.X);
    Direction.Axis axis2 = opposite.cycle(Direction.Axis.Y);
    Direction.Axis axis3 = opposite.cycle(Direction.Axis.Z);
    int min1 = MathHelper.floor(boundingBox.getMin(axis1) - 1.0E-7) - 1;
    int max1 = MathHelper.floor(boundingBox.getMax(axis1) + 1.0E-7) + 1;
    int min2 = MathHelper.floor(boundingBox.getMin(axis2) - 1.0E-7) - 1;
    int max2 = MathHelper.floor(boundingBox.getMax(axis2) + 1.0E-7) + 1;
    double min3 = boundingBox.getMin(axis3) - 1.0E-7;
    double max3 = boundingBox.getMax(axis3) + 1.0E-7;
    boolean directionPositive = distance > 0.0;
    int start = directionPositive ? MathHelper.floor(boundingBox.getMax(axis3) - 1.0E-7) - 1
        : MathHelper.floor(boundingBox.getMin(axis3) + 1.0E-7) + 1;
    int end = directionPositive ? MathHelper.floor(max3 + distance) + 1 : MathHelper.floor(min3 + distance) - 1;
    int step = directionPositive ? 1 : -1;
    int i = start;
    while (directionPositive ? i <= end : i >= end) {
      for (int j = min1; j <= max1; ++j) {
        for (int k = min2; k <= max2; ++k) {
          int touching = 0;
          if (j == min1 || j == max1)
            ++touching;
          if (k == min2 || k == max2)
            ++touching;
          if (i == start || i == end)
            ++touching;
          if (touching >= 3)
            continue;
          int blockX = opposite.choose(j, k, i, Direction.Axis.X);
          int blockY = opposite.choose(j, k, i, Direction.Axis.Y);
          int blockZ = opposite.choose(j, k, i, Direction.Axis.Z);
          Block block = blockGrid.getBlockAt(blockX, blockY, blockZ);
          if (touching == 1 && !block.method_17900()
              || touching == 2 /* && blockState.getBlock() != Blocks.MOVING_PISTON */)
            continue;
          distance = block.collisionShape.method_1108(axis3, boundingBox.offset(blockX, blockY, blockZ), distance);
          if (Math.abs(distance) < 1.0E-7)
            return 0.0;
          end = directionPositive ? MathHelper.floor(max3 + distance) + 1 : MathHelper.floor(min3 + distance) - 1;
        }
      }
      i += step;
    }
    return distance;
  }
}
