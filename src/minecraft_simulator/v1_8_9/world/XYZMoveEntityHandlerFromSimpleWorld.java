package minecraft_simulator.v1_8_9.world;

import java.util.List;

import minecraft_simulator.v1_8_9.collision.ICollidable;
import minecraft_simulator.v1_8_9.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.AbstractXYZPlayer;

/**
 * A utility class for a world handling movement with given collidable that do
 * not have any special effect.
 */
public class XYZMoveEntityHandlerFromSimpleWorld implements IXYZMoveEntityHandler<AbstractXYZPlayer> {
  public final ICollidable collidable;

  public XYZMoveEntityHandlerFromSimpleWorld(ICollidable collidable) { this.collidable = collidable; }

  /**
   * Attempt to move the player with given velocity, blocking it if necessary See
   * {net.minecraft.entity.Entity.moveEntity(double, double, double)}
   * 
   * @param player
   * @param x
   * @param y
   * @param z
   */
  public SimulationFlagsOut moveEntity(AbstractXYZPlayer player, double x, double y, double z,
      SimulationFlagsIn flagsIn, SimulationFlagsOut flagsOut) {
    XYZAxisAlignedBB workingBoundingBox = player.boundingBox.clone();
    if (flagsIn.checkSneaking && flagsIn.onGround && flagsIn.isSneaking) {
      final double checkDelta = 0.05D;
      while (x != 0.0D && !collidable.hasAnyCollidingBoundingBoxes(
          XYZAxisAlignedBB.copyOffset(workingBoundingBox, player.boundingBox, x, -1.0D, 0.0D))) {
        if (x < checkDelta && x >= -checkDelta)
          x = 0.0D;
        else if (x > 0.0D)
          x -= checkDelta;
        else
          x += checkDelta;
      }
      while (z != 0.0D && !collidable.hasAnyCollidingBoundingBoxes(
          XYZAxisAlignedBB.copyOffset(workingBoundingBox, player.boundingBox, 0.0D, -1.0D, z))) {
        if (z < checkDelta && z >= -checkDelta)
          z = 0.0D;
        else if (z > 0.0D)
          z -= checkDelta;
        else
          z += checkDelta;
      }
      while (x != 0.0D && z != 0.0D && !collidable.hasAnyCollidingBoundingBoxes(
          XYZAxisAlignedBB.copyOffset(workingBoundingBox, player.boundingBox, x, -1.0D, z))) {
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
    final double xNoBlock = x;
    final double yNoBlock = y;
    final double zNoBlock = z;
    List<XYZAxisAlignedBB> collidingBoundingBoxes = collidable
        .getCollidingBoundingBoxes(XYZAxisAlignedBB.copyAddCoord(workingBoundingBox, player.boundingBox, x, y, z));
    final XYZAxisAlignedBB boundingBoxBefore = player.boundingBox.clone();
    for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
      y = blockBoundingBox.calculateYOffset(player.boundingBox, y);
    }
    player.boundingBox.mutatingOffset(0.0D, y, 0.0D);
    for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
      x = blockBoundingBox.calculateXOffset(player.boundingBox, x);
    }
    player.boundingBox.mutatingOffset(x, 0.0D, 0.0D);
    for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
      z = blockBoundingBox.calculateZOffset(player.boundingBox, z);
    }
    player.boundingBox.mutatingOffset(0.0D, 0.0D, z);
    if (flagsIn.checkStepping && player.stepHeight > 0.0F && (flagsIn.onGround || yNoBlock != y && yNoBlock < 0.0D)
        && (xNoBlock != x || zNoBlock != z)) {
      final double xNoStepping = x;
      final double yNoStepping = y;
      final double zNoStepping = z;
      final XYZAxisAlignedBB boundingBoxNoStepping = player.boundingBox.clone();
      XYZAxisAlignedBB.copy(player.boundingBox, boundingBoxBefore);
      y = (double)player.stepHeight;
      // collidingBoundingBoxes=collidable.getCollidingBoundingBoxes(XYZAxisAlignedBB.copyAddCoord(workingBoundingBox,player.boundingBox,xNoBlock,y,zNoBlock));
      final XYZAxisAlignedBB boundingBoxStepping1 = player.boundingBox.clone();
      XYZAxisAlignedBB.copyAddCoord(workingBoundingBox, boundingBoxStepping1, xNoBlock, 0.0D, zNoBlock);
      double yStepping1 = y;
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        yStepping1 = blockBoundingBox.calculateYOffset(workingBoundingBox, yStepping1);
      }
      boundingBoxStepping1.mutatingOffset(0.0D, yStepping1, 0.0D);
      double xStepping1 = xNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        xStepping1 = blockBoundingBox.calculateXOffset(boundingBoxStepping1, xStepping1);
      }
      boundingBoxStepping1.mutatingOffset(xStepping1, 0.0D, 0.0D);
      double zStepping1 = zNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        zStepping1 = blockBoundingBox.calculateZOffset(boundingBoxStepping1, zStepping1);
      }
      boundingBoxStepping1.mutatingOffset(0.0D, 0.0D, zStepping1);
      final XYZAxisAlignedBB boundingBoxStepping2 = player.boundingBox.clone();
      double yStepping2 = y;
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        yStepping2 = blockBoundingBox.calculateYOffset(boundingBoxStepping2, yStepping2);
      }
      boundingBoxStepping2.mutatingOffset(0.0D, yStepping2, 0.0D);
      double xStepping2 = xNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        xStepping2 = blockBoundingBox.calculateXOffset(boundingBoxStepping2, xStepping2);
      }
      boundingBoxStepping2.mutatingOffset(xStepping2, 0.0D, 0.0D);
      double zStepping2 = zNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        zStepping2 = blockBoundingBox.calculateZOffset(boundingBoxStepping2, zStepping2);
      }
      boundingBoxStepping2.mutatingOffset(0.0D, 0.0D, zStepping2);
      final double distanceStepping1 = xStepping1 * xStepping1 + zStepping1 * zStepping1;
      final double distanceStepping2 = xStepping2 * xStepping2 + zStepping2 * zStepping2;
      if (distanceStepping1 > distanceStepping2) {
        x = xStepping1;
        y = -yStepping1;
        z = zStepping1;
        XYZAxisAlignedBB.copy(player.boundingBox, boundingBoxStepping1);
      } else {
        x = xStepping2;
        y = -yStepping2;
        z = zStepping2;
        XYZAxisAlignedBB.copy(player.boundingBox, boundingBoxStepping2);
      }
      for (XYZAxisAlignedBB blockBoundingBox : collidingBoundingBoxes) {
        y = blockBoundingBox.calculateYOffset(player.boundingBox, y);
      }
      player.boundingBox.mutatingOffset(0.0D, y, 0.0D);
      if (xNoStepping * xNoStepping + zNoStepping * zNoStepping >= x * x + z * z) {
        x = xNoStepping;
        y = yNoStepping;
        z = zNoStepping;
        XYZAxisAlignedBB.copy(player.boundingBox, boundingBoxNoStepping);
      }
    }
    player.resetPositionToBB();
    flagsOut.isCollidedHorizontally = xNoBlock != x || zNoBlock != z;
    flagsOut.isCollidedVertically = yNoBlock != y;
    flagsOut.onGround = flagsOut.isCollidedVertically && yNoBlock < 0.0D;
    flagsOut.isCollided = flagsOut.isCollidedHorizontally || flagsOut.isCollidedVertically;
    //Call to {net.minecraft.entity.Entity.updateFallState(double, boolean, Block, BlockPos)}, ommited
    if (xNoBlock != x)
      player.velX = 0.0D;
    if (zNoBlock != z)
      player.velZ = 0.0D;
    if (yNoBlock != y) {
      // Call to {net.minecraft.block.Block.onLanded(World, Entity)}, assumption calls for default behavior
      player.velY = 0.0D;
    }
    return flagsOut;
  }

  public void moveEntity(AbstractXYZPlayer player, double x, double y, double z) {
    SimulationFlagsIn flags = new SimulationFlagsIn();
    if (player instanceof ISimulationFlagCommuniator)
      ((ISimulationFlagCommuniator)player).setSimulationFlags(flags);
    SimulationFlagsOut flagsOut = new SimulationFlagsOut();
    moveEntity(player, x, y, z, flags, flagsOut);
    if (player instanceof ISimulationFlagCommuniator)
      ((ISimulationFlagCommuniator)player).getSimulationFlagsOut(flagsOut);
  }
}