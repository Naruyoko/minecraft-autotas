package minecraft_autotas;

import minecraft_simulator.v1_8_9.block.Blocks;
import minecraft_simulator.v1_8_9.util.MathHelper;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.SprintingClearStoneXZPlayer;

public class SmallZigzagJudge implements IJudge {
  public static final SprintingClearStoneXZPlayer startingState = new SprintingClearStoneXZPlayer(-0.5, 0.5, 0, 0, 0);

  public SprintingClearStoneXZPlayer getStartingState() { return startingState.clone(); }

  public static final boolean[] pathShape = new boolean[] { true, false, true, true, true, false, true, false, true,
      true, true, false };

  boolean canStandOnBlock(SprintingClearStoneXZPlayer player, int x, int z) {
    // Calculate path shape
    if (x >= 0 || x < -3 || z < 0 || !pathShape[(-1 - x) * 4 + z % 4])
      return false;
    return Blocks.fullBlock.hasAnyCollidingBoundingBoxes(x, z, player.boundingBox);
  }

  /**
   * See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity,
   * AxisAlignedBB)} for the list of blocks checked
   * 
   * @param player
   * @return
   */
  boolean onGround(SprintingClearStoneXZPlayer player) {
    // See {net.minecraft.world.World.getCollidingBoundingBoxes(Entity, AxisAlignedBB)}
    XZAxisAlignedBB bb = player.boundingBox;
    int minX = MathHelper.floor_double(bb.minX);
    int maxX = MathHelper.floor_double(bb.maxX + 1.0D);
    int minZ = MathHelper.floor_double(bb.minZ);
    int maxZ = MathHelper.floor_double(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int z = minZ; z < maxZ; z++) {
        if (canStandOnBlock(player, x, z))
          return true;
      }
    }
    return false;
  }

  public boolean isValid(SprintingClearStoneXZPlayer lastPlayer, SprintingClearStoneXZPlayer currentPlayer) {
    return onGround(lastPlayer) || onGround(currentPlayer);
  }

  public double score(SprintingClearStoneXZPlayer player) { return player.posZ - startingState.posZ; }
}
