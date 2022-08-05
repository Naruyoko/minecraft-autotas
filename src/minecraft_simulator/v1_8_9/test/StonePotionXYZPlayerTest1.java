package minecraft_simulator.v1_8_9.test;

import minecraft_simulator.v1_8_9.block.Block;
import minecraft_simulator.v1_8_9.block.Blocks;
import minecraft_simulator.v1_8_9.player.StonePotionXYZPlayer;
import minecraft_simulator.v1_8_9.util.Utility;
import minecraft_simulator.v1_8_9.world.AbstractXYZBlockGrid;
import minecraft_simulator.v1_8_9.world.IXYZMoveEntityHandler;
import minecraft_simulator.v1_8_9.world.XYZMoveEntityHandlerFromSimpleWorld;

public class StonePotionXYZPlayerTest1 {
  public static void main(String[] args) {
    // Inputs at physicsEmulationTest2.txt
    // Setup (O represents a fence):
    // OO 
    // OO 
    // O  
    // OOO
    //   O
    //   O
    //   O <- this is at (0,89,0)
    StonePotionXYZPlayer startPosition = new StonePotionXYZPlayer(0.5, 90.5, 0.5, 0, -0.0784000015258789, 0, 0);
    StonePotionXYZPlayer player = null;
    IXYZMoveEntityHandler<? super StonePotionXYZPlayer> moveEntityHandler = new XYZMoveEntityHandlerFromSimpleWorld(
        new AbstractXYZBlockGrid() {
          @Override
          public Block getBlockAt(int x, int y, int z) {
            if (y == 89) {
              if (x == 0 && z == 0)
                return Blocks.fenceConnectsSouth;
              if (x == 0 && z == 1)
                return Blocks.fenceConnectsNorthSouth;
              if (x == 0 && z == 2)
                return Blocks.fenceConnectsNorthSouth;
              if (x == 0 && z == 3)
                return Blocks.fenceConnectsNorthEast;
              if (x == 1 && z == 3)
                return Blocks.fenceConnectsWestEast;
              if (x == 2 && z == 3)
                return Blocks.fenceConnectsSouthWest;
              if (x == 2 && z == 4)
                return Blocks.fenceConnectsNorthSouth;
              if (x == 1 && z == 5)
                return Blocks.fenceConnectsSouthEast;
              if (x == 2 && z == 5)
                return Blocks.fenceConnectsNorthSouthWest;
              if (x == 1 && z == 6)
                return Blocks.fenceConnectsNorthEast;
              if (x == 2 && z == 6)
                return Blocks.fenceConnectsNorthWest;
            }
            return Blocks.air;
          }
        });
    float keyStrafe = 0.0F;
    float keyForward = 0.0F;
    boolean keyJump = false;
    boolean keySprint = false;
    boolean keySneak = false;
    for (int t = 0; t < 200; t++) {
      switch (t) {
      case 0:
        player = startPosition.clone();
        break;
      case 1:
        keyForward = StonePotionXYZPlayer.MOVE_FORWARD;
        keySprint = true;
        break;
      case 8:
        player.moveCamera(-300);
        break;
      case 17:
        player.moveCamera(300);
        break;
      case 18:
        player.moveCamera(92);
        break;
      case 23:
        keySneak = true;
        keySprint = false;
        break;
      case 24:
        keyForward = 0.0F;
        break;
      case 26:
        keySneak = false;
        break;
      case 29:
        keyForward = StonePotionXYZPlayer.MOVE_FORWARD;
        break;
      case 37:
        keyForward = 0.0F;
        break;
      case 50:
        player = startPosition.clone();
        player.setSpeedEffect(true, 5);
        break;
      case 51:
        keyForward = StonePotionXYZPlayer.MOVE_FORWARD;
        keySprint = true;
        break;
      case 54:
        player.moveCamera(-300);
        break;
      case 58:
        player.moveCamera(300);
        break;
      case 60:
        player.moveCamera(100);
        break;
      case 63:
        keySneak = true;
        keySprint = false;
        break;
      case 64:
        keyForward = 0.0F;
        break;
      case 67:
        keyForward = StonePotionXYZPlayer.MOVE_BACKWARD;
        break;
      case 74:
        keyForward = 0.0F;
        keySneak = false;
        break;
      case 78:
        keyForward = StonePotionXYZPlayer.MOVE_FORWARD;
        break;
      case 87:
        keyForward = 0.0F;
        break;
      case 100:
        player = startPosition.clone();
        player.setSlownessEffect(true, 1);
        break;
      case 101:
        keyForward = StonePotionXYZPlayer.MOVE_FORWARD;
        keySprint = true;
        break;
      case 112:
        player.moveCamera(-300);
        keyJump = true;
        break;
      case 113:
        keyJump = false;
        break;
      case 123:
        player.moveCamera(300);
        break;
      case 124:
        player.moveCamera(300);
        break;
      case 130:
        keyForward = 0.0F;
        keySprint = false;
        break;
      case 150:
        player = startPosition.clone();
        player.setJumpBoostEffect(true, 5);
        break;
      case 151:
        player.moveCamera(-100);
        keyForward = StonePotionXYZPlayer.MOVE_FORWARD;
        keyJump = true;
        keySprint = true;
        break;
      case 152:
        keyJump = false;
        break;
      case 170:
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
