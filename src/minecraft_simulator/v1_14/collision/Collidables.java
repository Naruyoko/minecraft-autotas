package minecraft_simulator.v1_14.collision;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of trivial collidables
 */
//TODO: update
public class Collidables {
  interface IDualCollidable extends ICollidable, IHorizontallyCollidable {}

  public static final IDualCollidable empty = new IDualCollidable() {
    @Override
    public List<XYZBoundingBox> getCollidingBoundingBoxes(XYZBoundingBox bb) { return new ArrayList<>(); }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(XYZBoundingBox bb) { return false; }

    @Override
    public List<XZBoundingBox> getHorizontallyCollidingBoundingBoxes(XZBoundingBox bb) { return new ArrayList<>(); }

    @Override
    public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZBoundingBox bb) { return false; }
  };
  public static final IDualCollidable full = new IDualCollidable() {
    @Override
    public List<XYZBoundingBox> getCollidingBoundingBoxes(XYZBoundingBox bb) {
      List<XYZBoundingBox> list = new ArrayList<>(1);
      list.add(new XYZBoundingBox(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
          Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
      return list;
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(XYZBoundingBox bb) { return true; }

    @Override
    public List<XZBoundingBox> getHorizontallyCollidingBoundingBoxes(XZBoundingBox bb) {
      List<XZBoundingBox> list = new ArrayList<>(1);
      list.add(new XZBoundingBox(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
          Double.POSITIVE_INFINITY));
      return list;
    }

    @Override
    public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZBoundingBox bb) { return true; }
  };
}