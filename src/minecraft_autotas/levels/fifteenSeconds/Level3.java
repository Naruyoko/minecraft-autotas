package minecraft_autotas.levels.fifteenSeconds;

import minecraft_simulator.v1_8_9.block.Block;
import minecraft_simulator.v1_8_9.block.Blocks;
import minecraft_simulator.v1_8_9.world.AbstractXYZBlockGrid;

/**
 * Level 3 from SethBling's "15 seconds" https://youtu.be/n02enFUrrlw
 */
public class Level3 extends AbstractXYZBlockGrid {
  public boolean hasFullBlockAt(int x, int y, int z) {
    if (z < 10) {
      if (y == 0)
        return x == 1100 && z == 0;
      else if (y == 2)
        return x == 1100 && (z == -1 || z == 1) || z == 0 && (x == 1099 || x == 1101);
      else
        return false;
    } else if (x >= 1099 && x <= 1101) {
      if (y == 10)
        return z >= 19 && z <= 39 || z >= 67 && z <= 80;
      else if (y == 16)
        return z >= 50 && z <= 63;
      else
        return false;
    } else if (x >= 1096 && x <= 1098 && z <= 115 && z >= 117) {
      if (y == 15)
        return true;
      else if (y == 17)
        return x != 1097 && z == 116;
      else if (y == 19)
        return z == 116;
      else
        return false;
    } else
      return false;
  }

  @Override
  public Block getBlockAt(int x, int y, int z) {
    if (y == 19 && x >= 1099 && x <= 1101 && z >= 50 && z <= 52)
      return Blocks.water;
    else if (y == 22 && x >= 1096 && x <= 1098 && z <= 115 && z >= 117)
      return Blocks.water;
    else
      return hasFullBlockAt(x, y, z) ? Blocks.fullBlock : Blocks.air;
  }
}
