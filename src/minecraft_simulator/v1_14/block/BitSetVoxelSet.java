package minecraft_simulator.v1_14.block;

import java.util.BitSet;

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

  // See {net.minecraft.util.shape.BitSetVoxelSet.BitSetVoxelSet(int, int, int, int, int, int, int, int, int)}
  public BitSetVoxelSet(int xSize, int ySize, int zSize) {
    this.storage = new BitSet(xSize * ySize * zSize);
    this.xSize = xSize;
    this.ySize = ySize;
    this.zSize = zSize;
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
  public boolean contains(int i, int j, int k) { return this.storage.get(this.getIndex(i, j, k)); }

  // See {net.minecraft.util.shape.BitSetVoxelSet.set(int, int, int, boolean)}
  public void set(int i, int j, int k, boolean value) { this.storage.set(this.getIndex(i, j, k), value); }

  // See {net.minecraft.util.shape.BitSetVoxelSet.isEmpty()}
  public boolean isEmpty() { return this.storage.isEmpty(); }

  // See {net.minecraft.util.shape.VoxelSet.getSize(Axis)}
  public int getSize(Direction.Axis axis) { return axis.choose(this.xSize, this.ySize, this.zSize); }

}
