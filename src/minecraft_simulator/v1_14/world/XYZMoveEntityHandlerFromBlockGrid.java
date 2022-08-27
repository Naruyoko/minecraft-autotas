package minecraft_simulator.v1_14.world;

import java.util.List;

import minecraft_simulator.v1_14.block.Block;
import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.player.AbstractXYZPlayer;
import minecraft_simulator.v1_14.player.IPushedByWater;
import minecraft_simulator.v1_14.util.MathHelper;

/**
 * A utility class for a world handling movement within a grid of blocks.
 */
//TODO: update
public class XYZMoveEntityHandlerFromBlockGrid implements IXYZMoveEntityHandler<AbstractXYZPlayer> {
  public final AbstractXYZBlockGrid blockGrid;

  public XYZMoveEntityHandlerFromBlockGrid(AbstractXYZBlockGrid blockGrid) { this.blockGrid = blockGrid; }

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
    XYZBoundingBox workingBoundingBox = player.boundingBox.clone();
    //web
    if (flagsIn.isInWeb) {
      flagsOut.isInWeb = false;
      x *= 0.25D;
      y *= 0.05000000074505806D;
      z *= 0.25D;
      player.velX = 0.0D;
      player.velY = 0.0D;
      player.velZ = 0.0D;
    }
    final boolean isSneaking = flagsIn.checkSneaking && flagsIn.onGround && flagsIn.isSneaking;
    if (isSneaking) {
      final double checkDelta = 0.05D;
      while (x != 0.0D && !blockGrid.hasAnyCollidingBoundingBoxes(
          XYZBoundingBox.copyOffset(workingBoundingBox, player.boundingBox, x, -1.0D, 0.0D))) {
        if (x < checkDelta && x >= -checkDelta)
          x = 0.0D;
        else if (x > 0.0D)
          x -= checkDelta;
        else
          x += checkDelta;
      }
      while (z != 0.0D && !blockGrid.hasAnyCollidingBoundingBoxes(
          XYZBoundingBox.copyOffset(workingBoundingBox, player.boundingBox, 0.0D, -1.0D, z))) {
        if (z < checkDelta && z >= -checkDelta)
          z = 0.0D;
        else if (z > 0.0D)
          z -= checkDelta;
        else
          z += checkDelta;
      }
      while (x != 0.0D && z != 0.0D && !blockGrid.hasAnyCollidingBoundingBoxes(
          XYZBoundingBox.copyOffset(workingBoundingBox, player.boundingBox, x, -1.0D, z))) {
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
    List<XYZBoundingBox> collidingBoundingBoxes = blockGrid
        .getCollidingBoundingBoxes(XYZBoundingBox.copyStretch(workingBoundingBox, player.boundingBox, x, y, z));
    final XYZBoundingBox boundingBoxBefore = player.boundingBox.clone();
    for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
      y = blockBoundingBox.calculateYOffset(player.boundingBox, y);
    }
    player.boundingBox.mutatingOffset(0.0D, y, 0.0D);
    for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
      x = blockBoundingBox.calculateXOffset(player.boundingBox, x);
    }
    player.boundingBox.mutatingOffset(x, 0.0D, 0.0D);
    for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
      z = blockBoundingBox.calculateZOffset(player.boundingBox, z);
    }
    player.boundingBox.mutatingOffset(0.0D, 0.0D, z);
    if (flagsIn.checkStepping && player.stepHeight > 0.0F && (flagsIn.onGround || yNoBlock != y && yNoBlock < 0.0D)
        && (xNoBlock != x || zNoBlock != z)) {
      final double xNoStepping = x;
      final double yNoStepping = y;
      final double zNoStepping = z;
      final XYZBoundingBox boundingBoxNoStepping = player.boundingBox.clone();
      XYZBoundingBox.copy(player.boundingBox, boundingBoxBefore);
      y = (double)player.stepHeight;
      // collidingBoundingBoxes=collidable.getCollidingBoundingBoxes(XYZBoundingBox.copyStretch(workingBoundingBox,player.boundingBox,xNoBlock,y,zNoBlock));
      final XYZBoundingBox boundingBoxStepping1 = player.boundingBox.clone();
      XYZBoundingBox.copyStretch(workingBoundingBox, boundingBoxStepping1, xNoBlock, 0.0D, zNoBlock);
      double yStepping1 = y;
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        yStepping1 = blockBoundingBox.calculateYOffset(workingBoundingBox, yStepping1);
      }
      boundingBoxStepping1.mutatingOffset(0.0D, yStepping1, 0.0D);
      double xStepping1 = xNoBlock;
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        xStepping1 = blockBoundingBox.calculateXOffset(boundingBoxStepping1, xStepping1);
      }
      boundingBoxStepping1.mutatingOffset(xStepping1, 0.0D, 0.0D);
      double zStepping1 = zNoBlock;
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        zStepping1 = blockBoundingBox.calculateZOffset(boundingBoxStepping1, zStepping1);
      }
      boundingBoxStepping1.mutatingOffset(0.0D, 0.0D, zStepping1);
      final XYZBoundingBox boundingBoxStepping2 = player.boundingBox.clone();
      double yStepping2 = y;
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        yStepping2 = blockBoundingBox.calculateYOffset(boundingBoxStepping2, yStepping2);
      }
      boundingBoxStepping2.mutatingOffset(0.0D, yStepping2, 0.0D);
      double xStepping2 = xNoBlock;
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        xStepping2 = blockBoundingBox.calculateXOffset(boundingBoxStepping2, xStepping2);
      }
      boundingBoxStepping2.mutatingOffset(xStepping2, 0.0D, 0.0D);
      double zStepping2 = zNoBlock;
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        zStepping2 = blockBoundingBox.calculateZOffset(boundingBoxStepping2, zStepping2);
      }
      boundingBoxStepping2.mutatingOffset(0.0D, 0.0D, zStepping2);
      final double distanceStepping1 = xStepping1 * xStepping1 + zStepping1 * zStepping1;
      final double distanceStepping2 = xStepping2 * xStepping2 + zStepping2 * zStepping2;
      if (distanceStepping1 > distanceStepping2) {
        x = xStepping1;
        y = -yStepping1;
        z = zStepping1;
        XYZBoundingBox.copy(player.boundingBox, boundingBoxStepping1);
      } else {
        x = xStepping2;
        y = -yStepping2;
        z = zStepping2;
        XYZBoundingBox.copy(player.boundingBox, boundingBoxStepping2);
      }
      for (XYZBoundingBox blockBoundingBox : collidingBoundingBoxes) {
        y = blockBoundingBox.calculateYOffset(player.boundingBox, y);
      }
      player.boundingBox.mutatingOffset(0.0D, y, 0.0D);
      if (xNoStepping * xNoStepping + zNoStepping * zNoStepping >= x * x + z * z) {
        x = xNoStepping;
        y = yNoStepping;
        z = zNoStepping;
        XYZBoundingBox.copy(player.boundingBox, boundingBoxNoStepping);
      }
    }
    player.resetPositionToBB();
    flagsOut.isCollidedHorizontally = xNoBlock != x || zNoBlock != z;
    flagsOut.isCollidedVertically = yNoBlock != y;
    flagsOut.onGround = flagsOut.isCollidedVertically && yNoBlock < 0.0D;
    flagsOut.isCollided = flagsOut.isCollidedHorizontally || flagsOut.isCollidedVertically;
    //Call to {net.minecraft.entity.Entity.updateFallState(double, boolean, Block, BlockPos)}, eventually calling {net.minecraft.entity.Entity.handleWaterMovement()}
    if (flagsIn.checkWater && !flagsIn.inWater)
      ((IPushedByWater)player).handleWaterMovement(blockGrid);
    if (xNoBlock != x)
      player.velX = 0.0D;
    if (zNoBlock != z)
      player.velZ = 0.0D;
    if (!isSneaking || yNoBlock != y) {
      Block blockBelow = blockGrid.getBlockAt(MathHelper.floor_double(player.posX),
          MathHelper.floor_double(player.posY - 0.20000000298023224D), MathHelper.floor_double(player.posZ));
      if (yNoBlock != y)
        blockBelow.onLanded(player, flagsIn, flagsOut);
      if (!isSneaking)
        blockBelow.onEntityCollidedWithBlockGround(player, flagsIn, flagsOut);
    }
    //Call to {net.minecraft.entity.Entity.doBlockCollisions()}
    final int minX = MathHelper.floor_double(player.boundingBox.minX + 0.001D);
    final int maxX = MathHelper.floor_double(player.boundingBox.maxX - 0.001D);
    final int minY = MathHelper.floor_double(player.boundingBox.minY + 0.001D);
    final int maxY = MathHelper.floor_double(player.boundingBox.maxY - 0.001D);
    final int minZ = MathHelper.floor_double(player.boundingBox.minZ + 0.001D);
    final int maxZ = MathHelper.floor_double(player.boundingBox.maxZ - 0.001D);
    for (int bx = minX; bx <= maxX; ++bx) {
      for (int by = minY; by <= maxY; ++by) {
        for (int bz = minZ; bz <= maxZ; ++bz) {
          blockGrid.getBlockAt(bx, by, bz).onEntityCollidedWithBlockIntersect(player, flagsIn, flagsOut);
        }
      }
    }
    //Return from doBlockCollisions
    return flagsOut;
  }

  public void moveEntity(AbstractXYZPlayer player, double x, double y, double z) {
    SimulationFlagsIn flags = new SimulationFlagsIn();
    if (player instanceof ISimulationFlagCommuniator)
      ((ISimulationFlagCommuniator)player).setSimulationFlagsIn(flags);
    SimulationFlagsOut flagsOut = new SimulationFlagsOut();
    moveEntity(player, x, y, z, flags, flagsOut);
    if (player instanceof ISimulationFlagCommuniator)
      ((ISimulationFlagCommuniator)player).getSimulationFlagsOut(flagsOut);
  }
}