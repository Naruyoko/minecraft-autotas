package minecraft_autotas.levels.fifteenSeconds;

import minecraft_simulator.v1_8_9.world.AbstractXYZStoneGrid;

/**
 * Level 1 from SethBling's "15 seconds" https://youtu.be/n02enFUrrlw
 */
public class Level1 extends AbstractXYZStoneGrid {
  @Override
  public boolean hasBlockAt(int x, int y, int z) {
    if (z < 10) {
      if (y == 0)
        return x == 1000 && z == 0;
      else if (y == 2)
        return x == 1000 && (z == -1 || z == 1) || z == 0 && (x == 999 || x == 1001);
      else
        return false;
    } else if (y == 10) {
      if (x >= 999 && x <= 1001)
        return z >= 19 && z <= 44 || z >= 50 && z <= 72;
      else
        return x >= 1002 && x <= 1006 && z >= 70 && z <= 72;
    } else if (y == 11)
      return x >= 1007 && x <= 1014 && z >= 70 && z <= 72;
    else if (y == 12)
      return x >= 1012 && x <= 1014 && z >= 62 && z <= 69;
    else if (y == 13)
      return x >= 1004 && x <= 1011 && z >= 62 && z <= 64;
    else if (y == 14) {
      if (x >= 1004 && x <= 1006)
        return z >= 65 && z <= 69 || z >= 73 && z <= 77;
      else if (z >= 75 && z <= 77)
        return x >= 995 && x <= 1000;
      else
        return x >= 995 && x <= 997 && z >= 78 && z <= 96;
    } else if (y == 16)
      return z == 95 && (x == 995 || x == 997);
    else
      return false;
  }
}
