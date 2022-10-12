package minecraft_simulator.v1_14.block;

import java.util.List;

import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.collision.XZBoundingBox;
import minecraft_simulator.v1_14.player.AbstractXYZPlayer;
import minecraft_simulator.v1_14.world.SimulationFlagsIn;
import minecraft_simulator.v1_14.world.SimulationFlagsOut;

// TODO: update
public class Block {
  public double minX;
  public double minY;
  public double minZ;
  public double maxX;
  public double maxY;
  public double maxZ;
  public float slipperiness; // {net.minecraft.block.Block.slipperiness}
  public float blockFrictionFactor;
  public double groundFriction;
  public float friction_intermediate;
  public boolean isLadder;
  public VoxelShape collisionShape; //TODO

  public Block(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
    setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    setSlipperiness(0.6F);
    isLadder = false;
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
   * See {{net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float,
   * float)}}
   */
  public void setSlipperiness(float slipperiness) {
    this.slipperiness = slipperiness;
    blockFrictionFactor = slipperiness * 0.91F;
    groundFriction = (double)blockFrictionFactor;
    friction_intermediate = 0.16277136F / (blockFrictionFactor * blockFrictionFactor * blockFrictionFactor);
  }

  /**
   * See {net.minecraft.block.Block.addCollisionBoxesToList(World, BlockPos,
   * IBlockState, BoundingBox, List<BoundingBox>, Entity)}
   */
  public void addCollisionBoxesToList(int x, int z, XZBoundingBox mask, List<XZBoundingBox> list) {
    XZBoundingBox bb = getCollisionBoundingBox(x, z);
    if (bb != null && mask.intersectsHorizontally(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int z, double minX, double minZ, double maxX, double maxZ,
      XZBoundingBox mask, List<XZBoundingBox> list) {
    XZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    if (bb != null && mask.intersectsHorizontally(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int z, float minX, float minZ, float maxX, float maxZ,
      XZBoundingBox mask, List<XZBoundingBox> list) {
    XZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    if (bb != null && mask.intersectsHorizontally(bb))
      list.add(bb);
  }

  public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZBoundingBox mask) {
    XZBoundingBox bb = getCollisionBoundingBox(x, z);
    return bb != null && mask.intersectsHorizontally(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int z, double minX, double minZ, double maxX, double maxZ,
      XZBoundingBox mask) {
    XZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    return bb != null && mask.intersectsHorizontally(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int z, float minX, float minZ, float maxX, float maxZ,
      XZBoundingBox mask) {
    XZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, z, minX, minZ, maxX, maxZ);
    return bb != null && mask.intersectsHorizontally(bb);
  }

  /**
   * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos,
   * IBlockState)}
   */
  public XZBoundingBox getCollisionBoundingBox(int x, int z) {
    return new XZBoundingBox((double)x + minX, (double)z + minZ, (double)x + maxX, (double)z + maxZ);
  }

  public XZBoundingBox getCollisionBoundingBoxFromBounds(int x, int z, double minX, double minZ, double maxX,
      double maxZ) {
    return new XZBoundingBox((double)x + minX, (double)z + minZ, (double)x + maxX, (double)z + maxZ);
  }

  public XZBoundingBox getCollisionBoundingBoxFromBounds(int x, int z, float minX, float minZ, float maxX, float maxZ) {
    return new XZBoundingBox((double)x + (double)minX, (double)z + (double)minZ, (double)x + (double)maxX,
        (double)z + (double)maxZ);
  }

  public void addCollisionBoxesToListAsFloor(int x, int z, XZBoundingBox mask, List<XZBoundingBox> list) {
    addCollisionBoxesToList(x, z, mask, list);
  }

  public boolean hasAnyCollidingBoundingBoxesAsFloor(int x, int z, XZBoundingBox mask) {
    return hasAnyCollidingBoundingBoxes(x, z, mask);
  }

  public XZBoundingBox getCollisionBoundingBoxAsFloor(int x, int z) { return getCollisionBoundingBox(x, z); }

  public void addCollisionBoxesToListAsCeiling(int x, int z, XZBoundingBox mask, List<XZBoundingBox> list) {
    addCollisionBoxesToList(x, z, mask, list);
  }

  public boolean hasAnyCollidingBoundingBoxesAsCeiling(int x, int z, XZBoundingBox mask) {
    return hasAnyCollidingBoundingBoxes(x, z, mask);
  }

  public XZBoundingBox getCollisionBoundingBoxAsCeiling(int x, int z) { return getCollisionBoundingBox(x, z); }

  /**
   * See {net.minecraft.block.Block.addCollisionBoxesToList(World, BlockPos,
   * IBlockState, BoundingBox, List<BoundingBox>, Entity)}
   */
  public void addCollisionBoxesToList(int x, int y, int z, XYZBoundingBox mask, List<XYZBoundingBox> list) {
    XYZBoundingBox bb = getCollisionBoundingBox(x, y, z);
    if (bb != null && mask.intersects(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int y, int z, double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, XYZBoundingBox mask, List<XYZBoundingBox> list) {
    XYZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    if (bb != null && mask.intersects(bb))
      list.add(bb);
  }

  public void addCollisionBoxFromBoundsToList(int x, int y, int z, float minX, float minY, float minZ, float maxX,
      float maxY, float maxZ, XYZBoundingBox mask, List<XYZBoundingBox> list) {
    XYZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    if (bb != null && mask.intersects(bb))
      list.add(bb);
  }

  public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZBoundingBox mask) {
    XYZBoundingBox bb = getCollisionBoundingBox(x, y, z);
    return bb != null && mask.intersects(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int y, int z, double minX, double minY, double minZ, double maxX,
      double maxY, double maxZ, XYZBoundingBox mask) {
    XYZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    return bb != null && mask.intersects(bb);
  }

  public boolean isCollidingBoxFromBounds(int x, int y, int z, float minX, float minY, float minZ, float maxX,
      float maxY, float maxZ, XYZBoundingBox mask) {
    XYZBoundingBox bb = getCollisionBoundingBoxFromBounds(x, y, z, minX, minY, minZ, maxX, maxY, maxZ);
    return bb != null && mask.intersects(bb);
  }

  /**
   * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos,
   * IBlockState)}
   */
  public XYZBoundingBox getCollisionBoundingBox(int x, int y, int z) {
    return new XYZBoundingBox((double)x + minX, (double)y + minY, (double)z + minZ, (double)x + maxX, (double)y + maxY,
        (double)z + maxZ);
  }

  public XYZBoundingBox getCollisionBoundingBoxFromBounds(int x, int y, int z, double minX, double minY, double minZ,
      double maxX, double maxY, double maxZ) {
    return new XYZBoundingBox((double)x + minX, (double)y + minY, (double)z + minZ, (double)x + maxX, (double)y + maxY,
        (double)z + maxZ);
  }

  public XYZBoundingBox getCollisionBoundingBoxFromBounds(int x, int y, int z, float minX, float minY, float minZ,
      float maxX, float maxY, float maxZ) {
    return new XYZBoundingBox((double)x + (double)minX, (double)y + (double)minY, (double)z + (double)minZ,
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

  public boolean method_17900() {
    //TODO 
    return false;
  }
}
