package minecraft_simulator.v1_8_9;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for a collidable made as grid of full blocks.
 */
public abstract class AbstractXYZStoneGrid implements ICollidable {
  public abstract boolean hasBlockAt(int x,int y,int z);
  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
   * @param player
   * @return
   */
  @Override
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
  @Override
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
}