package minecraft_simulator.v1_14.block;

import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.util.BooleanBiFunction;
import minecraft_simulator.v1_14.util.MathHelper;

/**
 * See {net.minecraft.util.shape.VoxelShape}
 * 
 * A container of VoxelSet that specifies where the cells are solid
 */
public abstract class VoxelShape {
  final BitSetVoxelSet voxels; // {net.minecraft.util.shape.VoxelShape.voxels}
  // {net.minecraft.util.shape.VoxelShape.VoxelShape(VoxelSet)}

  public VoxelShape(BitSetVoxelSet voxels) { this.voxels = voxels; }

  /**
   * See {net.minecraft.util.shape.VoxelShape.getCoord(Axis, int)}
   * 
   * @param axis
   * @param i
   * @return The coordinate of the grid in the direction of the axis
   */
  public double getCoord(Direction.Axis axis, int i) { return this.getIncludedPoints(axis)[i]; }

  /**
   * See {net.minecraft.util.shape.VoxelShape.getIncludedPoints(Axis)}
   * 
   * @param axis
   * @return List of divisions along the axis
   */
  public abstract double[] getIncludedPoints(Direction.Axis axis);

  /**
   * See {net.minecraft.util.shape.VoxelShape.getCoordIndex(Axis, double)}
   * 
   * @param axis
   * @param d
   * @return Maximum index of coordinate along the axis less than or equal the
   *         given coordinate
   */
  public int getCoordIndex(Direction.Axis axis, double d) {
    return MathHelper.binarySearch(0, this.voxels.getSize(axis) + 1, i -> {
      if (i < 0)
        return false;
      if (i > this.voxels.getSize(axis))
        return true;
      return d < this.getCoord(axis, i);
    }) - 1;
  }

  /**
   * See {net.minecraft.util.shape.VoxelShape.isEmpty()}
   * 
   * @return Whether this shape is empty
   */
  public boolean isEmpty() { return this.voxels.isEmpty(); }

  private VoxelShape cuboidByIndex(int i, int j, int k, int l, int m, int n) {
    return VoxelShapes.cuboid(this.getCoord(Direction.Axis.X, i), this.getCoord(Direction.Axis.Y, j),
        this.getCoord(Direction.Axis.Z, k), this.getCoord(Direction.Axis.X, l), this.getCoord(Direction.Axis.Y, m),
        this.getCoord(Direction.Axis.Z, n));
  }

  /**
   * See {net.minecraft.util.shape.VoxelShape.simplify()} and
   * {net.minecraft.util.shape.VoxelSet.forEachBox(class_253, boolean)}
   * 
   * @return A simplified shape
   */
  public VoxelShape simplify() {
    VoxelShape simplifiedShape = VoxelShapes.EMPTY;
    BitSetVoxelSet voxelSet = new BitSetVoxelSet(this.voxels);
    for (int x = 0; x <= this.voxels.xSize; x++) {
      for (int y = 0; y <= this.voxels.ySize; y++) {
        int zStart = -1;
        for (int z = 0; z <= this.voxels.zSize; z++) {
          if (voxelSet.inBoundsAndContains(x, y, z)) {
            if (zStart == -1)
              zStart = z;
            continue;
          }
          if (zStart == -1)
            continue;
          int xStart = x;
          int xEnd = x;
          int yStart = y;
          int yEnd = y;
          voxelSet.setColumn(zStart, z, x, y, false);
          while (voxelSet.isColumnFull(zStart, z, xStart - 1, yStart)) {
            voxelSet.setColumn(zStart, z, xStart - 1, yStart, false);
            --xStart;
          }
          while (voxelSet.isColumnFull(zStart, z, xEnd + 1, yStart)) {
            voxelSet.setColumn(zStart, z, xEnd + 1, yStart, false);
            ++xEnd;
          }
          while (voxelSet.isRectangleFull(xStart, xEnd + 1, zStart, z, yStart - 1)) {
            for (int xSweep = xStart; xSweep <= xEnd; xSweep++)
              voxelSet.setColumn(zStart, z, xSweep, yStart - 1, false);
            --yStart;
          }
          while (voxelSet.isRectangleFull(xStart, xEnd + 1, zStart, z, yEnd + 1)) {
            for (int xSweep = xStart; xSweep <= xEnd; xSweep++)
              voxelSet.setColumn(zStart, z, xSweep, yEnd + 1, false);
            ++yEnd;
          }
          simplifiedShape = VoxelShapes.combine(simplifiedShape,
              cuboidByIndex(xStart, yStart, zStart, xEnd + 1, yEnd + 1, z), BooleanBiFunction.OR);
          zStart = -1;
        }
      }
    }
    return simplifiedShape;
  }

  /**
   * See {net.minecraft.util.shape.VoxelShape.method_1108(Axis, BoundingBox,
   * double)}
   * 
   * @param axis
   * @param boundingBox
   * @param distance
   * @return The movement amount blocked by this shape
   */
  public double method_1108(Direction.Axis axis, XYZBoundingBox boundingBox, double distance) {
    // Call to {net.minecraft.util.shape.VoxelShape.method_1103(AxisCycleDirection, BoundingBox, double)}
    if (this.isEmpty())
      return distance;
    if (Math.abs(distance) < 1.0E-7)
      return 0.0;
    AxisCycleDirection axisCycleDirection = AxisCycleDirection.between(axis, Direction.Axis.X);
    AxisCycleDirection axisCycleDirectionOpposite = axisCycleDirection.opposite();
    Direction.Axis axis2 = axisCycleDirectionOpposite.cycle(Direction.Axis.Y);
    Direction.Axis axis3 = axisCycleDirectionOpposite.cycle(Direction.Axis.Z);
    double boundingBoxMin = boundingBox.getMin(axis);
    double boundingBoxMax = boundingBox.getMax(axis);
    int min = this.getCoordIndex(axis, boundingBoxMin + 1.0E-7);
    int max = this.getCoordIndex(axis, boundingBoxMax - 1.0E-7);
    int min2 = Math.max(0, this.getCoordIndex(axis2, boundingBox.getMin(axis2) + 1.0E-7));
    int max2 = Math.min(this.voxels.getSize(axis2), this.getCoordIndex(axis2, boundingBox.getMax(axis2) - 1.0E-7) + 1);
    int min3 = Math.max(0, this.getCoordIndex(axis3, boundingBox.getMin(axis3) + 1.0E-7));
    int max3 = Math.min(this.voxels.getSize(axis3), this.getCoordIndex(axis3, boundingBox.getMax(axis3) - 1.0E-7) + 1);
    if (distance > 0.0) {
      int voxelMax = this.voxels.getSize(axis);
      for (int i = max + 1; i < voxelMax; ++i) {
        for (int j = min2; j < max2; ++j) {
          for (int k = min3; k < max3; ++k) {
            if (!this.voxels.inBoundsAndContains(axisCycleDirectionOpposite, i, j, k))
              continue;
            double shortDistance = this.getCoord(axis, i) - boundingBoxMax;
            return shortDistance >= -1.0E-7 ? Math.min(distance, shortDistance) : distance;
          }
        }
      }
    } else {
      for (int i = min - 1; i >= 0; --i) {
        for (int j = min2; j < max2; ++j) {
          for (int k = min3; k < max3; ++k) {
            if (!this.voxels.inBoundsAndContains(axisCycleDirectionOpposite, i, j, k))
              continue;
            double shortDistance = this.getCoord(axis, i + 1) - boundingBoxMin;
            return shortDistance <= 1.0E-7 ? Math.max(distance, shortDistance) : distance;
          }
        }
      }
    }
    return distance;
  }
}
