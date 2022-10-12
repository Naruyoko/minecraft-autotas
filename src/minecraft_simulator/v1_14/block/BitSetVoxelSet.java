package minecraft_simulator.v1_14.block;

import java.util.BitSet;

import minecraft_simulator.v1_14.util.BooleanBiFunction;

/**
 * See {net.minecraft.util.shape.VoxelSet} and
 * {net.minecraft.util.shape.BitSetVoxelSet}
 * 
 * A container of a bit set representing whether a cell is solid.
 */
public class BitSetVoxelSet {
  final BitSet storage; // {net.minecraft.util.shape.BitSetVoxelSet.storage}
  final int xSize; // {net.minecraft.util.shape.VoxelSet.xSize}
  final int ySize; // {net.minecraft.util.shape.VoxelSet.ySize}
  final int zSize; // {net.minecraft.util.shape.VoxelSet.zSize}

  private BitSetVoxelSet(BitSet storage, int xSize, int ySize, int zSize) {
    this.storage = storage;
    this.xSize = xSize;
    this.ySize = ySize;
    this.zSize = zSize;
  }

  // See {net.minecraft.util.shape.BitSetVoxelSet.BitSetVoxelSet(int, int, int, int, int, int, int, int, int)}
  public BitSetVoxelSet(int xSize, int ySize, int zSize) {
    this(new BitSet(xSize * ySize * zSize), xSize, ySize, zSize);
  }

  public BitSetVoxelSet(BitSetVoxelSet source) {
    this((BitSet)source.storage.clone(), source.xSize, source.ySize, source.zSize);
  }

  // See {net.minecraft.util.shape.BitSetVoxelSet.getIndex(int, int, int)}
  public int getIndex(int x, int y, int z) { return (x * this.ySize + y) * this.zSize + z; }

  /**
   * See
   * {net.minecraft.util.shape.BitSetVoxelSet.inBoundsAndContains(AxisCycleDirection,
   * int, int, int)}
   * 
   * @param axisCycleDirection
   * @param i
   * @param j
   * @param k
   * @return Whether if the voxel identified by (i,j,k) cycled is within bounds of
   *         the set and is filled
   */
  public boolean inBoundsAndContains(AxisCycleDirection axisCycleDirection, int i, int j, int k) {
    return this.inBoundsAndContains(axisCycleDirection.choose(i, j, k, Direction.Axis.X),
        axisCycleDirection.choose(i, j, k, Direction.Axis.Y), axisCycleDirection.choose(i, j, k, Direction.Axis.Z));
  }

  /**
   * See {net.minecraft.util.shape.BitSetVoxelSet.inBoundsAndContains(int, int,
   * int)}
   * 
   * @param x
   * @param y
   * @param z
   * @return Whether if the voxel identified by (x,y,z) is within bounds of the
   *         set and is filled
   */
  public boolean inBoundsAndContains(int x, int y, int z) {
    if (x < 0 || y < 0 || z < 0 || x >= this.xSize || y >= this.ySize || z >= this.zSize)
      return false;
    return this.contains(x, y, z);
  }

  // See {net.minecraft.util.shape.BitSetVoxelSet.contains(int, int, int)}
  public boolean contains(int x, int y, int z) { return this.storage.get(this.getIndex(x, y, z)); }

  // See {net.minecraft.util.shape.BitSetVoxelSet.set(int, int, int, boolean)}
  public void set(int x, int y, int z, boolean value) { this.storage.set(this.getIndex(x, y, z), value); }

  // See {net.minecraft.util.shape.BitSetVoxelSet.isEmpty()}
  public boolean isEmpty() { return this.storage.isEmpty(); }

  // See {net.minecraft.util.shape.VoxelSet.getSize(Axis)}
  public int getSize(Direction.Axis axis) { return axis.choose(this.xSize, this.ySize, this.zSize); }

  /**
   * See {net.minecraft.util.shape.BitSetVoxelSet.isColumnFull(int, int, int,
   * int)}
   * 
   * @param zStart
   * @param zEnd
   * @param x
   * @param y
   * @return
   */
  public boolean isColumnFull(int zStart, int zEnd, int x, int y) {
    if (x < 0 || y < 0 || zStart < 0 || x >= this.xSize || y >= this.ySize || zEnd > this.zSize)
      return false;

    return this.storage.nextClearBit(this.getIndex(x, y, zStart)) >= this.getIndex(x, y, zEnd);
  }

  /**
   * See {net.minecraft.util.shape.BitSetVoxelSet.setColumn(int, int, int, int,
   * boolean)}
   * 
   * @param zStart
   * @param zEnd
   * @param x
   * @param y
   * @param value
   */
  public void setColumn(int zStart, int zEnd, int x, int y, boolean value) {
    this.storage.set(this.getIndex(x, y, zStart), this.getIndex(x, y, zEnd), value);
  }

  /**
   * See {net.minecraft.util.shape.VoxelSet.isRectangleFull(int, int, int, int,
   * int)}
   * 
   * @param xStart
   * @param xEnd
   * @param zStart
   * @param zEnd
   * @param y
   * @return
   */
  public boolean isRectangleFull(int xStart, int xEnd, int zStart, int zEnd, int y) {
    for (int x = xStart; x < xEnd; ++x)
      if (!this.isColumnFull(zStart, zEnd, x, y))
        return false;
    return true;
  }

  /**
   * See {net.minecraft.util.shape.BitSetVoxelSet.combine(VoxelSet, VoxelSet,
   * DoubleListPair, DoubleListPair, DoubleListPair, BooleanBiFunction)}
   * 
   * @param voxels1
   * @param voxels2
   * @param xPair
   * @param yPair
   * @param zPair
   * @param combineFunction
   * @return
   */
  public static BitSetVoxelSet combine(BitSetVoxelSet voxels1, BitSetVoxelSet voxels2, DoubleListPair xPair,
      DoubleListPair yPair, DoubleListPair zPair, BooleanBiFunction combineFunction) {
    int xSize = xPair.mergedList.length - 1;
    int ySize = yPair.mergedList.length - 1;
    int zSize = zPair.mergedList.length - 1;
    BitSetVoxelSet voxelSet = new BitSetVoxelSet(xSize, ySize, zSize);
    for (int x = 0; x < xSize; x++)
      for (int y = 0; y < ySize; y++)
        for (int z = 0; z < zSize; z++)
          voxelSet.set(x, y, z,
              combineFunction.apply(
                  voxels1.inBoundsAndContains(xPair.indexes1[x], yPair.indexes1[y], zPair.indexes1[z]),
                  voxels2.inBoundsAndContains(xPair.indexes2[x], yPair.indexes2[y], zPair.indexes2[z])));
    return voxelSet;
  }

}
