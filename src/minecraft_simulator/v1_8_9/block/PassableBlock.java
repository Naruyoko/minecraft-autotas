package minecraft_simulator.v1_8_9.block;

import java.util.List;

import minecraft_simulator.v1_8_9.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;

public class PassableBlock extends Block {
  @Override
  public void addCollisionBoxesToList(int x, int z, XZAxisAlignedBB mask, List<XZAxisAlignedBB> list) {}

  @Override
  public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZAxisAlignedBB mask) { return false; }

  @Override
  public XZAxisAlignedBB getCollisionBoundingBox(int x, int z) { return null; }

  @Override
  public void addCollisionBoxesToList(int x, int y, int z, XYZAxisAlignedBB mask, List<XYZAxisAlignedBB> list) {}

  @Override
  public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZAxisAlignedBB mask) { return false; }

  @Override
  public XYZAxisAlignedBB getCollisionBoundingBox(int x, int y, int z) { return null; }
}
