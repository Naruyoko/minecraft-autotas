package minecraft_simulator.v1_14.collision;

import java.util.List;

// TODO: update
public interface ICollidable {
  public List<XYZBoundingBox> getCollidingBoundingBoxes(XYZBoundingBox bb);

  public boolean hasAnyCollidingBoundingBoxes(XYZBoundingBox bb);
}
