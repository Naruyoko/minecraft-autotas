package minecraft_simulator.v1_8_9.world;

import java.util.ArrayList;
import java.util.List;

import minecraft_simulator.v1_8_9.block.Blocks;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.AbstractXZPlayer;
import minecraft_simulator.v1_8_9.util.MathHelper;

/**
 * A utility class for a world handling horizontal movement with the ground made of full blocks.
 */
public abstract class AbstractXZStoneGround implements IXZMoveEntityHandler<AbstractXZPlayer> {
  public abstract boolean hasBlockAt(int x,int z);
  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
   * @param player
   * @return
   */
  public List<XZAxisAlignedBB> getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb){
    final List<XZAxisAlignedBB> list=new ArrayList<>();
    final int minX=MathHelper.floor_double(bb.minX);
    final int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    final int minZ=MathHelper.floor_double(bb.minZ);
    final int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
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
    final int minX=MathHelper.floor_double(bb.minX);
    final int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    final int minZ=MathHelper.floor_double(bb.minZ);
    final int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
    for (int x=minX;x<maxX;x++){
      for (int z=minZ;z<maxZ;z++){
        if (hasBlockAt(x,z)&&Blocks.fullBlock.hasAnyCollidingBoundingBoxes(x,z,bb)) return true;
      }
    }
    return false;
  }
  /**
   * Attempt to move the player with given velocity
   * See {net.minecraft.entity.Entity.moveEntity(double, double, double)}
   * @param player
   * @param x
   * @param z
   */
  @Override
  public SimulationFlagsOut moveEntity(AbstractXZPlayer player,double x,double z,SimulationFlagsIn flagsIn,SimulationFlagsOut flagsOut){
    XZAxisAlignedBB workingBoundingBox=player.boundingBox.clone();
    if (flagsIn.checkSneaking&&flagsIn.onGround&&flagsIn.isSneaking){
      final double checkDelta=0.05D;
      while (x!=0.0D&&!hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,x,0.0D))){
        if (x<checkDelta&&x>=-checkDelta) x=0.0D;
        else if (x>0.0D) x-=checkDelta;
        else x+=checkDelta;
      }
      while (z!=0.0D&&!hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,0.0D,z))){
        if (z<checkDelta&&z>=-checkDelta) z=0.0D;
        else if (z>0.0D) z-=checkDelta;
        else z+=checkDelta;
      }
      while (x!=0.0D&&z!=0.0D&&!hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,x,z))){
        if (x<checkDelta&&x>=-checkDelta) x=0.0D;
        else if (x>0.0D) x-=checkDelta;
        else x+=checkDelta;
        if (z<checkDelta&&z>=-checkDelta) z=0.0D;
        else if (z>0.0D) z-=checkDelta;
        else z+=checkDelta;
      }
    }
    player.boundingBox.mutatingOffset(x,z);
    player.resetPositionToBB();
    flagsOut.isCollidedHorizontally=false;
    flagsOut.isCollidedVertically=hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB.copyAddCoord(workingBoundingBox,player.boundingBox,x,z));
    flagsOut.onGround=flagsOut.isCollidedVertically;
    flagsOut.isCollided=flagsOut.isCollidedHorizontally||flagsOut.isCollidedVertically;
    //Call to {net.minecraft.entity.Entity.updateFallState(double, boolean, Block, BlockPos)}, ommited
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
