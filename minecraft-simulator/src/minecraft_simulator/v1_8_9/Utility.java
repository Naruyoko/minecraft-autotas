package minecraft_simulator.v1_8_9;

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
  public static final double blockMinXZ=(double)0F;
  public static final double blockMaxXZ=(double)1F;
  public static boolean intersectsXZBlock(SprintingClearStoneXZPlayer player,int x,int z){
    // See {net.minecraft.block.Block.getCollisionBoundingBox(World, BlockPos, IBlockState)}
    MutableXZAxisAlignedBB blockbb=new MutableXZAxisAlignedBB((double)x+blockMinXZ,(double)z+blockMinXZ,(double)x+blockMaxXZ,(double)z+blockMaxXZ);
    return blockbb.intersectsHorizontally(player.boundingBox);
  }
}
