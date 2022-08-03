package minecraft_simulator.v1_8_9.world;

import java.util.List;

import minecraft_simulator.v1_8_9.collision.IHorizontallyCollidable;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.AbstractXZPlayer;

/**
 * A utility class for a world handling horizontal movement with given wall that do not have any special effect.
 */
public class XZMoveEntityHandlerFromCollidableSimpleWall implements IXZMoveEntityHandler<AbstractXZPlayer> {
  public final IHorizontallyCollidable wall;
  public XZMoveEntityHandlerFromCollidableSimpleWall(IHorizontallyCollidable wall) {
    this.wall = wall;
  }
  /**
   * Attempt to move the player with given velocity, blocking it if necessary
   * See {net.minecraft.entity.Entity.moveEntity(double, double, double)}
   * @param player
   * @param x
   * @param z
   */
  public SimulationFlagsOut moveEntity(AbstractXZPlayer player,double x,double z,SimulationFlagsIn flagsIn,SimulationFlagsOut flagsOut){
    XZAxisAlignedBB workingBoundingBox=player.boundingBox.clone();
    final double xNoBlock=x;
    final double zNoBlock=z;
    List<XZAxisAlignedBB> collidingBoundingBoxes=wall.getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB.copyAddCoord(workingBoundingBox,player.boundingBox,x,z));
    for (XZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      x=blockBoundingBox.calculateHorizontalXOffset(player.boundingBox,x);
    }
    player.boundingBox.mutatingOffset(x,0.0D);
    for (XZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      z=blockBoundingBox.calculateHorizontalZOffset(player.boundingBox,z);
    }
    player.boundingBox.mutatingOffset(0.0D,z);
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
