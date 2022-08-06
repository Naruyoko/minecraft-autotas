package minecraft_simulator.v1_8_9.collision;

/**
 * A mutable version of {net.minecraft.util.AxisAlignedBB}
 */
public class XYZAxisAlignedBB extends XZAxisAlignedBB {
  public double minY;
  public double maxY;

  public XYZAxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    super(minX, minZ, maxX, maxZ);
    this.minY = minY;
    this.maxY = maxY;
  }

  public XYZAxisAlignedBB clone() { return new XYZAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ); }

  public static XYZAxisAlignedBB copy(XYZAxisAlignedBB target, XYZAxisAlignedBB source) {
    target.minX = source.minX;
    target.minY = source.minY;
    target.minZ = source.minZ;
    target.maxX = source.maxX;
    target.maxY = source.maxY;
    target.maxZ = source.maxZ;
    return target;
  }

  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.offset(double, double,
   * double)} See also: {@link XYZAxisAlignedBB.offset}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB mutatingOffset(double x, double y, double z) {
    this.minX = this.minX + x;
    this.minY = this.minY + y;
    this.minZ = this.minZ + z;
    this.maxX = this.maxX + x;
    this.maxY = this.maxY + y;
    this.maxZ = this.maxZ + z;
    return this;
  }

  public static XYZAxisAlignedBB copyOffset(XYZAxisAlignedBB target, XYZAxisAlignedBB source, double x, double y,
      double z) {
    return copy(target, source).mutatingOffset(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.offset(double, double,
   * double)}. This returns a new instance.
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB offset(double x, double y, double z) { return this.clone().mutatingOffset(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.addCoord(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB mutatingAddCoord(double x, double y, double z) {
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

  public static XYZAxisAlignedBB copyAddCoord(XYZAxisAlignedBB target, XYZAxisAlignedBB source, double x, double y,
      double z) {
    return copy(target, source).mutatingAddCoord(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.addCoord(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB addCoord(double x, double y, double z) { return this.clone().mutatingAddCoord(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.expand(double, double,
   * double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB mutatingExpand(double x, double y, double z) {
    this.minX = this.minX - x;
    this.minY = this.minY - y;
    this.minZ = this.minZ - z;
    this.maxX = this.maxX + x;
    this.maxY = this.maxY + y;
    this.maxZ = this.maxZ + z;
    return this;
  }

  public static XYZAxisAlignedBB copyExpand(XYZAxisAlignedBB target, XYZAxisAlignedBB source, double x, double y,
      double z) {
    return copy(target, source).mutatingExpand(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.expand(double, double,
   * double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB expand(double x, double y, double z) { return this.clone().mutatingAddCoord(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.contract(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB mutatingContract(double x, double y, double z) {
    this.minX = this.minX + x;
    this.minY = this.minY + y;
    this.minZ = this.minZ + z;
    this.maxX = this.maxX - x;
    this.maxY = this.maxY - y;
    this.maxZ = this.maxZ - z;
    return this;
  }

  public static XYZAxisAlignedBB copyContract(XYZAxisAlignedBB target, XYZAxisAlignedBB source, double x, double y,
      double z) {
    return copy(target, source).mutatingContract(x, y, z);
  }

  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.contract(double,
   * double, double)}
   * 
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB contract(double x, double y, double z) { return this.clone().mutatingAddCoord(x, y, z); }

  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.expand(double, double,
   * double)} then {net.minecraft.util.AxisAlignedBB.contract(double, double,
   * double)}
   * 
   * @param x1
   * @param y1
   * @param z1
   * @param x2
   * @param y2
   * @param z2
   */
  public XYZAxisAlignedBB mutatingExpandAndContract(double x1, double y1, double z1, double x2, double y2, double z2) {
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

  public static XYZAxisAlignedBB copyExpandAndContract(XYZAxisAlignedBB target, XYZAxisAlignedBB source, double x1,
      double y1, double z1, double x2, double y2, double z2) {
    return copy(target, source).mutatingExpandAndContract(x1, y1, z1, x2, y2, z2);
  }

  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.expand(double, double,
   * double)} then {net.minecraft.util.AxisAlignedBB.contract(double, double,
   * double)}
   * 
   * @param x1
   * @param y1
   * @param z1
   * @param x2
   * @param y2
   * @param z2
   */
  public XYZAxisAlignedBB expandAndContract(double x1, double y1, double z1, double x2, double y2, double z2) {
    return this.clone().mutatingExpandAndContract(x1, y1, z1, x2, y2, z2);
  }

  /**
   * See {net.minecraft.util.AxisAlignedBB.calculateXOffset(AxisAlignedBB,
   * double)}
   * 
   * @param other
   * @return
   */
  public double calculateXOffset(XYZAxisAlignedBB other, double offsetX) {
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
   * See {net.minecraft.util.AxisAlignedBB.calculateYOffset(AxisAlignedBB,
   * double)}
   * 
   * @param other
   * @return
   */
  public double calculateYOffset(XYZAxisAlignedBB other, double offsetY) {
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
   * See {net.minecraft.util.AxisAlignedBB.calculateZOffset(AxisAlignedBB,
   * double)}
   * 
   * @param other
   * @return
   */
  public double calculateZOffset(XYZAxisAlignedBB other, double offsetZ) {
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
   * See {net.minecraft.util.AxisAlignedBB.intersectsWith(AxisAlignedBB)}
   * 
   * @param other
   * @return
   */
  public boolean intersectsWith(XYZAxisAlignedBB other) {
    return other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY
        && other.maxZ > this.minZ && other.minZ < this.maxZ;
  }
}
