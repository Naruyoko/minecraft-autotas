package minecraft_simulator.v1_8_9.block;

/**
 * See {net.minecraft.block.BlockLiquid}
 */
public class BlockLiquid extends PassableBlock {
  public final boolean isWater;
  public final boolean isLava;
  public final int level;
  public final float liquidHeightPercent;

  public BlockLiquid(boolean isLava, int level) {
    this.isWater = !isLava;
    this.isLava = isLava;
    this.level = level;
    this.liquidHeightPercent = (float)((level >= 8 ? 0 : level) + 1) / 9.0F;
  }
}
