package minecraft_simulator.v1_14.collision;

import minecraft_simulator.v1_14.block.Direction;

/**
 * A mutable and XZ-only version of {net.minecraft.util.math.BoundingBox}
 */
public class XZBoundingBox implements Cloneable {
  public double minX;
  public double minZ;
  public double maxX;
  public double maxZ;

  /**
   * Note: unlike the source, this does not swap if maximum given was smaller than
   * the minimum
   */
  public XZBoundingBox(double minX, double minZ, double maxX, double maxZ) {
    this.minX = minX;
    this.minZ = minZ;
    this.maxX = maxX;
    this.maxZ = maxZ;
  }

  public XZBoundingBox clone() { return new XZBoundingBox(minX, minZ, maxX, maxZ); }

  public static XZBoundingBox copy(XZBoundingBox target, XZBoundingBox source) {
    target.minX = source.minX;
    target.minZ = source.minZ;
    target.maxX = source.maxX;
    target.maxZ = source.maxZ;
    return target;
  }

  /**
   * See {net.minecraft.util.math.BoundingBox.getMin(Axis)}
   * 
   * @param axis
   * @return Lower bound along the given axis
   */
  public double getMin(Direction.Axis axis) { return axis.choose(this.minX, Double.NEGATIVE_INFINITY, this.minZ); }

  /**
   * See {net.minecraft.util.math.BoundingBox.getMax(Axis)}
   * 
   * @param axis
   * @return Upper bound along the given axis
   */
  public double getMax(Direction.Axis axis) { return axis.choose(this.maxX, Double.POSITIVE_INFINITY, this.maxZ); }

  /**
   * In-place and XZ-only version of
   * {net.minecraft.util.math.BoundingBox.offset(double, double, double)}
   * 
   * @param x
   * @param z
   */
  public XZBoundingBox mutatingOffset(double x, double z) {
    this.minX = this.minX + x;
    this.minZ = this.minZ + z;
    this.maxX = this.maxX + x;
    this.maxZ = this.maxZ + z;
    return this;
  }

  public static XZBoundingBox copyOffset(XZBoundingBox target, XZBoundingBox source, double x, double z) {
    return copy(target, source).mutatingOffset(x, z);
  }

  /**
   * Out-place and XZ-only version of
   * {net.minecraft.util.math.BoundingBox.offset(double, double, double)}
   * 
   * @param x
   * @param z
   */
  public XZBoundingBox offset(double x, double z) { return this.clone().mutatingOffset(x, z); }

  /**
   * In-place and XZ-only version of
   * {net.minecraft.util.math.BoundingBox.stretch(double, double, double)}
   * 
   * @param x
   * @param z
   */
  public XZBoundingBox mutatingStretch(double x, double z) {
    if (x < 0.0D)
      this.minX += x;
    else if (x > 0.0D)
      this.maxX += x;
    if (z < 0.0D)
      this.minZ += z;
    else if (z > 0.0D)
      this.maxZ += z;
    return this;
  }

  public static XZBoundingBox copyStretch(XZBoundingBox target, XZBoundingBox source, double x, double z) {
    return copy(target, source).mutatingStretch(x, z);
  }

  /**
   * Out-place and XZ-only version of
   * {net.minecraft.util.math.BoundingBox.stretch(double, double, double)}
   * 
   * @param x
   * @param z
   */
  public XZBoundingBox stretch(double x, double z) { return this.clone().mutatingStretch(x, z); }

  /**
   * XZ-only version of
   * {net.minecraft.util.math.BoundingBox.calculateXOffset(BoundingBox, double)}
   * 
   * TODO: find corresponding function
   * 
   * @param other
   * @return
   */
  public double calculateHorizontalXOffset(XZBoundingBox other, double offsetX) {
    if (other.maxZ > this.minZ && other.minZ < this.maxZ) {
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
   * XZ-only version of
   * {net.minecraft.util.math.BoundingBox.calculateZOffset(BoundingBox, double)}
   * 
   * TODO: find corresponding function
   * 
   * @param other
   * @return
   */
  public double calculateHorizontalZOffset(XZBoundingBox other, double offsetZ) {
    if (other.maxX > this.minX && other.minX < this.maxX) {
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
   * XZ-only version of
   * {net.minecraft.util.math.BoundingBox.intersects(BoundingBox)}
   * 
   * @param other
   * @return
   */
  public boolean intersectsHorizontally(XZBoundingBox other) {
    return this.minX < other.maxX && this.maxX > other.minX && this.minZ < other.maxZ && this.maxZ > other.minZ;
  }
}
