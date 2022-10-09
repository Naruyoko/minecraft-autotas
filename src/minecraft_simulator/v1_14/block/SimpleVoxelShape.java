package minecraft_simulator.v1_14.block;

import minecraft_simulator.v1_14.util.MathHelper;

/**
 * See {net.minecraft.util.shape.SimpleVoxelShape}
 * 
 * Instance of {@link VoxelShape} where the cells are divided evenly across a
 * block
 */
public class SimpleVoxelShape extends VoxelShape {
  // Since we don't have DoubleList, we calculate the list in constructor
  final double[] xPoints;
  final double[] yPoints;
  final double[] zPoints;

  SimpleVoxelShape(BitSetVoxelSet voxelSet) {
    super(voxelSet);
    // See {net.minecraft.util.shape.SimpleVoxelShape.getIncludedPoints(Axis)}
    xPoints = emulateFractionalDoubleList(voxelSet.xSize);
    yPoints = emulateFractionalDoubleList(voxelSet.ySize);
    zPoints = emulateFractionalDoubleList(voxelSet.zSize);
  }

  /**
   * See {net.minecraft.util.shape.FractionalDoubleList.getDouble(int)}
   * 
   * @param sectionCount
   * @return
   */
  public static double[] emulateFractionalDoubleList(int sectionCount) {
    double[] r = new double[sectionCount + 1];
    for (int i = 0; i <= sectionCount; i++)
      r[i] = (double)i / (double)sectionCount;
    return r;
  }

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

  @Override
  public int getCoordIndex(Direction.Axis axis, double d) {
    int i = this.voxels.getSize(axis);
    return MathHelper.clamp(MathHelper.floor(d * (double)i), -1, i);
  }
}
