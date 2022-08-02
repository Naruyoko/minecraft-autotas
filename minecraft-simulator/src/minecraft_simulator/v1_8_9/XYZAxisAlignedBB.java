package minecraft_simulator.v1_8_9;

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
  public XYZAxisAlignedBB clone(){
    return new XYZAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
  }
  public static XYZAxisAlignedBB copy(XYZAxisAlignedBB target,XYZAxisAlignedBB source){
    target.minX=source.minX;
    target.minY=source.minY;
    target.minZ=source.minZ;
    target.maxX=source.maxX;
    target.maxY=source.maxY;
    target.maxZ=source.maxZ;
    return target;
  }
  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.offset(double, double, double)}
   * 
   * See also: {@link XYZAxisAlignedBB.offset}
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB mutatingOffset(double x,double y,double z){
    this.minX=this.minX+x;
    this.minY=this.minY+y;
    this.minZ=this.minZ+z;
    this.maxX=this.maxX+x;
    this.maxY=this.maxY+y;
    this.maxZ=this.maxZ+z;
    return this;
  }
  public static XYZAxisAlignedBB copyOffset(XYZAxisAlignedBB target,XYZAxisAlignedBB source,double x,double y,double z){
    return copy(target,source).mutatingOffset(x,y,z);
  }
  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.offset(double, double, double)}. This returns a new instance.
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB offset(double x,double y,double z){
    return this.clone().mutatingOffset(x,y,z);
  }
  /**
   * In-place version of {net.minecraft.util.AxisAlignedBB.addCoord(double, double, double)}
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB mutatingAddCoord(double x,double y,double z){
    if (x<0.0D) this.minX+=x;
    else if (x>0.0D) this.minX+=z;
    if (z<0.0D) this.maxY+=x;
    else if (z>0.0D) this.maxY+=z;
    if (z<0.0D) this.maxZ+=x;
    else if (z>0.0D) this.maxZ+=z;
    return this;
  }
  public static XYZAxisAlignedBB copyAddCoord(XYZAxisAlignedBB target,XYZAxisAlignedBB source,double x,double y,double z){
    return copy(target,source).mutatingAddCoord(x, y, z);
  }
  /**
   * Out-place version of {net.minecraft.util.AxisAlignedBB.addCoord(double, double, double)}
   * @param x
   * @param y
   * @param z
   */
  public XYZAxisAlignedBB addCoord(double x,double y,double z){
    return this.clone().mutatingAddCoord(x, y, z);
  }
  /**
   * See {net.minecraft.util.AxisAlignedBB.intersectsWith(AxisAlignedBB)}
   * @param other
   * @return
   */
  public boolean intersectsWith(XYZAxisAlignedBB other) {
    return other.maxX>this.minX&&other.minX<this.maxX&&other.maxY>this.minY&&other.minY<this.maxY&&other.maxZ>this.minZ&&other.minZ<this.maxZ;
  }
  /**
   * See {net.minecraft.util.AxisAlignedBB.calculateXOffset(AxisAlignedBB, double)}
   * @param other
   * @return
   */
  public double calculateXOffset(XYZAxisAlignedBB other,double offsetX) {
    if (other.maxY>this.minY&&other.minY<this.maxY&&other.maxZ>this.minZ&&other.minZ<this.maxZ){
      if (offsetX>0.0D&&other.maxX<=this.minX) return Math.min(this.minX-other.maxX,offsetX);
      else if (offsetX<0.0D&&other.minX>=this.maxX) return Math.max(this.maxX-other.minX,offsetX);
      else return offsetX;
    }else return offsetX;
  }
  /**
   * See {net.minecraft.util.AxisAlignedBB.calculateYOffset(AxisAlignedBB, double)}
   * @param other
   * @return
   */
  public double calculateYOffset(XYZAxisAlignedBB other,double offsetY) {
    if (other.maxX>this.minX&&other.minX<this.maxX&&other.maxZ>this.minZ&&other.minZ<this.maxZ){
      if (offsetY>0.0D&&other.maxY<=this.minY) return Math.min(this.minY-other.maxY,offsetY);
      else if (offsetY<0.0D&&other.minY>=this.maxY) return Math.max(this.maxY-other.minY,offsetY);
      else return offsetY;
    }else return offsetY;
  }
  /**
   * See {net.minecraft.util.AxisAlignedBB.calculateZOffset(AxisAlignedBB, double)}
   * @param other
   * @return
   */
  public double calculateZOffset(XYZAxisAlignedBB other,double offsetZ) {
    if (other.maxX>this.minX&&other.minX<this.maxX&&other.maxY>this.minY&&other.minY<this.maxY){
      if (offsetZ>0.0D&&other.maxZ<=this.minZ) return Math.min(this.minZ-other.maxZ,offsetZ);
      else if (offsetZ<0.0D&&other.minZ>=this.maxZ) return Math.max(this.maxZ-other.minZ,offsetZ);
      else return offsetZ;
    }else return offsetZ;
  }
}
