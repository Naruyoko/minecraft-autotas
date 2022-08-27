package minecraft_simulator.v1_14.world;

import java.util.ArrayList;
import java.util.List;

import minecraft_simulator.v1_14.block.Blocks;
import minecraft_simulator.v1_14.collision.ICollidable;
import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.util.MathHelper;

/**
 * A utility class for a collidable made as grid of full blocks.
 */
//TODO: update
public abstract class AbstractXYZStoneGrid implements ICollidable {
  public abstract boolean hasBlockAt(int x, int y, int z);

  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity,
   * BoundingBox)}
   * 
   * @param player
   * @return
   */
  @Override
  public List<XYZBoundingBox> getCollidingBoundingBoxes(XYZBoundingBox bb) {
    final List<XYZBoundingBox> list = new ArrayList<>();
    final int minX = MathHelper.floor_double(bb.minX);
    final int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
    final int minY = MathHelper.floor_double(bb.minY);
    final int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor_double(bb.minZ);
    final int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int z = minZ; z < maxZ; z++) {
        for (int y = minY - 1; y < maxY; y++) {
          if (hasBlockAt(x, y, z))
            Blocks.fullBlock.addCollisionBoxesToList(x, y, z, bb, list);
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
  public boolean hasAnyCollidingBoundingBoxes(XYZBoundingBox bb) {
    final int minX = MathHelper.floor_double(bb.minX);
    final int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
    final int minY = MathHelper.floor_double(bb.minY);
    final int maxY = MathHelper.floor_double(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor_double(bb.minZ);
    final int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int z = minZ; z < maxZ; z++) {
        for (int y = minY - 1; y < maxY; y++) {
          if (hasBlockAt(x, y, z) && Blocks.fullBlock.hasAnyCollidingBoundingBoxes(x, y, z, bb))
            return true;
        }
      }
    }
    return false;
  }
}