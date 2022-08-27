package minecraft_simulator.v1_14.collision;

import java.util.List;

// TODO: update
public interface IHorizontallyCollidable {
  public List<XZBoundingBox> getHorizontallyCollidingBoundingBoxes(XZBoundingBox bb);

  public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZBoundingBox bb);
}
