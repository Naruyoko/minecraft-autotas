package minecraft_simulator.v1_8_9;

/**
 * A mutable and XZ-only version of {net.minecraft.util.AxisAlignedBB}
 */
public class MutableXZAxisAlignedBB {
  public double minX;
  public double minZ;
  public double maxX;
  public double maxZ;
  public MutableXZAxisAlignedBB(double minX,double minZ,double maxX,double maxZ) {
    this.minX=minX;
    this.minZ=minZ;
    this.maxX=maxX;
    this.maxZ=maxZ;
  }
  public MutableXZAxisAlignedBB clone(){
    return new MutableXZAxisAlignedBB(minX, minZ, maxX, maxZ);
  }
  public static void copy(MutableXZAxisAlignedBB target,MutableXZAxisAlignedBB source){
    target.minX=source.minX;
    target.minZ=source.minZ;
    target.maxX=source.maxX;
    target.maxZ=source.maxZ;
  }
  /**
   * In-place and horizontal version of {net.minecraft.util.AxisAlignedBB.offset(double, double, double)}
   * @param x
   * @param z
   */
  public void move(double x,double z){
    this.minX=this.minX+x;
    this.minZ=this.minZ+z;
    this.maxX=this.maxX+x;
    this.maxZ=this.maxZ+z;
  }
  /**
   * Horizontal component-only version of {net.minecraft.util.AxisAlignedBB.intersectsWith(AxisAlignedBB)}
   * See also {net.minecraft.util.AxisAlignedBB.calculateYOffset(AxisAlignedBB, double)}
   * @param other
   * @return
   */
  public boolean intersectsHorizontally(MutableXZAxisAlignedBB other){
    return other.maxX>this.minX&&other.minX<this.maxX&&other.maxZ>this.minZ&&other.minZ<this.maxZ;
  }
}
