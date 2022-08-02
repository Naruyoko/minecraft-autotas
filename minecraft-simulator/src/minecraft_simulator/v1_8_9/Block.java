package minecraft_simulator.v1_8_9;

import java.util.List;

public class Block {
  public final double minX;
  public final double minY;
  public final double minZ;
  public final double maxX;
  public final double maxY;
  public final double maxZ;
  public Block(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
    this.minX = (double)minX;
    this.minY = (double)minY;
    this.minZ = (double)minZ;
    this.maxX = (double)maxX;
    this.maxY = (double)maxY;
    this.maxZ = (double)maxZ;
  }
  /**
   * See {net.minecraft.block.Block.addCollisionBoxesToList(World, BlockPos, IBlockState, AxisAlignedBB, List<AxisAlignedBB>, Entity)}
   */
  public void addCollisionBoxesToList(int x,int z,XZAxisAlignedBB mask,List<XZAxisAlignedBB> list){
    XZAxisAlignedBB bb=getCollisionBoundingBox(x, z);
    if (bb!=null&&mask.intersectsHorizontallyWith(bb)) list.add(bb);
  }
  public boolean hasAnyCollidingBoundingBoxes(int x,int z,XZAxisAlignedBB mask){
    XZAxisAlignedBB bb=getCollisionBoundingBox(x, z);
    return bb!=null&&mask.intersectsHorizontallyWith(bb);
  }
  /**
   * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos, IBlockState)}
   */
  public XZAxisAlignedBB getCollisionBoundingBox(int x,int z){
    return new XZAxisAlignedBB((double)x+minX, (double)z+minZ, (double)x+maxX, (double)z+maxZ);
  }
  //? is this needed
  public void addCollisionBoxesToListAsFloor(int x,int z,XZAxisAlignedBB mask,List<XZAxisAlignedBB> list){
    addCollisionBoxesToList(x, z, mask, list);
  }
  public XZAxisAlignedBB getCollisionBoundingBoxAsFloor(int x,int z){
    return getCollisionBoundingBox(x, z);
  }
  public void addCollisionBoxesToListAsCeiling(int x,int z,XZAxisAlignedBB mask,List<XZAxisAlignedBB> list){
    addCollisionBoxesToList(x, z, mask, list);
  }
  public XZAxisAlignedBB getCollisionBoundingBoxAsCeiling(int x,int z){
    return getCollisionBoundingBox(x, z);
  }
  /**
   * See {net.minecraft.block.Block.addCollisionBoxesToList(World, BlockPos, IBlockState, AxisAlignedBB, List<AxisAlignedBB>, Entity)}
   */
  public void addCollisionBoxesToList(int x,int y,int z,XYZAxisAlignedBB mask,List<XYZAxisAlignedBB> list){
    XYZAxisAlignedBB bb=getCollisionBoundingBox(x, y, z);
    if (bb!=null&&mask.intersectsHorizontallyWith(bb)) list.add(bb);
  }
  public boolean hasAnyCollidingBoundingBoxes(int x,int y,int z,XYZAxisAlignedBB mask){
    XYZAxisAlignedBB bb=getCollisionBoundingBox(x, y, z);
    return bb!=null&&mask.intersectsHorizontallyWith(bb);
  }
  /**
   * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos, IBlockState)}
   */
  public XYZAxisAlignedBB getCollisionBoundingBox(int x,int y,int z){
    return new XYZAxisAlignedBB((double)x+minX, (double)y+minY, (double)z+minZ, (double)x+maxX, (double)y+maxY, (double)z+maxZ);
  }
}
