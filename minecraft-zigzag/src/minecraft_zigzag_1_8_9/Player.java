package minecraft_zigzag_1_8_9;
public class Player {
  // See {net.minecraft.entity.player.EntityPlayer.preparePlayerToSpawn()}
  static final float width=0.6F;

  public AxisAlignedBB boundingBox; // {net.minecraft.entity.Entity.boundingBox}
  public double posX; // {net.minecraft.entity.Entity.posX}
  public double posZ; // {net.minecraft.entity.Entity.posZ}
  public double velX; // {net.minecraft.entity.Entity.motionX}
  public double velZ; // {net.minecraft.entity.Entity.motionZ}
  public float yaw; // {net.minecraft.entity.Entity.rotationYaw}
  public Player(double posX,double posZ,AxisAlignedBB boundingBox,double velX,double velZ,float yaw) {
    this.posX=posX;
    this.posZ=posZ;
    this.boundingBox=boundingBox;
    this.velX=velX;
    this.velZ=velZ;
    this.yaw=yaw;
  }
  public Player(double posX,double posZ,double velX,double velZ,float yaw) {
    this(posX,posZ,null,velX,velZ,yaw);
    // See {net.minecraft.entity.Entity.setPosition(double, double, double)}
    final float halfWidth=width/2.0F;
    this.boundingBox=new AxisAlignedBB(posX-(double)halfWidth, posZ-(double)halfWidth, posX+(double)halfWidth, posZ+(double)halfWidth);
  }
  public Player clone() {
    return new Player(posX, posZ, boundingBox.clone(), velX, velZ, yaw);
  }
  public static void copy(Player target,Player source) {
    target.posX=source.posX;
    target.posZ=source.posZ;
    AxisAlignedBB.copy(target.boundingBox,source.boundingBox);
    target.velX=source.velX;
    target.velZ=source.velZ;
    target.yaw=source.yaw;
  }

  // See https://www.mcpk.wiki/wiki/Mouse_Movement and {net.minecraft.client.renderer.EntityRenderer.updateCameraAndRender(float, long)}
  static final float mouseSensitivity=0.5F;
  static final float mouseMult_intermediate=mouseSensitivity*0.6F+0.2F;
  static final float mouseMult=mouseMult_intermediate*mouseMult_intermediate*mouseMult_intermediate*8.0F;
  /**
   * Changes yaw as if the mouse was moved horizontally at default sensitivity
   * @param pixels
   * @return
   */
  public void moveCamera(int pixels) {
    float yawD=(float)pixels*mouseMult;
    //See {net.minecraft.entity.Entity#setAngles}
    yaw=(float)((double)yaw+(double)yawD*0.15D);
  }

  // See https://www.mcpk.wiki/wiki/45_Strafe and {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
  // {net.minecraft.block.Block.slipperiness}
  static final float slipperinessDefault=0.6F;
  // {net.minecraft.block.BlockIce.slipperiness}
  static final float slipperinessIce=0.98F;
  // {net.minecraft.block.BlockPackedIce.slipperiness}
  static final float slipperinessPackedIce=0.98F;
  // {net.minecraft.block.BlockSlime.slipperiness}
  static final float slipperinessSlime=0.8F;
  // {net.minecraft.entity.player.EntityPlayer.applyEntityAttributes()}
  static final double baseMovementSpeed=0.10000000149011612D;
  // {net.minecraft.entity.EntityLivingBase.setSprinting(boolean)} {net.minecraft.entity.EntityLivingBase.sprintingSpeedBoostModifier}
  static final double sprintingSpeedBoostModifier=0.30000001192092896D;
  // Set in {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
  // {net.minecraft.entity.ai.attributes.ModifiableAttributeInstance.computeValue()}
  static final float movementSpeed=(float)(baseMovementSpeed*(1.0D+sprintingSpeedBoostModifier));
  // {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
  static final float blockFrictionFactor=slipperinessDefault*0.91F;
  static final float friction_intermediate=0.16277136F/(blockFrictionFactor*blockFrictionFactor*blockFrictionFactor);
  static final float friction=movementSpeed*friction_intermediate;
  /**
   * Simulate 1 tick of movement on a normal flat surface given the yaw while springting
   * See {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
   * See {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()} and {net.minecraft.client.entity.EntityPlayerSP.updateEntityActionState()} for the source of the movement inputs
   * @param movementInputMoveStrafe +1.0F if inputting strafe left, -1.0F if inputting strafe right
   * @param movementInputMoveForward +1.0F if inputting move forward, -1.0F if inputting move backward
   */
  public void step(final float movementInputMoveStrafe,final float movementInputMoveForward) {
    if (Math.abs(this.velX)<0.005D) this.velX=0.0D;
    if (Math.abs(this.velZ)<0.005D) this.velZ=0.0D;
    // Call to {net.minecraft.entity.player.EntityPlayer.jump()} if jumping, omitted
    final float moveStrafing=movementInputMoveStrafe*0.98F;
    final float moveForward=movementInputMoveForward*0.98F;
    // Call to {net.minecraft.entity.player.EntityPlayer.moveEntityWithHeading(float, float)}, immediately delagated to {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
    // Call to {net.minecraft.entity.Entity.moveFlying(float, float, float)}
    float movementDistance=moveStrafing*moveStrafing+moveForward*moveForward;
    if (movementDistance>=1E-4F){
      movementDistance=MathHelper.sqrt_float(movementDistance);
      if (movementDistance<1F) movementDistance=1F;
      movementDistance=friction/movementDistance;
      final float strafe=moveStrafing*movementDistance;
      final float forward=moveForward*movementDistance;
      final float sinYaw=MathHelper.sin(yaw*(float)Math.PI/180F);
      final float cosYaw=MathHelper.cos(yaw*(float)Math.PI/180F);
      velX+=(double)(strafe*cosYaw-forward*sinYaw);
      velZ+=(double)(forward*cosYaw+strafe*sinYaw);
    }
    // Return from moveFlying
    // Call to {net.minecraft.entity.Entity.moveEntity(double, double, double)}
    // Positions are calculated by manipulating the bounding boxes and updated by {net.minecraft.entity.Entity.resetPositionToBB()}
    boundingBox.move(velX,velZ);
    posX=(boundingBox.minX+boundingBox.maxX)/2.0D;
    posZ=(boundingBox.minZ+boundingBox.maxZ)/2.0D;
    // Return from moveEntity
    velX*=blockFrictionFactor;
    velZ*=blockFrictionFactor;
    // Return from moveEntityWithHeading
  }
  public static void main(String[] args) {
    for (int i=-900;i<=900;i+=100){
      for (float j:new float[]{-1F,0F,1F}){
        Player player=new Player(0,0,0,0,0);
        player.moveCamera(i);
        player.step(j,1F);
        System.out.println(String.format("%4d %2d %-22s %-22s %-22s %-22s",i,(int)j,Utility.padSignDouble(player.posX),Utility.padSignDouble(player.posZ),Utility.padSignDouble(player.velX),Utility.padSignDouble(player.velZ)));
      }
    }
    for (float j:new float[]{0F,1F}){
      System.out.println();
      Player player=new Player(0,0,0,0,0);
      player.moveCamera((int)j*300);
      for (int t=0;t<10;t++){
        player.step(j,1F);
        System.out.println(String.format("%2d %2d %-22s %-22s %-22s %-22s",(int)j,t+1,Utility.padSignDouble(player.posX),Utility.padSignDouble(player.posZ),Utility.padSignDouble(player.velX),Utility.padSignDouble(player.velZ)));
      }
    }
  }
}
