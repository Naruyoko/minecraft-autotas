package minecraft_simulator.v1_8_9.block;

import java.util.List;

import minecraft_simulator.v1_8_9.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.AbstractXYZPlayer;
import minecraft_simulator.v1_8_9.world.SimulationFlagsIn;
import minecraft_simulator.v1_8_9.world.SimulationFlagsOut;

public class Block {
  public double minX;
  public double minY;
  public double minZ;
  public double maxX;
  public double maxY;
  public double maxZ;

  public Block(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
    setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
  }

  public Block() { this(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F); }

  /**
   * See {net.minecraft.block.Block.setBlockBounds(float, float, float, float,
   * float, float)}
   */
  public void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
    this.minX = (double)minX;
    this.minY = (double)minY;
    this.minZ = (double)minZ;
    this.maxX = (double)maxX;
    this.maxY = (double)maxY;
    this.maxZ = (double)maxZ;
  }

  /**
   * See {net.minecraft.block.Block.addCollisionBoxesToList(World, BlockPos,
   * IBlockState, AxisAlignedBB, List<AxisAlignedBB>, Entity)}
   */
  public void addCollisionBoxesToList(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
    XZAxisAlignedBB bb = getCollisionBoundingBox(x, z);
    if (bb != null && mask.intersectsHorizontallyWith(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int z, double minX, double minZ, double maxX, double maxZ,
      XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
    XZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    if (bb != null && mask.intersectsHorizontallyWith(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int z, float minX, float minZ, float maxX, float maxZ,
      XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
    XZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    if (bb != null && mask.intersectsHorizontallyWith(bb))
      list.add(bb);
  }

  public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZAxisAlignedBB mask) {
    XZAxisAlignedBB bb = getCollisionBoundingBox(x, z);
    return bb != null && mask.intersectsHorizontallyWith(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int z, double minX, double minZ, double maxX, double maxZ,
      XZAxisAlignedBB mask) {
    XZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    return bb != null && mask.intersectsHorizontallyWith(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int z, float minX, float minZ, float maxX, float maxZ,
      XZAxisAlignedBB mask) {
    XZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    return bb != null && mask.intersectsHorizontallyWith(bb);
  }

  /**
   * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos,
   * IBlockState)}
   */
  public XZAxisAlignedBB getCollisionBoundingBox(int x, int z) {
    return new XZAxisAlignedBB((double)x + minX, (double)z + minZ, (double)x + maxX, (double)z + maxZ);
  }

  public XZAxisAlignedBB getCollisionBoundingBoxFromBounds(int x, int z, double minX, double minZ, double maxX,
      double maxZ) {
    return new XZAxisAlignedBB((double)x + minX, (double)z + minZ, (double)x + maxX, (double)z + maxZ);
  }

  public XZAxisAlignedBB getCollisionBoundingBoxFromBounds(int x, int z, float minX, float minZ, float maxX,
      float maxZ) {
    return new XZAxisAlignedBB((double)x + (double)minX, (double)z + (double)minZ, (double)x + (double)maxX,
        (double)z + (double)maxZ);
  }

  public void addCollisionBoxesToListAsFloor(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
    addCollisionBoxesToList(x, z, mask, list);
  }

  public boolean hasAnyCollidingBoundingBoxesAsFloor(int x, int z, XZAxisAlignedBB mask) {
    return hasAnyCollidingBoundingBoxes(x, z, mask);
  }

  public XZAxisAlignedBB getCollisionBoundingBoxAsFloor(int x, int z) { return getCollisionBoundingBox(x, z); }

  public void addCollisionBoxesToListAsCeiling(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
    addCollisionBoxesToList(x, z, mask, list);
  }

  public boolean hasAnyCollidingBoundingBoxesAsCeiling(int x, int z, XZAxisAlignedBB mask) {
    return hasAnyCollidingBoundingBoxes(x, z, mask);
  }

  public XZAxisAlignedBB getCollisionBoundingBoxAsCeiling(int x, int z) { return getCollisionBoundingBox(x, z); }

  /**
   * See {net.minecraft.block.Block.addCollisionBoxesToList(World, BlockPos,
   * IBlockState, AxisAlignedBB, List<AxisAlignedBB>, Entity)}
   */
  public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
    XYZAxisAlignedBB bb = getCollisionBoundingBox(x, y, z);
    if (bb != null && mask.intersectsWith(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int y, int z, double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
    XYZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    if (bb != null && mask.intersectsWith(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int y, int z, float minX, float minY, float minZ, float maxX,
      float maxY, float maxZ, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
    XYZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    if (bb != null && mask.intersectsWith(bb))
      list.add(bb);
  }

  public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
    XYZAxisAlignedBB bb = getCollisionBoundingBox(x, y, z);
    return bb != null && mask.intersectsWith(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int y, int z, double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, XYZAxisAlignedBB mask) {
    XYZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    return bb != null && mask.intersectsWith(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int y, int z, float minX, float minY, float minZ, float maxX,
      float maxY, float maxZ, XYZAxisAlignedBB mask) {
    XYZAxisAlignedBB bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    return bb != null && mask.intersectsWith(bb);
  }

  /**
   * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos,
   * IBlockState)}
   */
  public XYZAxisAlignedBB getCollisionBoundingBox(int x, int y, int z) {
    return new XYZAxisAlignedBB((double)x + minX, (double)y + minY, (double)z + minZ, (double)x + maxX,
        (double)y + maxY, (double)z + maxZ);
  }

  public XYZAxisAlignedBB getCollisionBoundingBoxFromBounds(int x, int y, int z, double minX, double minY, double minZ,
      double maxX, double maxY, double maxZ) {
    return new XYZAxisAlignedBB((double)x + minX, (double)y + minY, (double)z + minZ, (double)x + maxX,
        (double)y + maxY, (double)z + maxZ);
  }

  public XYZAxisAlignedBB getCollisionBoundingBoxFromBounds(int x, int y, int z, float minX, float minY, float minZ,
      float maxX, float maxY, float maxZ) {
    return new XYZAxisAlignedBB((double)x + (double)minX, (double)y + (double)minY, (double)z + (double)minZ,
        (double)x + (double)maxX, (double)y + (double)maxY, (double)z + (double)maxZ);
  }

  /**
   * See {net.minecraft.block.BlockSlime.onLanded(World, Entity)}
   * 
   * @param player
   * @param flagsIn
   * @param flagsOut
   */
  public void onLanded(AbstractXYZPlayer player, SimulationFlagsIn flagsIn, SimulationFlagsOut flagsOut) {
    player.velY = 0.0D;
  }

  /**
   * See {net.minecraft.block.Block.onEntityCollidedWithBlock(World, BlockPos,
   * Entity)}
   * 
   * @param player
   * @param flagsIn
   * @param flagsOut
   */
  public void onEntityCollidedWithBlockGround(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
      SimulationFlagsOut flagsOut) {}

  /**
   * See {net.minecraft.block.Block.onEntityCollidedWithBlock(World, BlockPos,
   * IBlockState, Entity)}
   * 
   * @param player
   * @param flagsIn
   * @param flagsOut
   */
  public void onEntityCollidedWithBlockIntersect(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
      SimulationFlagsOut flagsOut) {}
}
