package minecraft_simulator.v1_8_9.collision;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of trivial collidables
 */
public class Collidables {
  interface IDualCollidable extends ICollidable, IHorizontallyCollidable {}

  public static final IDualCollidable empty = new IDualCollidable() {
    @Override
    public List<XYZAxisAlignedBB> getCollidingBoundingBoxes(XYZAxisAlignedBB bb) { return new ArrayList<>(); }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB bb) { return false; }

    @Override
    public List<XZAxisAlignedBB> getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb) { return new ArrayList<>(); }

    @Override
    public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb) { return false; }
  };
  public static final IDualCollidable full = new IDualCollidable() {
    @Override
    public List<XYZAxisAlignedBB> getCollidingBoundingBoxes(XYZAxisAlignedBB bb) {
      List<XYZAxisAlignedBB> list = new ArrayList<>(1);
      list.add(new XYZAxisAlignedBB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
          Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
      return list;
    }

    @Override
    public boolean hasAnyCollidingBoundingBoxes(XYZAxisAlignedBB bb) { return true; }

    @Override
    public List<XZAxisAlignedBB> getHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb) {
      List<XZAxisAlignedBB> list = new ArrayList<>(1);
      list.add(new XZAxisAlignedBB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
          Double.POSITIVE_INFINITY));
      return list;
    }

    @Override
    public boolean hasAnyHorizontallyCollidingBoundingBoxes(XZAxisAlignedBB bb) { return true; }
  };
}