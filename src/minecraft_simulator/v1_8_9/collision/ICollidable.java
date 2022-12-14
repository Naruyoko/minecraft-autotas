package minecraft_simulator.v1_8_9.collision;

import java.util.List;

public interface ICollidable {
  public List<XYZAxisAlignedBB> getCollidingBoundingBoxes(XYZAxisAlignedBB bb);

  public boolean hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB bb);
}
