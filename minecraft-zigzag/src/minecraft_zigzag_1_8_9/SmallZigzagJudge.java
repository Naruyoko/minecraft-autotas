package minecraft_zigzag_1_8_9;

public class SmallZigzagJudge implements IJudge {
  public static final Player startingState=new Player(-0.5,0.5,0,0,0);
  public Player getStartingState() {
    return startingState.clone();
  }
  public static final boolean[] pathShape=new boolean[]{
    true,false,true,true,
    true,false,true,false,
    true,true,true,false
  };
  boolean canStandOnBlock(Player player,int x,int z){
    // Calculate path shape
    if (x>=0||x<-3||z<0||!pathShape[(-1-x)*4+z%4]) return false;
    return Utility.intersectsBlock(player,x,z);
  }
  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)} for the list of blocks checked
   * @param player
   * @return whether the player would be on ground on the next tick
   */
  boolean onGround(Player player){
    // See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
    AxisAlignedBB bb=player.boundingBox;
    int minX=MathHelper.floor_double(bb.minX);
    int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    int minZ=MathHelper.floor_double(bb.minZ);
    int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
    for (int x=minX;x<maxX;x++){
      for (int z=minZ;z<maxZ;z++){
        if (canStandOnBlock(player, x, z)) return true;
      }
    }
    return false;
  }
  public boolean isValid(Player player){
    return onGround(player);
  }
  public double score(Player player) {
    return player.posZ-startingState.posZ;
  }
}
