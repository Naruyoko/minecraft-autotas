package minecraft_simulator.v1_14.collision;

import minecraft_simulator.v1_14.block.Direction;

/**
 * A mutable version of {net.minecraft.util.math.BoundingBox}
 */
public class XYZBoundingBox extends XZBoundingBox {
  public double minY;
  public double maxY;

  /**
   * Note: unlike the source, this does not swap if maximum given was smaller than
   * the minimum
   */
  public XYZBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    super(minX, minZ, maxX, maxZ);
    this.minY = minY;
    this.maxY = maxY;
  }

  public XYZBoundingBox clone() { return new XYZBoundingBox(minX, minY, minZ, maxX, maxY, maxZ); }

  public static XYZBoundingBox copy(XYZBoundingBox target, XYZBoundingBox source) {
    target.minX = source.minX;
    target.minY = source.minY;
    target.minZ = source.minZ;
    target.maxX = source.maxX;
    target.maxY = source.maxY;
    target.maxZ = source.maxZ;
    return target;
  }

  /**
   * See {net.minecraft.util.math.BoundingBox.getMin(Axis)}
   * 
   * @param axis
   * @return Lower bound along the given axis
   */
  public double getMin(Direction.Axis axis) { return axis.choose(this.minX, this.minY, this.minZ); }

  /**
   * See {net.minecraft.util.math.BoundingBox.getMax(Axis)}
   * 
   * @param axis
   * @return Upper bound along the given axis
   */
  public double getMax(Direction.Axis axis) { return axis.choose(this.maxX, this.maxY, this.maxZ); }

  /**
   * In-place version of {net.minecraft.util.math.BoundingBox.offset(double,
   * double, double)} See also: {@link XYZBoundingBox.offset}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZBoundingBox mutatingOffset(double x, double y, double z) {
    this.minX = this.minX + x;
    this.minY = this.minY + y;
    this.minZ = this.minZ + z;
    this.maxX = this.maxX + x;
    this.maxY = this.maxY + y;
    this.maxZ = this.maxZ + z;
    return this;
  }

  public static XYZBoundingBox copyOffset(XYZBoundingBox target, XYZBoundingBox source, double x, double y, double z) {
    return copy(target, source).mutatingOffset(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.math.BoundingBox.offset(double,
   * double, double)}. This returns a new instance.
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZBoundingBox offset(double x, double y, double z) { return this.clone().mutatingOffset(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.math.BoundingBox.stretch(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZBoundingBox mutatingStretch(double x, double y, double z) {
    if (x < 0.0D)
      this.minX += x;
    else if (x > 0.0D)
      this.maxX += x;
    if (y < 0.0D)
      this.minY += y;
    else if (y > 0.0D)
      this.maxY += y;
    if (z < 0.0D)
      this.minZ += z;
    else if (z > 0.0D)
      this.maxZ += z;
    return this;
  }

  public static XYZBoundingBox copyStretch(XYZBoundingBox target, XYZBoundingBox source, double x, double y, double z) {
    return copy(target, source).mutatingStretch(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.math.BoundingBox.stretch(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZBoundingBox stretch(double x, double y, double z) { return this.clone().mutatingStretch(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.math.BoundingBox.expand(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZBoundingBox mutatingExpand(double x, double y, double z) {
    this.minX = this.minX - x;
    this.minY = this.minY - y;
    this.minZ = this.minZ - z;
    this.maxX = this.maxX + x;
    this.maxY = this.maxY + y;
    this.maxZ = this.maxZ + z;
    return this;
  }

  public static XYZBoundingBox copyExpand(XYZBoundingBox target, XYZBoundingBox source, double x, double y, double z) {
    return copy(target, source).mutatingExpand(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.math.BoundingBox.expand(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZBoundingBox expand(double x, double y, double z) { return this.clone().mutatingStretch(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.math.BoundingBox.expand(double,
   * double, double)} then {net.minecraft.util.math.BoundingBox.contract(double,
   * double, double)}
   * 
   * TODO: update
   * 
   * @param x1
   * @param y1
   * @param z1
   * @param x2
   * @param y2
   * @param z2
   */
  public XYZBoundingBox mutatingExpandAndContract(double x1, double y1, double z1, double x2, double y2, double z2) {
    this.minX = this.minX - x1;
    this.minY = this.minY - y1;
    this.minZ = this.minZ - z1;
    this.maxX = this.maxX + x1;
    this.maxY = this.maxY + y1;
    this.maxZ = this.maxZ + z1;
    this.minX = this.minX + x2;
    this.minY = this.minY + y2;
    this.minZ = this.minZ + z2;
    this.maxX = this.maxX - x2;
    this.maxY = this.maxY - y2;
    this.maxZ = this.maxZ - z2;
    return this;
  }

  public static XYZBoundingBox copyExpandAndContract(XYZBoundingBox target, XYZBoundingBox source, double x1, double y1,
      double z1, double x2, double y2, double z2) {
    return copy(target, source).mutatingExpandAndContract(x1, y1, z1, x2, y2, z2);
  }

  /**
   * Out-place version of {net.minecraft.util.math.BoundingBox.expand(double,
   * double, double)} then {net.minecraft.util.math.BoundingBox.contract(double,
   * double, double)}
   * 
   * TODO: update
   * 
   * @param x1
   * @param y1
   * @param z1
   * @param x2
   * @param y2
   * @param z2
   */
  public XYZBoundingBox expandAndContract(double x1, double y1, double z1, double x2, double y2, double z2) {
    return this.clone().mutatingExpandAndContract(x1, y1, z1, x2, y2, z2);
  }

  /**
   * See {net.minecraft.util.math.BoundingBox.calculateXOffset(BoundingBox,
   * double)}
   * 
   * TODO: find corresponding function
   * 
   * @param other
   * @return
   */
  public double calculateXOffset(XYZBoundingBox other, double offsetX) {
    if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
      if (offsetX > 0.0D && other.maxX <= this.minX)
        return Math.min(this.minX - other.maxX, offsetX);
      else if (offsetX < 0.0D && other.minX >= this.maxX)
        return Math.max(this.maxX - other.minX, offsetX);
      else
        return offsetX;
    } else
      return offsetX;
  }

  /**
   * See {net.minecraft.util.math.BoundingBox.calculateYOffset(BoundingBox,
   * double)}
   * 
   * TODO: find corresponding function
   * 
   * @param other
   * @return
   */
  public double calculateYOffset(XYZBoundingBox other, double offsetY) {
    if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
      if (offsetY > 0.0D && other.maxY <= this.minY)
        return Math.min(this.minY - other.maxY, offsetY);
      else if (offsetY < 0.0D && other.minY >= this.maxY)
        return Math.max(this.maxY - other.minY, offsetY);
      else
        return offsetY;
    } else
      return offsetY;
  }

  /**
   * See {net.minecraft.util.math.BoundingBox.calculateZOffset(BoundingBox,
   * double)}
   * 
   * TODO: find corresponding function
   * 
   * @param other
   * @return
   */
  public double calculateZOffset(XYZBoundingBox other, double offsetZ) {
    if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
      if (offsetZ > 0.0D && other.maxZ <= this.minZ)
        return Math.min(this.minZ - other.maxZ, offsetZ);
      else if (offsetZ < 0.0D && other.minZ >= this.maxZ)
        return Math.max(this.maxZ - other.minZ, offsetZ);
      else
        return offsetZ;
    } else
      return offsetZ;
  }

  /**
   * See {net.minecraft.util.math.BoundingBox.intersects(BoundingBox)}
   * 
   * @param other
   * @return
   */
  public boolean intersects(XYZBoundingBox other) {
    return this.minX < other.maxX && this.maxX > other.minX && this.minY < other.maxY && this.maxY > other.minY
        && this.minZ < other.maxZ && this.maxZ > other.minZ;
  }
}
