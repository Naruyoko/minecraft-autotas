package minecraft_simulator.v1_14.block;

import java.util.List;

import minecraft_simulator.v1_14.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_14.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_14.player.AbstractXYZPlayer;
import minecraft_simulator.v1_14.world.SimulationFlagsIn;
import minecraft_simulator.v1_14.world.SimulationFlagsOut;

// TODO: update
public class Blocks {
  public static final Block fullBlock = new Block();
  public static final Block air = new PassableBlock();
  // See {net.minecraft.block.BlockAnvil}
  public static final Block anvilX = new Block(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
  public static final Block anvilZ = new Block(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
  // See {net.minecraft.block.BlockBed}
  public static final Block bed = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.5625F, 1.0F);
  // See {net.minecraft.block.BlockBrewingStand}
  public static final Block brewingStand = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F) {
    @Override
    public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
      addCollisionBoxFromBoundsToList(x, y, z, 0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F, mask, list);
      super.addCollisionBoxesToList(x, y, z, mask, list);
    };

    @Override
    public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
      if (isCollidingBoxFromBounds(x, y, z, 0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F, mask))
        return true;
      if (super.hasAnyCollidingBoundingBoxes(x, y, z, mask))
        return true;
      return false;
    };
  };

  // See {net.minecraft.block.BlockCake}
  public static Block cake(int bites) {
    final float missing = 0.0625F;
    final float eaten = (float)(1 + bites * 2) / 16.0F;
    final float height = 0.5F;
    return new Block(eaten, 0.0F, missing, 1.0F - missing, height, 1.0F - missing);
  }

  public static final Block[] cakes = new Block[8];
  public static final Block cake0;
  public static final Block cake1;
  public static final Block cake2;
  public static final Block cake3;
  public static final Block cake4;
  public static final Block cake5;
  public static final Block cake6;
  public static final Block cake7;
  public static final Block cake;
  static {
    for (int bites = 0; bites < 8; bites++)
      cakes[bites] = cake(bites);
    cake0 = cakes[0];
    cake1 = cakes[1];
    cake2 = cakes[2];
    cake3 = cakes[3];
    cake4 = cakes[4];
    cake5 = cakes[5];
    cake6 = cakes[6];
    cake7 = cakes[7];
    cake = cake0;
  }

  // See {net.minecraft.block.BlockCarpet}
  public static final Block carpet = new Block(0.0F, 0.0F, 0.0F, 1.0F, (float)2 / 16.0F, 1.0F);
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

  // See {net.minecraft.block.BlockCocoa}
  public static Block cocoa(EnumFacing facing, int age) {
    final int widthPixels = 4 + age * 2;
    final int heightPixels = 5 + age * 2;
    final float halfWidth = (float)widthPixels / 2.0F;
    switch (facing) {
    case SOUTH:
      return new Block((8.0F - halfWidth) / 16.0F, (12.0F - (float)heightPixels) / 16.0F,
          (15.0F - (float)widthPixels) / 16.0F, (8.0F + halfWidth) / 16.0F, 0.75F, 0.9375F);
    case NORTH:
      return new Block((8.0F - halfWidth) / 16.0F, (12.0F - (float)heightPixels) / 16.0F, 0.0625F,
          (8.0F + halfWidth) / 16.0F, 0.75F, (1.0F + (float)widthPixels) / 16.0F);
    case WEST:
      return new Block(0.0625F, (12.0F - (float)heightPixels) / 16.0F, (8.0F - halfWidth) / 16.0F,
          (1.0F + (float)widthPixels) / 16.0F, 0.75F, (8.0F + halfWidth) / 16.0F);
    case EAST:
      return new Block((15.0F - (float)widthPixels) / 16.0F, (12.0F - (float)heightPixels) / 16.0F,
          (8.0F - halfWidth) / 16.0F, 0.9375F, 0.75F, (8.0F + halfWidth) / 16.0F);
    default:
      return new Block();
    }
  }

  public static final Block[] cocoas = new Block[12];
  public static final Block cocoaSouth0;
  public static final Block cocoaSouth1;
  public static final Block cocoaSouth2;
  public static final Block cocoaNorth0;
  public static final Block cocoaNorth1;
  public static final Block cocoaNorth2;
  public static final Block cocoaWest0;
  public static final Block cocoaWest1;
  public static final Block cocoaWest2;
  public static final Block cocoaEast0;
  public static final Block cocoaEast1;
  public static final Block cocoaEast2;
  static {
    for (EnumFacing facing : EnumFacing.HORIZONTALS)
      for (int age = 0; age < 3; age++)
        cocoas[facing.horizontalIndex | age << 2] = cocoa(facing, age);
    cocoaSouth0 = cocoas[EnumFacing.SOUTH.horizontalIndex | 0 << 2];
    cocoaSouth1 = cocoas[EnumFacing.SOUTH.horizontalIndex | 1 << 2];
    cocoaSouth2 = cocoas[EnumFacing.SOUTH.horizontalIndex | 2 << 2];
    cocoaNorth0 = cocoas[EnumFacing.NORTH.horizontalIndex | 0 << 2];
    cocoaNorth1 = cocoas[EnumFacing.NORTH.horizontalIndex | 1 << 2];
    cocoaNorth2 = cocoas[EnumFacing.NORTH.horizontalIndex | 2 << 2];
    cocoaWest0 = cocoas[EnumFacing.WEST.horizontalIndex | 0 << 2];
    cocoaWest1 = cocoas[EnumFacing.WEST.horizontalIndex | 1 << 2];
    cocoaWest2 = cocoas[EnumFacing.WEST.horizontalIndex | 2 << 2];
    cocoaEast0 = cocoas[EnumFacing.EAST.horizontalIndex | 0 << 2];
    cocoaEast1 = cocoas[EnumFacing.EAST.horizontalIndex | 1 << 2];
    cocoaEast2 = cocoas[EnumFacing.EAST.horizontalIndex | 2 << 2];
  }

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
      addCollisionBoxFromBoundsToList(x, z, 0.0F, 0.0F, thickness, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, z, 0.0F, 0.0F, 1.0F, thickness, mask, list);
      addCollisionBoxFromBoundsToList(x, z, 1.0F - thickness, 0.0F, 1.0F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, z, 0.0F, 1.0F - thickness, 1.0F, 1.0F, mask, list);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxesAsFloor(int x, int z, XZAxisAlignedBB mask) {
      final float thickness = 0.125F;
      if (isCollidingBoxFromBounds(x, z, 0.0F, 0.0F, thickness, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, z, 0.0F, 0.0F, 1.0F, thickness, mask))
        return true;
      if (isCollidingBoxFromBounds(x, z, 1.0F - thickness, 0.0F, 1.0F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, z, 0.0F, 1.0F - thickness, 1.0F, 1.0F, mask))
        return true;
      return false;
    }

    @Override
    public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F, mask, list);
      final float thickness = 0.125F;
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F, mask, list);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.3125F, 1.0F, mask))
        return true;
      final float thickness = 0.125F;
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F, mask))
        return true;
      return false;
    }
  };
  // See {net.minecraft.block.BlockDaylightDetector}
  public static final Block daylightDetector = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);

  // See {net.minecraft.block.BlockDoor}
  public static Block door(EnumFacing facing) {
    final float thickness = 0.1875F;
    switch (facing) {
    case EAST:
      return new Block(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
    case SOUTH:
      return new Block(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
    case WEST:
      return new Block(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    case NORTH:
      return new Block(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
    default:
      return new Block(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
    }
  }

  public static final Block[] doors = new Block[4];
  public static final Block doorEast;
  public static final Block doorSouth;
  public static final Block doorWest;
  public static final Block doorNorth;
  static {
    for (EnumFacing facing : EnumFacing.HORIZONTALS)
      doors[facing.horizontalIndex] = door(facing);
    doorEast = doors[EnumFacing.EAST.horizontalIndex];
    doorSouth = doors[EnumFacing.SOUTH.horizontalIndex];
    doorWest = doors[EnumFacing.WEST.horizontalIndex];
    doorNorth = doors[EnumFacing.NORTH.horizontalIndex];
  }
  // See {net.minecraft.block.BlockDragonEgg}
  public static final Block dragonEgg = new Block(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
  // See {net.minecraft.block.BlockEnchantmentTable}
  public static final Block enchantmentTable = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
  // See {net.minecraft.block.BlockEndPortalFrame}
  public static final Block endPortalFrameUnfilled = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F) {
    @Override
    public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F, mask, list);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F, mask))
        return true;
      return false;
    };
  };
  public static final Block endPortalFrame = endPortalFrameUnfilled;
  public static final Block endPortalFrameFilled = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F) {
    @Override
    public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F, mask, list);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 0.3125F, 0.8125F, 0.3125F, 0.6875F, 1.0F, 0.6875F, mask))
        return true;
      return false;
    };
  };

  // See {net.minecraft.block.BlockFence}
  public static Block fence(boolean connectsNorth, boolean connectsSouth, boolean connectsWest, boolean connectsEast) {
    final float centerMin = 0.375F;
    final float centerMax = 0.625F;
    final float minX = connectsWest ? 0.0F : centerMin;
    final float maxX = connectsEast ? 1.0F : centerMax;
    final float minZ = connectsNorth ? 0.0F : centerMin;
    final float maxZ = connectsSouth ? 1.0F : centerMax;
    if (connectsNorth || connectsSouth) {
      if (connectsWest || connectsEast)
        return new Block(minX, 0.0F, minZ, maxX, 1.0F, maxZ) {
          @Override
          public void addCollisionBoxesToList(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
            addCollisionBoxFromBoundsToList(x, z, centerMin, minZ, centerMax, maxZ, mask, list);
            addCollisionBoxFromBoundsToList(x, z, minX, centerMin, maxX, centerMax, mask, list);
          };

          @Override
          public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZAxisAlignedBB mask) {
            if (isCollidingBoxFromBounds(x, z, centerMin, minZ, centerMax, maxZ, mask))
              return true;
            if (isCollidingBoxFromBounds(x, z, minX, centerMin, maxX, centerMax, mask))
              return true;
            return false;
          };

          @Override
          public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
            addCollisionBoxFromBoundsToList(x, y, z, centerMin, 0.0F, minZ, centerMax, 1.5F, maxZ, mask, list);
            addCollisionBoxFromBoundsToList(x, y, z, minX, 0.0F, centerMin, maxX, 1.5F, centerMax, mask, list);
          };

          @Override
          public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
            if (isCollidingBoxFromBounds(x, y, z, centerMin, 0.0F, minZ, centerMax, 1.5F, maxZ, mask))
              return true;
            if (isCollidingBoxFromBounds(x, y, z, minX, 0.0F, centerMin, maxX, 1.5F, centerMax, mask))
              return true;
            return false;
          };
        };
      else
        return new Block(minX, 0.0F, minZ, maxX, 1.0F, maxZ) {
          @Override
          public void addCollisionBoxesToList(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
            addCollisionBoxFromBoundsToList(x, z, centerMin, minZ, centerMax, maxZ, mask, list);
          };

          @Override
          public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZAxisAlignedBB mask) {
            if (isCollidingBoxFromBounds(x, z, centerMin, minZ, centerMax, maxZ, mask))
              return true;
            return false;
          };

          @Override
          public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
            addCollisionBoxFromBoundsToList(x, y, z, centerMin, 0.0F, minZ, centerMax, 1.5F, maxZ, mask, list);
          };

          @Override
          public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
            if (isCollidingBoxFromBounds(x, y, z, centerMin, 0.0F, minZ, centerMax, 1.5F, maxZ, mask))
              return true;
            return false;
          };
        };
    } else
      return new Block(minX, 0.0F, minZ, maxX, 1.0F, maxZ) {
        @Override
        public void addCollisionBoxesToList(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
          addCollisionBoxFromBoundsToList(x, z, minX, centerMin, maxX, centerMax, mask, list);
        };

        @Override
        public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZAxisAlignedBB mask) {
          if (isCollidingBoxFromBounds(x, z, minX, centerMin, maxX, centerMax, mask))
            return true;
          return false;
        };

        @Override
        public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
          addCollisionBoxFromBoundsToList(x, y, z, minX, 0.0F, centerMin, maxX, 1.5F, centerMax, mask, list);
        };

        @Override
        public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
          if (isCollidingBoxFromBounds(x, y, z, minX, 0.0F, centerMin, maxX, 1.5F, centerMax, mask))
            return true;
          return false;
        };
      };
  }

  public static final Block fenceConnectsNone = fence(false, false, false, false);
  public static final Block fenceConnectsEast = fence(false, false, false, true);
  public static final Block fenceConnectsWest = fence(false, false, true, false);
  public static final Block fenceConnectsWestEast = fence(false, false, true, true);
  public static final Block fenceConnectsSouth = fence(false, true, false, false);
  public static final Block fenceConnectsSouthEast = fence(false, true, false, true);
  public static final Block fenceConnectsSouthWest = fence(false, true, true, false);
  public static final Block fenceConnectsSouthWestEast = fence(false, true, true, true);
  public static final Block fenceConnectsNorth = fence(true, false, false, false);
  public static final Block fenceConnectsNorthEast = fence(true, false, false, true);
  public static final Block fenceConnectsNorthWest = fence(true, false, true, false);
  public static final Block fenceConnectsNorthWestEast = fence(true, false, true, true);
  public static final Block fenceConnectsNorthSouth = fence(true, true, false, false);
  public static final Block fenceConnectsNorthSouthEast = fence(true, true, false, true);
  public static final Block fenceConnectsNorthSouthWest = fence(true, true, true, false);
  public static final Block fenceConnectsNorthSouthWestEast = fence(true, true, true, true);
  public static final Block fence = fenceConnectsNone;
  // See {net.minecraft.block.BlockFenceGate}
  public static final Block fenceGateX = new Block(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
  public static final Block fenceGateZ = new Block(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
  // See {net.minecraft.block.BlockFlowerPot}
  public static final Block flowerPot;
  static {
    final float length = 0.375F;
    final float halfLength = length / 2.0F;
    flowerPot = new Block(0.5F - halfLength, 0.0F, 0.5F - halfLength, 0.5F + halfLength, length, 0.5F + halfLength);
  }
  // See {net.minecraft.block.BlockHopper}
  public static final Block hopper = new Block() {
    @Override
    public void addCollisionBoxesToListAsFloor(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {
      final float thickness = 0.125F;
      addCollisionBoxFromBoundsToList(x, z, 0.0F, 0.0F, thickness, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, z, 0.0F, 0.0F, 1.0F, thickness, mask, list);
      addCollisionBoxFromBoundsToList(x, z, 1.0F - thickness, 0.0F, 1.0F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, z, 0.0F, 1.0F - thickness, 1.0F, 1.0F, mask, list);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxesAsFloor(int x, int z, XZAxisAlignedBB mask) {
      final float thickness = 0.125F;
      if (isCollidingBoxFromBounds(x, z, 0.0F, 0.0F, thickness, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, z, 0.0F, 0.0F, 1.0F, thickness, mask))
        return true;
      if (isCollidingBoxFromBounds(x, z, 1.0F - thickness, 0.0F, 1.0F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, z, 0.0F, 1.0F - thickness, 1.0F, 1.0F, mask))
        return true;
      return false;
    }

    @Override
    public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F, mask, list);
      final float thickness = 0.125F;
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, mask, list);
      addCollisionBoxFromBoundsToList(x, y, z, 0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F, mask, list);
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) {
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F, mask))
        return true;
      final float thickness = 0.125F;
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, mask))
        return true;
      if (isCollidingBoxFromBounds(x, y, z, 0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F, mask))
        return true;
      return false;
    }
  };
  // See {net.minecraft.block.BlockIce}
  public static final Block ice = new Block();
  static {
    ice.setSlipperiness(0.98F);
  }

  // See {net.minecraft.block.BlockLadder}
  public static Block ladder(EnumFacing facing) {
    final float thickness = 0.125F;
    Block block;
    switch (facing) {
    case NORTH:
      block = new Block(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
    case SOUTH:
      block = new Block(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
    case WEST:
      block = new Block(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    case EAST:
    default:
      block = new Block(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
    }
    block.isLadder = true;
    return block;
  }

  public static final Block[] ladders = new Block[4];
  public static final Block ladderNorth;
  public static final Block ladderSouth;
  public static final Block ladderWest;
  public static final Block ladderEast;
  static {
    for (EnumFacing facing : EnumFacing.HORIZONTALS)
      ladders[facing.horizontalIndex] = ladder(facing);
    ladderNorth = ladders[EnumFacing.NORTH.horizontalIndex];
    ladderSouth = ladders[EnumFacing.SOUTH.horizontalIndex];
    ladderWest = ladders[EnumFacing.WEST.horizontalIndex];
    ladderEast = ladders[EnumFacing.EAST.horizontalIndex];
  }
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
  static {
    slime.setSlipperiness(0.8F);
  }
  // See {net.minecraft.block.BlockLilyPad}
  public static final Block lilyPad;
  static {
    final float halfWidth = 0.5F;
    final float height = 0.015625F;
    lilyPad = new Block(0.5F - halfWidth, 0.0F, 0.5F - halfWidth, 0.5F + halfWidth, height, 0.5F + halfWidth);
  }
  public static final Block[] waters = new Block[8];
  public static final Block[] lavas = new Block[8];
  public static final Block water0;
  public static final Block water1;
  public static final Block water2;
  public static final Block water3;
  public static final Block water4;
  public static final Block water5;
  public static final Block water6;
  public static final Block water7;
  public static final Block water;
  public static final Block lava0;
  public static final Block lava1;
  public static final Block lava2;
  public static final Block lava3;
  public static final Block lava4;
  public static final Block lava5;
  public static final Block lava6;
  public static final Block lava7;
  public static final Block lava;
  static {
    for (int level = 0; level < 8; level++) {
      waters[level] = new BlockLiquid(false, level);
      lavas[level] = new BlockLiquid(true, level);
    }
    water0 = waters[0];
    water1 = waters[1];
    water2 = waters[2];
    water3 = waters[3];
    water4 = waters[4];
    water5 = waters[5];
    water6 = waters[6];
    water7 = waters[7];
    water = water0;
    lava0 = lavas[0];
    lava1 = lavas[1];
    lava2 = lavas[2];
    lava3 = lavas[3];
    lava4 = lavas[4];
    lava5 = lavas[5];
    lava6 = lavas[6];
    lava7 = lavas[7];
    lava = lava0;
  }
  //TODO: pane
  //TODO: pistonBase
  //TODO: pistonExtension
  // See {net.minecraft.block.BlockRedstoneDiode}
  public static final Block redstoneDiode = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
  public static final Block redstoneRepeater = redstoneDiode;
  // See {net.minecraft.block.BlockSkull}
  public static final Block skullUp = new Block(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
  public static final Block skullNorth = new Block(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
  public static final Block skullSouth = new Block(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
  public static final Block skullWest = new Block(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
  public static final Block skullEast = new Block(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
  // See {net.minecraft.block.BlockSlab}
  public static final Block slabBottom = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
  public static final Block slabTop = new Block(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
  public static final Block slabDouble = new Block(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

  // See {net.minecraft.block.BlockSnow}
  public static Block snow(int layers) { return new Block(0.0F, 0.0F, 0.0F, 1.0F, (float)layers / 8.0F, 1.0F); }

  public static final Block[] snows = new Block[8];
  public static final Block snow0;
  public static final Block snow1;
  public static final Block snow2;
  public static final Block snow3;
  public static final Block snow4;
  public static final Block snow5;
  public static final Block snow6;
  public static final Block snow7;
  public static final Block snow;
  static {
    for (int bites = 0; bites < 8; bites++)
      snows[bites] = snow(bites);
    snow0 = snows[0];
    snow1 = snows[1];
    snow2 = snows[2];
    snow3 = snows[3];
    snow4 = snows[4];
    snow5 = snows[5];
    snow6 = snows[6];
    snow7 = snows[7];
    snow = snow0;
  }
  //TODO: stairs
  // See {net.minecraft.block.BlockTrapDoor}
  public static final Block trapDoorUp = new Block(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
  public static final Block trapDoorDown = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
  public static final Block trapDoorNorth = new Block(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
  public static final Block trapDoorSouth = new Block(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
  public static final Block trapDoorWest = new Block(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
  public static final Block trapDoorEast = new Block(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
  // See {net.minecraft.block.BlockVine}
  public static final Block vine = new PassableBlock();
  static {
    vine.isLadder = true;
  }
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

  // See {net.minecraft.block.BlockWall}
  public static Block wall(boolean connectsNorth, boolean connectsSouth, boolean connectsWest, boolean connectsEast) {
    float minX = 0.25F;
    float maxX = 0.75F;
    float minZ = 0.25F;
    float maxZ = 0.75F;
    if (connectsNorth)
      minZ = 0.0F;
    if (connectsSouth)
      maxZ = 1.0F;
    if (connectsWest)
      minZ = 0.0F;
    if (connectsEast)
      maxZ = 1.0F;
    if (connectsNorth && connectsSouth && !connectsWest && !connectsEast) {
      minX = 0.3125F;
      maxX = 0.6875F;
    } else if (!connectsNorth && !connectsSouth && connectsWest && connectsEast) {
      minZ = 0.3125F;
      maxZ = 0.6875F;
    }
    return new Block(minX, 0.0F, minZ, maxX, 1.5F, maxZ);
  }

  public static final Block wallConnectsNone = wall(false, false, false, false);
  public static final Block wallConnectsEast = wall(false, false, false, true);
  public static final Block wallConnectsWest = wall(false, false, true, false);
  public static final Block wallConnectsWestEast = wall(false, false, true, true);
  public static final Block wallConnectsSouth = wall(false, true, false, false);
  public static final Block wallConnectsSouthEast = wall(false, true, false, true);
  public static final Block wallConnectsSouthWest = wall(false, true, true, false);
  public static final Block wallConnectsSouthWestEast = wall(false, true, true, true);
  public static final Block wallConnectsNorth = wall(true, false, false, false);
  public static final Block wallConnectsNorthEast = wall(true, false, false, true);
  public static final Block wallConnectsNorthWest = wall(true, false, true, false);
  public static final Block wallConnectsNorthWestEast = wall(true, false, true, true);
  public static final Block wallConnectsNorthSouth = wall(true, true, false, false);
  public static final Block wallConnectsNorthSouthEast = wall(true, true, false, true);
  public static final Block wallConnectsNorthSouthWest = wall(true, true, true, false);
  public static final Block wallConnectsNorthSouthWestEast = wall(true, true, true, true);
  public static final Block wall = wallConnectsNone;

  // See {net.minecraft.block.BlockWeb}
  public static final Block web = new PassableBlock() {
    @Override
    public void onEntityCollidedWithBlockIntersect(AbstractXYZPlayer player, SimulationFlagsIn flagsIn,
        SimulationFlagsOut flagsOut) {
      flagsOut.isInWeb = true;
    }
  };

}
