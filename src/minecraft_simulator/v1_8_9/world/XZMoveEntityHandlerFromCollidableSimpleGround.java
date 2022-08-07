package minecraft_simulator.v1_8_9.world;

import minecraft_simulator.v1_8_9.collision.IHorizontallyCollidable;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.AbstractXZPlayer;

/**
 * A utility class for a world handling horizontal movement with given ground
 * that do not have any special effect.
 */
public class XZMoveEntityHandlerFromCollidableSimpleGround implements IXZMoveEntityHandler<AbstractXZPlayer> {
  public final IHorizontallyCollidable ground;

  public XZMoveEntityHandlerFromCollidableSimpleGround(IHorizontallyCollidable ground) { this.ground = ground; }

  /**
   * Attempt to move the player with given velocity See
   * {net.minecraft.entity.Entity.moveEntity(double, double, double)}
   * 
   * @param player
   * @param x
   * @param z
   */
  public SimulationFlagsOut moveEntity(AbstractXZPlayer player, double x, double z, SimulationFlagsIn flagsIn,
      SimulationFlagsOut flagsOut) {
    XZAxisAlignedBB workingBoundingBox = player.boundingBox.clone();
    if (flagsIn.checkSneaking && flagsIn.onGround && flagsIn.isSneaking) {
      final double checkDelta = 0.05D;
      while (x != 0.0D && !ground.hasAnyHorizontallyCollidingBoundingBoxes(
          XZAxisAlignedBB.copyOffset(workingBoundingBox, player.boundingBox, x, 0.0D))) {
        if (x < checkDelta && x >= -checkDelta)
          x = 0.0D;
        else if (x > 0.0D)
          x -= checkDelta;
        else
          x += checkDelta;
      }
      while (z != 0.0D && !ground.hasAnyHorizontallyCollidingBoundingBoxes(
          XZAxisAlignedBB.copyOffset(workingBoundingBox, player.boundingBox, 0.0D, z))) {
        if (z < checkDelta && z >= -checkDelta)
          z = 0.0D;
        else if (z > 0.0D)
          z -= checkDelta;
        else
          z += checkDelta;
      }
      while (x != 0.0D && z != 0.0D && !ground.hasAnyHorizontallyCollidingBoundingBoxes(
          XZAxisAlignedBB.copyOffset(workingBoundingBox, player.boundingBox, x, z))) {
        if (x < checkDelta && x >= -checkDelta)
          x = 0.0D;
        else if (x > 0.0D)
          x -= checkDelta;
        else
          x += checkDelta;
        if (z < checkDelta && z >= -checkDelta)
          z = 0.0D;
        else if (z > 0.0D)
          z -= checkDelta;
        else
          z += checkDelta;
      }
    }
    final XZAxisAlignedBB boundingBoxBefore = player.boundingBox.clone();
    player.boundingBox.mutatingOffset(x, z);
    player.resetPositionToBB();
    flagsOut.isCollidedHorizontally = false;
    flagsOut.isCollidedVertically = ground.hasAnyHorizontallyCollidingBoundingBoxes(
        XZAxisAlignedBB.copyAddCoord(workingBoundingBox, boundingBoxBefore, x, z));
    flagsOut.onGround = flagsOut.isCollidedVertically;
    flagsOut.isCollided = flagsOut.isCollidedHorizontally || flagsOut.isCollidedVertically;
    //Call to {net.minecraft.entity.Entity.updateFallState(double, boolean, Block, BlockPos)}, ommited
    return flagsOut;
  }

  public void moveEntity(AbstractXZPlayer player, double x, double z) {
    SimulationFlagsIn flags = new SimulationFlagsIn();
    if (player instanceof ISimulationFlagCommuniator)
      ((ISimulationFlagCommuniator)player).setSimulationFlagsIn(flags);
    SimulationFlagsOut flagsOut = new SimulationFlagsOut();
    moveEntity(player, x, z, flags, flagsOut);
    if (player instanceof ISimulationFlagCommuniator)
      ((ISimulationFlagCommuniator)player).getSimulationFlagsOut(flagsOut);
  }
}
