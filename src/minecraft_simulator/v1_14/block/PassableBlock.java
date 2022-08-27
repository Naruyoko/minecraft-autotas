package minecraft_simulator.v1_14.block;

import java.util.List;

import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.collision.XZBoundingBox;

// TODO: update
public class PassableBlock extends Block {
  @Override
  public void addCollisionBoxesToList(int x, int z, XZBoundingBox mask, List<XZBoundingBox> list) {}

  @Override
  public boolean hasAnyCollidingBoundingBoxes(int x, int z, XZBoundingBox mask) { return false; }

  @Override
  public XZBoundingBox getCollisionBoundingBox(int x, int z) { return null; }

  @Override
  public void addCollisionBoxesToList(int x, int y, int z, XYZBoundingBox mask, List<XYZBoundingBox> list) {}

  @Override
  public boolean hasAnyCollidingBoundingBoxes(int x, int y, int z, XYZBoundingBox mask) { return false; }

  @Override
  public XYZBoundingBox getCollisionBoundingBox(int x, int y, int z) { return null; }
}
