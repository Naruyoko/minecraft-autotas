package minecraft_simulator.v1_14.block;

/**
 * See {net.minecraft.util.shape.ArrayVoxelShape}
 * 
 * Instance of {@link VoxelShape} where the cells are divided using lists
 */
public class ArrayVoxelShape extends VoxelShape {
  final double[] xPoints; // {net.minecraft.util.shape.ArrayVoxelShape.xPoints}
  final double[] yPoints; // {net.minecraft.util.shape.ArrayVoxelShape.yPoints}
  final double[] zPoints; // {net.minecraft.util.shape.ArrayVoxelShape.zPoints}
  // {net.minecraft.util.shape.ArrayVoxelShape.ArrayVoxelShape(VoxelSet, double[], double[], double[])}

  public ArrayVoxelShape(BitSetVoxelSet voxels, double[] xPoints, double[] yPoints, double[] zPoints) {
    super(voxels);
    this.xPoints = xPoints;
    this.yPoints = yPoints;
    this.zPoints = zPoints;
  }

  /**
   * See {net.minecraft.util.shape.ArrayVoxelShape.getIncludedPoints(Axis)}
   * 
   * {@inheritDoc}
   */
  @Override
  public double[] getIncludedPoints(Direction.Axis axis) {
    switch (axis) {
    case X:
      return this.xPoints;
    case Y:
      return this.yPoints;
    case Z:
      return this.zPoints;
    }
    throw new IllegalArgumentException();
  }
}
