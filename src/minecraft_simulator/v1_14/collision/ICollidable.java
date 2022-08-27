package minecraft_simulator.v1_14.collision;

import java.util.List;

// TODO: update
public interface ICollidable {
  public List<XYZAxisAlignedBB> getCollidingBoundingBoxes(XYZAxisAlignedBB bb);

  public boolean hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB bb);
}
