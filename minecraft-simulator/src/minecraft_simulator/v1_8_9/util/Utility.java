package minecraft_simulator.v1_8_9.util;

public class Utility {
  public static String padSignFloat(float value){
    var r=Float.toString(value);
    if (value==0) return ' '+r;
    else if (value>0) return '+'+r;
    else return r;
  }
  public static String padSignDouble(double value){
    var r=Double.toString(value);
    if (value==0) return ' '+r;
    else if (value>0) return '+'+r;
    else return r;
  }
  // /**
  //  * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos, IBlockState)}
  //  * @param x
  //  * @param z
  //  * @return
  //  */
  // public static XZAxisAlignedBB fullXZBlockBB(int x,int z){
  //   return Blocks.fullBlock.getCollisionBoundingBox(x, z);
  // }
  // public static boolean intersectsXZBlock(AbstractXZPlayer player,int x,int z){
  //   return fullXZBlockBB(x, z).intersectsHorizontallyWith(player.boundingBox);
  // }
  // /**
  //  * See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos, IBlockState)}
  //  * @param x
  //  * @param y
  //  * @param z
  //  * @return
  //  */
  // public static XYZAxisAlignedBB fullBlockBB(int x,int y,int z){
  //   return Blocks.fullBlock.getCollisionBoundingBox(x, y, z);
  // }
  // public static boolean intersectsBlock(AbstractXYZPlayer player,int x,int y,int z){
  //   return fullBlockBB(x, y, z).intersectsWith(player.boundingBox);
  // }
}
