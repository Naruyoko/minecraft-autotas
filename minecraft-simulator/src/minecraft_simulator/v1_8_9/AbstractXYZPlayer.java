package minecraft_simulator.v1_8_9;

public abstract class AbstractXYZPlayer extends AbstractXZPlayer {
  // See {net.minecraft.entity.player.EntityPlayer.preparePlayerToSpawn()}
  public final float height=1.8F;
  public final float stepHeight=0.6F; // {net.minecraft.entity.Entity.stepHeight}, see {net.minecraft.entity.EntityLivingBase.EntityLivingBase(World)}
  public XYZAxisAlignedBB boundingBox; // {net.minecraft.entity.Entity.boundingBox}
  public double posY; // {net.minecraft.entity.Entity.posZ}
  public double velY; // {net.minecraft.entity.Entity.motionY}
  public AbstractXYZPlayer(XYZAxisAlignedBB boundingBox, double posX, double posY, double posZ, double velX,
      double velY, double velZ, float yaw) {
    super(boundingBox, posX, posZ, velX, velZ, yaw);
    this.posY = posY;
    this.velY = velY;
  }
  public AbstractXYZPlayer(double posX,double posY,double posZ,double velX,double velY,double velZ,float yaw) {
    this(null,posX,posY,posZ,velX,velY,velZ,yaw);
    // See {net.minecraft.entity.Entity.setPosition(double, double, double)}
    final float halfWidth=width/2.0F;
    this.boundingBox=new XYZAxisAlignedBB(posX-(double)halfWidth, posY, posZ-(double)halfWidth, posX+(double)halfWidth, posY+(double)height, posZ+(double)halfWidth);
  }
  public AbstractXYZPlayer() {
    this(0,0,0,0,0,0,0);
  }
  public static void copy(AbstractXYZPlayer target,AbstractXYZPlayer source){
    target.posX=source.posX;
    target.posY=source.posY;
    target.posZ=source.posZ;
    XYZAxisAlignedBB.copy(target.boundingBox,source.boundingBox);
    target.velX=source.velX;
    target.velY=source.velY;
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
    posY=(boundingBox.minY+boundingBox.maxY)/2.0D;
    posZ=(boundingBox.minZ+boundingBox.maxZ)/2.0D;
  }
}
