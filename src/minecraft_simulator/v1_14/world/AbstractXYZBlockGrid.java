package minecraft_simulator.v1_14.world;

import java.util.ArrayList;
import java.util.List;

import minecraft_simulator.v1_14.block.Block;
import minecraft_simulator.v1_14.collision.ICollidable;
import minecraft_simulator.v1_14.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_14.util.MathHelper;

/**
 * A utility class for a collidable made as grid of blocks.
 */
//TODO: update
public abstract class AbstractXYZBlockGrid implements ICollidable {
  /**
   * Gets the block at the coordinates. Returned value must not be null.
   * 
   * @param x
   * @param y
   * @param z
   * @return
   */
  public abstract Block getBlockAt(int x, int y, int z);

  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity,
   * AxisAlignedBB)}
   * 
   * @param player
   * @return
   */
  @Override
  public List<XYZAxisAlignedBB> getCollidingBoundingBoxes(XYZAxisAlignedBB bb) {
    final List<XYZAxisAlignedBB> list = new ArrayList<>();
    final int minX = MathHelper.floor_double(bb.minX);
    final int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
    final int minY = MathHelper.floor_double(bb.minY);
    final int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor_double(bb.minZ);
    final int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int z = minZ; z < maxZ; z++) {
        for (int y = minY - 1; y < maxY; y++) {
          getBlockAt(x, y, z).addCollisionBoxesToList(x, y, z, bb, list);
        }
      }
    }
    return list;
  }

  /**
   * Equivalent to !getCollidingBoundingBoxes(bb).isEmpty()
   * 
   * @param player
   * @return
   */
  @Override
  public boolean hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB bb) {
    final int minX = MathHelper.floor_double(bb.minX);
    final int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
    final int minY = MathHelper.floor_double(bb.minY);
    final int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor_double(bb.minZ);
    final int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int z = minZ; z < maxZ; z++) {
        for (int y = minY - 1; y < maxY; y++) {
          if (getBlockAt(x, y, z).hasAnyCollidingBoundingBoxes(x, y, z, bb))
            return true;
        }
      }
    }
    return false;
  }
}