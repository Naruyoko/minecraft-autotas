package minecraft_autotas.levels.fifteenSeconds;

import minecraft_simulator.v1_8_9.block.Block;
import minecraft_simulator.v1_8_9.block.Blocks;
import minecraft_simulator.v1_8_9.world.AbstractXYZBlockGrid;

/**
 * Level 4 from SethBling's "15 seconds" https://youtu.be/n02enFUrrlw
 */
public class Level4 extends AbstractXYZBlockGrid {
  public boolean hasBlockAt(int x, int y, int z) {
    if (z < 10) {
      if (y == 0)
        return x == 1050 && z == 0;
      else if (y == 2)
        return x == 1050 && (z == -1 || z == 1) || z == 0 && (x == 1049 || x == 1051);
      else
        return false;
    } else if (y == 10) {
      if (x >= 1049 && x <= 1051)
        return z >= 19 && z <= 44 || z >= 50 && z <= 71;
      else
        return x >= 1052 && x <= 1056 && z >= 70 && z <= 71;
    } else if (y == 11)
      return x >= 1057 && x <= 1063 && z >= 70 && z <= 71;
    else if (y == 12)
      return x >= 1062 && x <= 1063 && z >= 63 && z <= 69;
    else if (y == 13)
      return x >= 1055 && x <= 1061 && z >= 63 && z <= 64;
    else if (y == 14) {
      if (x >= 1055 && x <= 1056)
        return z >= 65 && z <= 69 || z >= 73 && z <= 77;
      else if (z >= 76 && z <= 77)
        return x >= 1146 && x <= 1050;
      else if (z >= 78 && z <= 93)
        return x == 1146;
      else
        return x >= 1145 && x <= 1147 && z >= 94 && z <= 96;
    } else if (y == 16)
      return z == 95 && (x == 1145 || x == 1147);
    else
      return false;
  }

  @Override
  public Block getBlockAt(int x, int y, int z) {
    return hasBlockAt(x, y, z) ? z >= 22 && z <= 93 ? Blocks.ice : Blocks.fullBlock : Blocks.air;
  }
}
