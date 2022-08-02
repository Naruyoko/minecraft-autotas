package minecraft_simulator.v1_8_9;

/**
 * A mutable and XZ-only version of {net.minecraft.util.AxisAlignedBB}
 */
public class XZAxisAlignedBB implements Cloneable {
  public double minX;
  public double minZ;
  public double maxX;
  public double maxZ;
  public XZAxisAlignedBB(double minX,double minZ,double maxX,double maxZ) {
    this.minX=minX;
    this.minZ=minZ;
    this.maxX=maxX;
    this.maxZ=maxZ;
  }
  public XZAxisAlignedBB clone(){
    return new XZAxisAlignedBB(minX, minZ, maxX, maxZ);
  }
  public static XZAxisAlignedBB copy(XZAxisAlignedBB target,XZAxisAlignedBB source){
    target.minX=source.minX;
    target.minZ=source.minZ;
    target.maxX=source.maxX;
    target.maxZ=source.maxZ;
    return target;
  }
  /**
   * In-place and XZ-only version of {net.minecraft.util.AxisAlignedBB.offset(double, double, double)}
   * @param x
   * @param z
   */
  public XZAxisAlignedBB mutatingOffset(double x,double z){
    this.minX=this.minX+x;
    this.minZ=this.minZ+z;
    this.maxX=this.maxX+x;
    this.maxZ=this.maxZ+z;
    return this;
  }
  public static XZAxisAlignedBB copyOffset(XZAxisAlignedBB target,XZAxisAlignedBB source,double x,double z){
    return copy(target,source).mutatingOffset(x,z);
  }
  /**
   * Out-place and XZ-only version of {net.minecraft.util.AxisAlignedBB.offset(double, double, double)}
   * @param x
   * @param z
   */
  public XZAxisAlignedBB offset(double x,double z){
    return this.clone().mutatingOffset(x,z);
  }
  /**
   * In-place and XZ-only version of {net.minecraft.util.AxisAlignedBB.addCoord(double, double, double)}
   * @param x
   * @param z
   */
  public XZAxisAlignedBB mutatingAddCoord(double x,double z){
    if (x<0.0D) this.minX+=x;
    else if (x>0.0D) this.minX+=z;
    if (z<0.0D) this.maxZ+=x;
    else if (z>0.0D) this.maxZ+=z;
    return this;
  }
  public static XZAxisAlignedBB copyAddCoord(XZAxisAlignedBB target,XZAxisAlignedBB source,double x,double z){
    return copy(target,source).mutatingAddCoord(x, z);
  }
  /**
   * Out-place and XZ-only version of {net.minecraft.util.AxisAlignedBB.addCoord(double, double, double)}
   * @param x
   * @param z
   */
  public XZAxisAlignedBB addCoord(double x,double z){
    return this.clone().mutatingAddCoord(x, z);
  }
  /**
   * XZ-only version of {net.minecraft.util.AxisAlignedBB.intersectsWith(AxisAlignedBB)}
   * @param other
   * @return
   */
  public boolean intersectsHorizontallyWith(XZAxisAlignedBB other) {
    return other.maxX>this.minX&&other.minX<this.maxX&&other.maxZ>this.minZ&&other.minZ<this.maxZ;
  }
  /**
   * XZ-only version of {net.minecraft.util.AxisAlignedBB.calculateXOffset(AxisAlignedBB, double)}
   * @param other
   * @return
   */
  public double calculateHorizontalXOffset(XZAxisAlignedBB other,double offsetX) {
    if (other.maxZ>this.minZ&&other.minZ<this.maxZ){
      if (offsetX>0.0D&&other.maxX<=this.minX) return Math.min(this.minX-other.maxX,offsetX);
      else if (offsetX<0.0D&&other.minX>=this.maxX) return Math.max(this.maxX-other.minX,offsetX);
      else return offsetX;
    }else return offsetX;
  }
  /**
   * XZ-only version of {net.minecraft.util.AxisAlignedBB.calculateZOffset(AxisAlignedBB, double)}
   * @param other
   * @return
   */
  public double calculateHorizontalZOffset(XZAxisAlignedBB other,double offsetZ) {
    if (other.maxX>this.minX&&other.minX<this.maxX){
      if (offsetZ>0.0D&&other.maxZ<=this.minZ) return Math.min(this.minZ-other.maxZ,offsetZ);
      else if (offsetZ<0.0D&&other.minZ>=this.maxZ) return Math.max(this.maxZ-other.minZ,offsetZ);
      else return offsetZ;
    }else return offsetZ;
  }
}
