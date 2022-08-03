package minecraft_simulator.v1_8_9.collision;

import java.util.List;

public interface IHorizontallyCollidable {
  public List<XZAxisAlignedBB> getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb);

  public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb);
}
