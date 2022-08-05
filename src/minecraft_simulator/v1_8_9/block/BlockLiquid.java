package minecraft_simulator.v1_8_9.block;

import minecraft_simulator.v1_8_9.util.MathHelper;

/**
 * See {net.minecraft.block.BlockLiquid}
 */
public class BlockLiquid extends PassableBlock {
  public final boolean isWater;
  public final boolean isLava;
  public final int level;
  public final int effectiveFlowDecay;
  public final float liquidHeightPercent;
  public final int northLevel;
  public final int eastLevel;
  public final int southLevel;
  public final int westLevel;
  public final double flowX;
  public final double flowY;
  public final double flowZ;

  /**
   * Creates a liquid with the material and level of themselves and levels of
   * adjacent liquids. The flow vector are calculated on instantiation, so any
   * instances should be created in advance for the best performance.
   * 
   * @param isLava     true if lava, false if water
   * @param level      0-7 indicates the corresponding levels, 0 being the
   *                   highest. 8-15 acts functionally the same and indicates the
   *                   "falling" liquid, having the same level as 0 but creating a
   *                   downward flow. Note that this implementation always creates
   *                   downward flow for "falling" liquid, while the real game
   *                   only does so if there is a block adjacent at the same level
   *                   or a block above. Use level 0 for a full-level liquid block
   *                   without this downward flow.
   * @param northLevel The liquid level of the adjacent block. 0-7 indicates the
   *                   levels within the same block height as this block. 8-15
   *                   indicates the levels 0-7 but a block below. -1 indicates
   *                   that there is no liquid to this direction or that the flow
   *                   is blocked.
   * @param eastLevel
   * @param southLevel
   * @param westLevel
   */
  public BlockLiquid(boolean isLava, int level, int northLevel, int eastLevel, int southLevel, int westLevel) {
    isWater = !isLava;
    this.isLava = isLava;
    this.level = level;
    effectiveFlowDecay = (level >= 8 ? 0 : level);
    liquidHeightPercent = (float)(effectiveFlowDecay + 1) / 9.0F;
    this.northLevel = northLevel;
    this.eastLevel = eastLevel;
    this.southLevel = southLevel;
    this.westLevel = westLevel;
    double flowX = 0.0D;
    double flowY = 0.0D;
    double flowZ = 0.0D;
    final EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.asArray();
    final int[] adjacentLevels = new int[] { northLevel, eastLevel, southLevel, westLevel };
    for (int i = 0; i < 4; i++) {
      EnumFacing facing = facings[i];
      int adjacentLevel = adjacentLevels[i];
      if (adjacentLevel >= 0) {
        int levelDifference = adjacentLevel - effectiveFlowDecay;
        flowX = flowX + (double)(facing.offsetX * levelDifference);
        flowY = flowY + (double)(-(levelDifference >>> 3) * levelDifference);
        flowZ = flowZ + (double)(facing.offsetZ * levelDifference);
      }
    }
    if (level >= 8) {
      double flowMag = (double)MathHelper.sqrt_double(flowX * flowX + flowY * flowY + flowZ * flowZ);
      if (flowMag < 1.0E-4D) {
        flowX = flowY = flowZ = 0.0D;
      } else {
        flowX = flowX / flowMag;
        flowY = flowY / flowMag;
        flowZ = flowZ / flowMag;
      }
      flowY = flowY + -6.0D;
    }
    double flowMag = (double)MathHelper.sqrt_double(flowX * flowX + flowY * flowY + flowZ * flowZ);
    if (flowMag < 1.0E-4D) {
      flowX = flowY = flowZ = 0.0D;
    } else {
      flowX = flowX / flowMag;
      flowY = flowY / flowMag;
      flowZ = flowZ / flowMag;
    }
    this.flowX = flowX;
    this.flowY = flowY;
    this.flowZ = flowZ;
  }

  /**
   * Creates a liquid with the material and level with no adjacent liquid. See
   * another constructor for more detail.
   * 
   * @param isLava
   * @param level
   */
  public BlockLiquid(boolean isLava, int level) { this(isLava, level, -1, -1, -1, -1); }
}
