package minecraft_simulator.v1_8_9;

public abstract class AbstractXZPlayer {
  // See {net.minecraft.entity.player.EntityPlayer.preparePlayerToSpawn()}
  public final float width=0.6F;
  public XZAxisAlignedBB boundingBox; // {net.minecraft.entity.Entity.boundingBox}
  public double posX; // {net.minecraft.entity.Entity.posX}
  public double posZ; // {net.minecraft.entity.Entity.posZ}
  public double velX; // {net.minecraft.entity.Entity.motionX}
  public double velZ; // {net.minecraft.entity.Entity.motionZ}
  public float yaw; // {net.minecraft.entity.Entity.rotationYaw}
  public AbstractXZPlayer(XZAxisAlignedBB boundingBox,double posX,double posZ,double velX,double velZ,float yaw) {
    this.boundingBox=boundingBox;
    this.posX=posX;
    this.posZ=posZ;
    this.velX=velX;
    this.velZ=velZ;
    this.yaw=yaw;
  }
  public AbstractXZPlayer(double posX,double posZ,double velX,double velZ,float yaw) {
    this(null,posX,posZ,velX,velZ,yaw);
    // See {net.minecraft.entity.Entity.setPosition(double, double, double)}
    final float halfWidth=width/2.0F;
    this.boundingBox=new XZAxisAlignedBB(posX-(double)halfWidth, posZ-(double)halfWidth, posX+(double)halfWidth, posZ+(double)halfWidth);
  }
  public AbstractXZPlayer() {
    this(0,0,0,0,0);
  }
  public static void copy(AbstractXZPlayer target,AbstractXZPlayer source){
    target.posX=source.posX;
    target.posZ=source.posZ;
    XZAxisAlignedBB.copy(target.boundingBox,source.boundingBox);
    target.velX=source.velX;
    target.velZ=source.velZ;
    target.yaw=source.yaw;
  }
  /**
   * Changes yaw as if the mouse was moved horizontally
   * @param pixels
   */
  public abstract void moveCamera(int pixels);
  public void resetPositionToBB(){
    posX=(boundingBox.minX+boundingBox.maxX)/2.0D;
    posZ=(boundingBox.minZ+boundingBox.maxZ)/2.0D;
  }
}
