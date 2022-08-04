package minecraft_simulator.v1_8_9.block;

import java.util.List;

import minecraft_simulator.v1_8_9.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.player.AbstractXYZPlayer;
import minecraft_simulator.v1_8_9.world.SimulationFlagsIn;
import minecraft_simulator.v1_8_9.world.SimulationFlagsOut;

public class Blocks {
  public static final Block fullBlock = new Block();
  public static final Block slabBottom = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
  public static final Block slabTop = new Block(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
  // See {net.minecraft.block.BlockChest}
  public static final Block chestSingle = new Block(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  public static final Block chestPosZSide = new Block(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
  public static final Block chestSouthSide = chestPosZSide;
  public static final Block chestConnectsNorth = chestPosZSide;
  public static final Block chestNegZSide = new Block(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
  public static final Block chestNorthSide = chestNegZSide;
  public static final Block chestConnectsSouth = chestNegZSide;
  public static final Block chestPosXSide = new Block(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  public static final Block chestEastSide = chestPosXSide;
  public static final Block chestConnectsWest = chestPosXSide;
  public static final Block chestNegXSide = new Block(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
  public static final Block chestWestSide = chestNegXSide;
  public static final Block chestConnectsEast = chestNegXSide;
  // See {net.minecraft.block.BlockCuctus}
  public static final Block cactus = new Block() {
    @Override
    public XZAxisAlignedBB getCollisionBoundingBox(int x, int z) {
      final float missing = 0.0625F;
      return new XZAxisAlignedBB((double)((float)x + missing), (double)((float)z + missing),
          (double)((float)(x + 1) - missing), (double)((float)(z + 1) - missing));
    }

    @Override
    public XYZAxisAlignedBB getCollisionBoundingBox(int x, int y, int z) {
      final float missing = 0.0625F;
      return new XYZAxisAlignedBB((double)((float)x + missing), (double)y, (double)((float)z + missing),
          (double)((float)(x + 1) - missing), (double)((float)(y + 1) - missing), (double)((float)(z + 1) - missing));
    }

    @Override
    public void onEntityCollidedWithBlockIntersect(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
        SimulationFlagsOut flagsOut) {
      //TODO: damage player
    }
  };
  // See {net.minecraft.block.BlockCauldron}
  public static final Block cauldron = new Block() {
    @Override
    public void addCollisionBoxesToListAsFloor(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
      final float thickness = 0.125F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
      super.addCollisionBoxesToList(x, z, mask, list);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
      super.addCollisionBoxesToList(x, z, mask, list);
      this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(x, z, mask, list);
      this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(x, z, mask, list);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxesAsFloor(int x, int z, XZAxisAlignedBB mask) {
      final float thickness = 0.125F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
      if (super.hasAnyCollidingBoundingBoxes(x, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return false;
    }

    @Override
    public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
      super.addCollisionBoxesToList(x, y, z, mask, list);
      final float thickness = 0.125F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
      super.addCollisionBoxesToList(x, y, z, mask, list);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
      super.addCollisionBoxesToList(x, y, z, mask, list);
      this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(x, y, z, mask, list);
      this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
      super.addCollisionBoxesToList(x, y, z, mask, list);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, y, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      final float thickness = 0.125F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, y, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
      if (super.hasAnyCollidingBoundingBoxes(x, y, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, y, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
      if (super.hasAnyCollidingBoundingBoxes(x, y, z, mask)) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
      }
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      return false;
    }
  };
  // See {net.minecraft.block.BlockSlime}
  public static final Block slime = new Block() {
    @Override
    public void onLanded(AbstractXYZPlayer player, SimulationFlagsIn flagsIn, SimulationFlagsOut flagsOut) {
      if (flagsIn.isSneaking)
        super.onLanded(player, flagsIn, flagsOut);
      else if (player.velY < 0.0D)
        player.velY = -player.velY;
    }

    @Override
    public void onEntityCollidedWithBlockGround(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
        SimulationFlagsOut flagsOut) {
      final double absVelY = Math.abs(player.velY);
      if (!flagsIn.isSneaking && absVelY < 0.1D) {
        final double stickFactor = 0.4D + absVelY * 0.2D;
        player.velX *= stickFactor;
        player.velY *= stickFactor;
      }
      super.onEntityCollidedWithBlockGround(player, flagsIn, flagsOut);
    }
  };
  // See {net.minecraft.block.BlockSoulSand}
  public static final Block soulsand = new Block() {
    @Override
    public XZAxisAlignedBB getCollisionBoundingBox(int x, int z) {
      return new XZAxisAlignedBB((double)x, (double)z, (double)(x + 1), (double)(z + 1));
    }

    @Override
    public XYZAxisAlignedBB getCollisionBoundingBox(int x, int y, int z) {
      final float missing = 0.125F;
      return new XYZAxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)((float)(y + 1) - missing),
          (double)(z + 1));
    }

    @Override
    public void onEntityCollidedWithBlockIntersect(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
        SimulationFlagsOut flagsOut) {
      player.velX *= 0.4D;
      player.velZ *= 0.4D;
    }
  };
  // See {net.minecraft.block.BlockWeb}
  public static final Block web = new PassableBlock() {
    @Override
    public void onEntityCollidedWithBlockIntersect(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
        SimulationFlagsOut flagsOut) {
      flagsOut.isInWeb = true;
    }
  };

  public static final Block air = new PassableBlock();
}
