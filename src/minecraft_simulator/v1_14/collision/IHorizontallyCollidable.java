package minecraft_simulator.v1_14.collision;

import java.util.List;

// TODO: update
public interface IHorizontallyCollidable {
  public List<XZAxisAlignedBB> getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb);

  public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb);
}
