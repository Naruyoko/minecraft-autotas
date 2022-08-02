package minecraft_simulator.v1_8_9;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for a horizontally collidable made as grid of full blocks.
 */
public abstract class AbstractXZStoneGrid implements IHorizontallyCollidable {
  public abstract boolean hasBlockAt(int x,int z);
  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
   * @param player
   * @return
   */
  @Override
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
  @Override
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
}
