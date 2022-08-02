package minecraft_simulator.v1_8_9;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for a world handling horizontal movement with the wall made of full blocks. The ground is assumed to be infinitely extending.
 */
public abstract class AbstractXZStoneWall implements IXZMoveEntityHandler<AbstractXZPlayer> {
  public abstract boolean hasBlockAt(int x,int z);
  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
   * @param player
   * @return
   */
  public List<XZAxisAlignedBB> getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb){
    List<XZAxisAlignedBB> list=new ArrayList<>();
    int minX=MathHelper.floor_double(bb.minX);
    int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    int minZ=MathHelper.floor_double(bb.minZ);
    int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
    for (int x=minX;x<maxX;x++){
      for (int z=minZ;z<maxZ;z++){
        if (hasBlockAt(x,z)) Blocks.fullBlock.addCollisionBoxesToList(x, z, bb, list);
      }
    }
    return list;
  }
  /**
   * Equivalent to !getCollidingBoundingBoxes(bb).isEmpty()
   * @param player
   * @return
   */
  public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb){
    int minX=MathHelper.floor_double(bb.minX);
    int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    int minZ=MathHelper.floor_double(bb.minZ);
    int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
    for (int x=minX;x<maxX;x++){
      for (int z=minZ;z<maxZ;z++){
        if (hasBlockAt(x,z)&&Blocks.fullBlock.hasAnyCollidingBoundingBoxes(x,z,bb)) return true;
      }
    }
    return false;
  }
  /**
   * Attempt to move the player with given velocity, blocking it if necessary
   * See {net.minecraft.entity.Entity.moveEntity(double, double, double)}
   * @param player
   * @param x
   * @param z
   */
  @Override
  public SimulationFlagsOut moveEntity(AbstractXZPlayer player,double x,double z,SimulationFlagsIn flagsIn,SimulationFlagsOut flagsOut){
    XZAxisAlignedBB workingBoundingBox=player.boundingBox.clone();
    final double xNoBlock=x;
    final double zNoBlock=z;
    List<XZAxisAlignedBB> collidingBoundingBoxes=getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB.copyAddCoord(workingBoundingBox,player.boundingBox,x,z));
    for (XZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      x=blockBoundingBox.calculateHorizontalXOffset(player.boundingBox,x);
    }
    player.boundingBox.move(x,0.0D);
    for (XZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      z=blockBoundingBox.calculateHorizontalZOffset(player.boundingBox,z);
    }
    player.boundingBox.move(0.0D,z);
    player.resetPositionToBB();
    flagsOut.isCollidedHorizontally=xNoBlock!=x||zNoBlock!=z;
    flagsOut.isCollidedVertically=true;
    flagsOut.onGround=flagsOut.isCollidedVertically;
    flagsOut.isCollided=flagsOut.isCollidedHorizontally||flagsOut.isCollidedVertically;
    //Call to {net.minecraft.entity.Entity.updateFallState(double, boolean, Block, BlockPos)}, ommited
    if (xNoBlock!=x) player.velX=0.0D;
    if (zNoBlock!=z) player.velZ=0.0D;
    return flagsOut;
  }
  public void moveEntity(AbstractXZPlayer player,double x,double z){
    SimulationFlagsIn flags=new SimulationFlagsIn();
    if (player instanceof ISimulationFlagCommuniator) ((ISimulationFlagCommuniator)player).setSimulationFlags(flags);
    SimulationFlagsOut flagsOut=new SimulationFlagsOut();
    moveEntity(player, x, z, flags, flagsOut);
    if (player instanceof ISimulationFlagCommuniator) ((ISimulationFlagCommuniator)player).getSimulationFlagsOut(flagsOut);
  }
}
