package minecraft_simulator.v1_8_9.test;

import minecraft_simulator.v1_8_9.block.Block;
import minecraft_simulator.v1_8_9.block.Blocks;
import minecraft_simulator.v1_8_9.player.StoneXYZPlayer;
import minecraft_simulator.v1_8_9.util.Utility;
import minecraft_simulator.v1_8_9.world.AbstractXYZBlockGrid;
import minecraft_simulator.v1_8_9.world.IXYZMoveEntityHandler;
import minecraft_simulator.v1_8_9.world.XYZMoveEntityHandlerFromSimpleWorld;

public class StoneXYZPlayerTest1 {
  public static void main(String[] args) {
    // See physicsEmulationTest for the setup and inputs
    StoneXYZPlayer player = new StoneXYZPlayer(0, 80, 0, 0, -0.0784000015258789, 0, 0);
    IXYZMoveEntityHandler<? super StoneXYZPlayer> moveEntityHandler = new XYZMoveEntityHandlerFromSimpleWorld(
        new AbstractXYZBlockGrid() {
          @Override
          public Block getBlockAt(int x, int y, int z) {
            if (x == -1 || x == 0) {
              if (y == 79 && (z == -1 || z == 0 || z == 3 || z == 4 || z == 16 || z == 17 || z == 20))
                return Blocks.fullBlock;
              if (y == 83 && z == 16 || y == 82 && z == 20)
                return Blocks.fullBlock;
            }
            if (x == -1) {
              if (y == 80 && (z == 7 || z == 8 || z == 13 || z == 14 || z == 15))
                return Blocks.fullBlock;
              if ((y == 81 || y == 82) && z == 10)
                return Blocks.fullBlock;
            }
            if (x == 0) {
              if (y == 80 && (z == 24 || z == 25 || z == 26))
                return Blocks.fullBlock;
              if (y == 81 && z == 25)
                return Blocks.slabBottom;
            }
            if (z == 26) {
              if ((x == 0 || x == 1 || x == 2) && y == 81)
                return Blocks.fullBlock;
              if (x == 1 && y == 82)
                return Blocks.slabBottom;
              if (x == 2 && y == 82)
                return Blocks.fullBlock;
            }
            if ((x == 7 || x == 8) && y == 81 && (z == 25 || z == 26))
              return Blocks.fullBlock;
            return Blocks.air;
          }
        });
    float keyStrafe = 0.0F;
    float keyForward = 0.0F;
    boolean keyJump = false;
    boolean keySprint = false;
    boolean keySneak = false;
    for (int t = 0; t < 150; t++) {
      switch (t) {
      case 1:
        keyForward = StoneXYZPlayer.MOVE_FORWARD;
        keySprint = true;
        break;
      case 4:
        keyJump = true;
        break;
      case 5:
        keyJump = false;
        break;
      case 16:
        keyJump = true;
        break;
      case 17:
        keyJump = false;
        break;
      case 24:
        player.moveCamera(-400);
        keyStrafe = StoneXYZPlayer.STRAFE_RIGHT;
        break;
      case 29:
        player.moveCamera(100);
        break;
      case 32:
        player.moveCamera(300);
        keyStrafe = 0.0F;
        keyJump = true;
        break;
      case 33:
        player.moveCamera(-280);
        keyStrafe = StoneXYZPlayer.STRAFE_RIGHT;
        keyJump = false;
        break;
      case 44:
        player.moveCamera(280);
        keyStrafe = 0.0F;
        keyJump = true;
        break;
      case 45:
        keyJump = false;
        keySprint = false;
        break;
      case 55:
        keySneak = true;
        break;
      case 59:
        keyStrafe = StoneXYZPlayer.STRAFE_LEFT;
        break;
      case 79:
        keyForward = 0.0F;
        keyStrafe = 0.0F;
        break;
      case 81:
        keySneak = false;
        break;
      case 87:
        keyForward = StoneXYZPlayer.MOVE_FORWARD;
        break;
      case 95:
        keyJump = true;
        break;
      case 96:
        keyJump = false;
        break;
      case 98:
        keySprint = true;
        break;
      case 108:
        keyJump = true;
        break;
      case 109:
        keyJump = false;
        break;
      case 111:
        keyJump = true;
        break;
      case 112:
        keyJump = false;
        break;
      case 122:
        player.moveCamera(-600);
        break;
      case 124:
        keyJump = true;
        break;
      case 125:
        keyJump = false;
        break;
      case 126:
        keyJump = true;
        break;
      case 127:
        keyJump = false;
        break;
      case 128:
        keyJump = true;
        break;
      case 129:
        keyJump = false;
        break;
      case 141:
        keyForward = 0.0F;
        keySprint = false;
        break;
      default:
        break;
      }
      player.step(moveEntityHandler, keyStrafe, keyForward, keyJump, keySprint, keySneak);
      System.out.println(String.format("%3d %-10s %-22s %-22s %-22s %-22s %-22s %-22s %s", t,
          Utility.padSignFloat(player.yaw), Utility.padSignDouble(player.posX), Utility.padSignDouble(player.posY),
          Utility.padSignDouble(player.posZ), Utility.padSignDouble(player.velX), Utility.padSignDouble(player.velY),
          Utility.padSignDouble(player.velZ),
          (player.isSprinting ? "S" : " ") + (player.isCollidedHorizontally ? "H" : " ")
              + (player.isCollidedVertically ? "V" : " ") + (player.isCollided ? "C" : " ")
              + (player.onGround ? "G" : " ")));
    }
  }
}
