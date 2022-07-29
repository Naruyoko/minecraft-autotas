package minecraft_zigzag_1_8_9;

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
  static final double blockMinXZ=(double)0F;
  static final double blockMaxXZ=(double)1F;
  public static boolean intersectsBlock(Player player,int x,int z){
    // See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos, IBlockState)}
    AxisAlignedBB blockbb=new AxisAlignedBB((double)x+blockMinXZ,(double)z+blockMinXZ,(double)x+blockMaxXZ,(double)z+blockMaxXZ);
    return blockbb.intersectsHorizontally(player.boundingBox);
  }
}
