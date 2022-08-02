package minecraft_simulator.v1_8_9;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for a world made of full blocks.
 */
public abstract class AbstractXYZStoneWorld implements IXYZMoveEntityHandler<AbstractXYZPlayer> {
  public abstract boolean hasBlockAt(int x,int y,int z);
  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
   * @param player
   * @return
   */
  public List<XYZAxisAlignedBB> getCollidingBoundingBoxes(XYZAxisAlignedBB bb){
    final List<XYZAxisAlignedBB> list=new ArrayList<>();
    final int minX=MathHelper.floor_double(bb.minX);
    final int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    final int minY=MathHelper.floor_double(bb.minY);
    final int maxY=MathHelper.floor_double(bb.maxY+1.0D);
    final int minZ=MathHelper.floor_double(bb.minZ);
    final int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
    for (int x=minX;x<maxX;x++){
      for (int z=minZ;z<maxZ;z++){
        for (int y=minY-1;y<maxY;y++){
          if (hasBlockAt(x,y,z)) Blocks.fullBlock.addCollisionBoxesToList(x, y, z, bb, list);
        }
      }
    }
    return list;
  }
  /**
   * Equivalent to !getCollidingBoundingBoxes(bb).isEmpty()
   * @param player
   * @return
   */
  public boolean hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB bb){
    final int minX=MathHelper.floor_double(bb.minX);
    final int maxX=MathHelper.floor_double(bb.maxX+1.0D);
    final int minY=MathHelper.floor_double(bb.minY);
    final int maxY=MathHelper.floor_double(bb.maxY+1.0D);
    final int minZ=MathHelper.floor_double(bb.minZ);
    final int maxZ=MathHelper.floor_double(bb.maxZ+1.0D);
    for (int x=minX;x<maxX;x++){
      for (int z=minZ;z<maxZ;z++){
        for (int y=minY-1;y<maxY;y++){
          if (hasBlockAt(x,y,z)&&Blocks.fullBlock.hasAnyCollidingBoundingBoxes(x,y,z,bb)) return true;
        }
      }
    }
    return false;
  }
  /**
   * Attempt to move the player with given velocity, blocking it if necessary
   * See {net.minecraft.entity.Entity.moveEntity(double, double, double)}
   * @param player
   * @param x
   * @param y
   * @param z
   */
  @Override
  public SimulationFlagsOut moveEntity(AbstractXYZPlayer player,double x,double y,double z,SimulationFlagsIn flagsIn,SimulationFlagsOut flagsOut){
    XYZAxisAlignedBB workingBoundingBox=player.boundingBox.clone();
    if (flagsIn.checkSneaking&&flagsIn.onGround&&flagsIn.isSneaking){
      final double checkDelta=0.05D;
      while (x!=0.0D&&!hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,x,-1.0D,0.0D))){
        if (x<checkDelta&&x>=-checkDelta) x=0.0D;
        else if (x>0.0D) x-=checkDelta;
        else x+=checkDelta;
      }
      while (z!=0.0D&&hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,0.0D,-1.0D,z))){
        if (z<checkDelta&&z>=-checkDelta) z=0.0D;
        else if (z>0.0D) z-=checkDelta;
        else z+=checkDelta;
      }
      while (x!=0.0D&&z!=0.0D&&hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,x,-1.0D,z))){
        if (x<checkDelta&&x>=-checkDelta) x=0.0D;
        else if (x>0.0D) x-=checkDelta;
        else x+=checkDelta;
        if (z<checkDelta&&z>=-checkDelta) z=0.0D;
        else if (z>0.0D) z-=checkDelta;
        else z+=checkDelta;
      }
    }
    final double xNoBlock=x;
    final double yNoBlock=y;
    final double zNoBlock=z;
    List<XYZAxisAlignedBB> collidingBoundingBoxes=getCollidingBoundingBoxes(XYZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,x,y,z));
    final XYZAxisAlignedBB boundingBoxBefore=player.boundingBox.clone();
    for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      y=blockBoundingBox.calculateYOffset(player.boundingBox,y);
    }
    player.boundingBox.move(0.0D,y,0.0D);
    for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      x=blockBoundingBox.calculateXOffset(player.boundingBox,x);
    }
    player.boundingBox.move(x,0.0D,0.0D);
    for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
      z=blockBoundingBox.calculateZOffset(player.boundingBox,z);
    }
    player.boundingBox.move(0.0D,0.0D,z);
    if (flagsIn.checkStepping&&player.stepHeight>0.0F&&(flagsIn.onGround||yNoBlock!=y&&yNoBlock<0.0D)&&(xNoBlock!=x||zNoBlock!=z)){
      final double xNoStepping=x;
      final double yNoStepping=y;
      final double zNoStepping=z;
      final XYZAxisAlignedBB boundingBoxNoStepping=player.boundingBox.clone();
      XYZAxisAlignedBB.copy(player.boundingBox,boundingBoxBefore);
      y=(double)player.stepHeight;
      collidingBoundingBoxes=getCollidingBoundingBoxes(XYZAxisAlignedBB.copyOffset(workingBoundingBox,player.boundingBox,xNoBlock,y,zNoBlock));
      final XYZAxisAlignedBB boundingBoxStepping1=player.boundingBox.clone();
      final XYZAxisAlignedBB axisalignedbb5=boundingBoxStepping1.addCoord(xNoBlock,0.0D,zNoBlock);
      double yStepping1=y;
      for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
        yStepping1=blockBoundingBox.calculateYOffset(axisalignedbb5,yStepping1);
      }
      boundingBoxStepping1.move(0.0D,yStepping1,0.0D);
      double xStepping1=xNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
        xStepping1=blockBoundingBox.calculateXOffset(boundingBoxStepping1,xStepping1);
      }
      boundingBoxStepping1.move(xStepping1,0.0D,0.0D);
      double zStepping1=zNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
        zStepping1=blockBoundingBox.calculateZOffset(boundingBoxStepping1,zStepping1);
      }
      boundingBoxStepping1.move(0.0D,0.0D,zStepping1);
      final XYZAxisAlignedBB boundingBoxStepping2=player.boundingBox.clone();
      double yStepping2=y;
      for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
        yStepping2=blockBoundingBox.calculateYOffset(boundingBoxStepping2,yStepping2);
      }
      boundingBoxStepping2.move(0.0D,yStepping2,0.0D);
      double xStepping2=xNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
        xStepping2=blockBoundingBox.calculateXOffset(boundingBoxStepping2,xStepping2);
      }
      boundingBoxStepping2.move(xStepping2,0.0D,0.0D);
      double zStepping2=zNoBlock;
      for (XYZAxisAlignedBB blockBoundingBox:collidingBoundingBoxes){
        zStepping2=blockBoundingBox.calculateZOffset(boundingBoxStepping2,zStepping2);
      }
      boundingBoxStepping2.move(0.0D,0.0D,zStepping2);
      final double distanceStepping1=xStepping1*xStepping1+zStepping1*zStepping1;
      final double distanceStepping2=xStepping2*xStepping2+zStepping2*zStepping2;
      if (distanceStepping1>distanceStepping2){
        x=xStepping1;
        y=-yStepping1;
        z=zStepping1;
        XYZAxisAlignedBB.copy(player.boundingBox,boundingBoxStepping1);
      }else{
        x=xStepping2;
        y=-yStepping2;
        z=zStepping2;
        XYZAxisAlignedBB.copy(player.boundingBox,boundingBoxStepping2);
      }
      for (XYZAxisAlignedBB axisalignedbb12:collidingBoundingBoxes){
        y=axisalignedbb12.calculateYOffset(player.boundingBox, y);
      }
      player.boundingBox.move(0.0D,y,0.0D);
      if (xNoStepping*xNoStepping+zNoStepping*zNoStepping>=x*x+z*z){
        x=xNoStepping;
        y=yNoStepping;
        z=zNoStepping;
        XYZAxisAlignedBB.copy(player.boundingBox,boundingBoxNoStepping);
      }
    }
    player.resetPositionToBB();
    flagsOut.isCollidedHorizontally=xNoBlock!=x||zNoBlock!=z;
    flagsOut.isCollidedVertically=yNoBlock!=y;
    flagsOut.onGround=flagsOut.isCollidedVertically&&yNoBlock<0.0D;
    flagsOut.isCollided=flagsOut.isCollidedHorizontally||flagsOut.isCollidedVertically;
    //Call to {net.minecraft.entity.Entity.updateFallState(double, boolean, Block, BlockPos)}, ommited
    if (xNoBlock!=x) player.velX=0.0D;
    if (zNoBlock!=z) player.velZ=0.0D;
    if (yNoBlock!=y){
      // Call to {net.minecraft.block.Block.onLanded(World, Entity)}, assumption calls for default behavior
      player.velY=0.0D;
    }
    return flagsOut;
  }
  public void moveEntity(AbstractXYZPlayer player,double x,double y,double z){
    SimulationFlagsIn flags=new SimulationFlagsIn();
    if (player instanceof ISimulationFlagCommuniator) ((ISimulationFlagCommuniator)player).setSimulationFlags(flags);
    SimulationFlagsOut flagsOut=new SimulationFlagsOut();
    moveEntity(player, x, y, z, flags, flagsOut);
    if (player instanceof ISimulationFlagCommuniator) ((ISimulationFlagCommuniator)player).getSimulationFlagsOut(flagsOut);
  }
}