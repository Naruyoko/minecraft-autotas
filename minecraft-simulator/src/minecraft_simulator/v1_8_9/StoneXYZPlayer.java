package minecraft_simulator.v1_8_9;

/**
 * Simulates the player movement assuming with following restrictions:
 * - Assumed to be on ground with normal slipperiness
 */
public class StoneXYZPlayer extends AbstractXYZPlayer {
  public int sprintingTicksLeft=0; // {net.minecraft.client.entity.EntityPlayerSP.sprintingTicksLeft}
  public boolean isSprinting=false; // {net.minecraft.entity.Entity.isSprinting()}
  public boolean isCollidedHorizontally=false; // {net.minecraft.entity.Entity.isCollidedHorizontally}
  public boolean isCollidedVertically; // {net.minecraft.entity.Entity.isCollidedVertically}
  public boolean isCollided; // {net.minecraft.entity.Entity.isCollided}
  public boolean onGround=true; // {net.minecraft.entity.Entity.onGround}
  public boolean jumpedLast=false; // Whether or not it jumped during the last tick, blocks jumping if true
  public float jumpMovementFactor=jumpMovementFactorBase; // {net.minecraft.entity.EntityLivingBase.jumpMovementFactor}
  public final SimulationFlagsIn flagsIn=new SimulationFlagsIn();
  public final SimulationFlagsOut flagsOut=new SimulationFlagsOut();
  public StoneXYZPlayer(XYZAxisAlignedBB boundingBox,double posX,double posY,double posZ,double velX,double velY,double velZ,float yaw,boolean checkSneaking,boolean checkStepping) {
    super(boundingBox, posX, posY, posZ, velX, velY, velZ, yaw);
    init(checkSneaking,checkStepping);
  }
  public StoneXYZPlayer(XYZAxisAlignedBB boundingBox,double posX,double posY,double posZ,double velX,double velY,double velZ,float yaw) {
    super(boundingBox, posX, posY, posZ, velX, velY, velZ, yaw);
    init();
  }
  public StoneXYZPlayer(double posX,double posY,double posZ,double velX,double velY,double velZ,float yaw) {
    super(posX, posY, posZ, velX, velY, velZ, yaw);
    init();
  }
  public StoneXYZPlayer() {
    super();
    init();
  }
  private void init(boolean checkSneaking,boolean checkStepping){
    flagsIn.checkSneaking=checkSneaking;
    flagsIn.checkStepping=checkStepping;
  }
  private void init(){
    init(true,true);
  }
  @Override
  public StoneXYZPlayer clone() {
    return new StoneXYZPlayer(boundingBox.clone(), posX, posY, posZ, velX, velY, velZ, yaw, flagsIn.checkSneaking, flagsIn.checkStepping);
  }
  public static void copy(StoneXYZPlayer target,StoneXYZPlayer source){
    AbstractXYZPlayer.copy(target, source);
    target.sprintingTicksLeft=source.sprintingTicksLeft;
    target.isSprinting=source.isSprinting;
    target.isCollidedHorizontally=source.isCollidedHorizontally;
    target.isCollidedVertically=source.isCollidedVertically;
    target.isCollided=source.isCollided;
    target.onGround=source.onGround;
    target.jumpedLast=source.jumpedLast;
    target.jumpMovementFactor=source.jumpMovementFactor;
    target.mouseSensitivity=source.mouseSensitivity;
    target.mouseMult=source.mouseMult;
    target.flagsIn.checkSneaking=source.flagsIn.checkSneaking;
    target.flagsIn.checkSneaking=source.flagsIn.checkSneaking;
  }

  // See https://www.mcpk.wiki/wiki/Mouse_Movement and {net.minecraft.client.renderer.EntityRenderer.updateCameraAndRender(float, long)}
  public static final float defaultMouseSensitivity=0.5F;
  private static final float defaultMouseMult_intermediate=defaultMouseSensitivity*0.6F+0.2F;
  public static final float defaultMouseMult=defaultMouseMult_intermediate*defaultMouseMult_intermediate*defaultMouseMult_intermediate*8.0F;
  protected float mouseSensitivity=defaultMouseSensitivity;
  protected float mouseMult=defaultMouseMult;
  public float getMouseSensitivity() {
    return mouseSensitivity;
  }
  public float setMouseSensitivity(float mouseSensitivity){
    this.mouseSensitivity=mouseSensitivity;
    final float mouseMult_intermediate=mouseSensitivity*0.6F+0.2F;
    return this.mouseMult=mouseMult_intermediate*mouseMult_intermediate*mouseMult_intermediate*8.0F;
  }
  public float getMouseMult() {
    return mouseMult;
  }
  public void moveCamera(int pixels) {
    float yawD=(float)pixels*mouseMult;
    //See {net.minecraft.entity.Entity#setAngles}
    yaw=(float)((double)yaw+(double)yawD*0.15D);
  }

  // See https://www.mcpk.wiki/wiki/45_Strafe and {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
  // {net.minecraft.block.Block.slipperiness}
  public static final float slipperinessDefault=0.6F;
  // {net.minecraft.block.BlockIce.slipperiness}
  public static final float slipperinessIce=0.98F;
  // {net.minecraft.block.BlockPackedIce.slipperiness}
  public static final float slipperinessPackedIce=0.98F;
  // {net.minecraft.block.BlockSlime.slipperiness}
  public static final float slipperinessSlime=0.8F;
  // {net.minecraft.entity.player.EntityPlayer.applyEntityAttributes()}
  public static final double baseMovementSpeedDouble=0.10000000149011612D;
  // {net.minecraft.entity.EntityLivingBase.setSprinting(boolean)} {net.minecraft.entity.EntityLivingBase.sprintingSpeedBoostModifier}
  public static final double sprintingSpeedBoostModifier=0.30000001192092896D;
  // Set in {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
  // {net.minecraft.entity.ai.attributes.ModifiableAttributeInstance.computeValue()}
  public static final double sprintingMovementSpeedDouble=baseMovementSpeedDouble*(1.0D+sprintingSpeedBoostModifier);
  // {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
  private static final float blockFrictionFactor=slipperinessDefault*0.91F;
  private static final double groundFriction=(double)blockFrictionFactor;
  private static final double airFriction=(double)0.91F;
  private static final float friction_intermediate=0.16277136F/(blockFrictionFactor*blockFrictionFactor*blockFrictionFactor);
  public static final float frictionBase=((float)baseMovementSpeedDouble)*friction_intermediate;
  public static final float frictionSprinting=((float)sprintingMovementSpeedDouble)*friction_intermediate;
  // See {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
  public static final float speedInAir=0.02F; // {net.minecraft.entity.player.EntityPlayer.speedInAir}
  public static final double jumpUpwardsMotionDouble=(double)0.42F; // {net.minecraft.entity.EntityLivingBase.getJumpUpwardsMotion()}
  public static final float jumpMovementFactorBase=speedInAir;
  public static final float jumpMovementFactorSprinting=(float)((double)jumpMovementFactorBase+(double)speedInAir*0.3D);
  public static final float STRAFE_LEFT=1.0F;
  public static final float STRAFE_RIGHT=-1.0F;
  public static final float MOVE_FORWARD=1.0F;
  public static final float MOVE_BACKWARD=-1.0F;
  /**
   * Simulate 1 tick of horizontal movement
   * See {net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()}
   * See {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()} and {net.minecraft.client.entity.EntityPlayerSP.updateEntityActionState()} for the source of the movement inputs
   * @param moveEntityHandler
   * @param keyStrafe +1.0F if inputting strafe left, -1.0F if inputting strafe right
   * @param keyForward +1.0F if inputting move forward, -1.0F if inputting move backward
   * @param keyJump
   * @param keySprint
   * @param keySneak
   */
  public void step(final IXYZMoveEntityHandler<? super StoneXYZPlayer> moveEntityHandler,final float keyStrafe,final float keyForward,final boolean keyJump,final boolean keySprint,final boolean keySneak) {
    if (sprintingTicksLeft>0&&--sprintingTicksLeft==0) isSprinting=false;
    // Call to {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()}
    final float movementInputMoveStrafing=keySneak?(float)((double)keyStrafe*0.3D):keyStrafe;
    final float movementInputMoveForward=keySneak?(float)((double)keyForward*0.3D):keyForward;
    // Return from updatePlayerMoveState
    if (!isSprinting&&movementInputMoveForward>=0.8F){
      isSprinting=true;
      sprintingTicksLeft=600;
    }
    if (isSprinting&&(isCollidedHorizontally||movementInputMoveForward<0.8F)){
      isSprinting=false;
      sprintingTicksLeft=0;
    }
    // Call to {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
    // Call to {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
    if (Math.abs(this.velX)<0.005D) this.velX=0.0D;
    if (Math.abs(this.velY)<0.005D) this.velY=0.0D;
    if (Math.abs(this.velZ)<0.005D) this.velZ=0.0D;
    jumpedLast=keyJump&&onGround&&!jumpedLast;
    // Call to {net.minecraft.entity.player.EntityPlayer.jump()} if jumping
    if (jumpedLast){
      velY=jumpUpwardsMotionDouble;
      if (isSprinting){
        final float yawRad=yaw*0.017453292F;
        velX-=(double)(MathHelper.sin(yawRad)*0.2F);
        velZ+=(double)(MathHelper.cos(yawRad)*0.2F);
      }
    }
    // Return from jump
    final float moveStrafing=movementInputMoveStrafing*0.98F;
    final float moveForward=movementInputMoveForward*0.98F;
    // Call to {net.minecraft.entity.player.EntityPlayer.moveEntityWithHeading(float, float)}, immediately delagated to {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
    final float friction=onGround?isSprinting?frictionSprinting:frictionBase:jumpMovementFactor;
    // Call to {net.minecraft.entity.Entity.moveFlying(float, float, float)}
    float movementDistance=moveStrafing*moveStrafing+moveForward*moveForward;
    if (movementDistance>=1E-4F){
      movementDistance=MathHelper.sqrt_float(movementDistance);
      if (movementDistance<1F) movementDistance=1F;
      movementDistance=friction/movementDistance;
      final float strafe=moveStrafing*movementDistance;
      final float forward=moveForward*movementDistance;
      float yawRad = yaw*(float)Math.PI/180F;
      final float sinYaw=MathHelper.sin(yawRad);
      final float cosYaw=MathHelper.cos(yawRad);
      velX+=(double)(strafe*cosYaw-forward*sinYaw);
      velZ+=(double)(forward*cosYaw+strafe*sinYaw);
    }
    // Return from moveFlying
    // Note: friction is recalculated when on ground, assumption uses default sliperiness
    final double XZdampFactor=onGround?groundFriction:airFriction;
    flagsIn.onGround=onGround;
    flagsIn.isSneaking=keySneak;
    moveEntityHandler.moveEntity(this, velX, velY, velZ, flagsIn, flagsOut); // {net.minecraft.entity.Entity.moveEntity(double, double, double)}
    isCollidedHorizontally=flagsOut.isCollidedHorizontally;
    isCollidedVertically=flagsOut.isCollidedVertically;
    isCollided=flagsOut.isCollided;
    onGround=flagsOut.onGround;
    velY-=0.08D;
    velY*=0.9800000190734863D;
    velX*=XZdampFactor;
    velZ*=XZdampFactor;
    // Return from moveEntityWithHeading
    // Return from (EntityLivingBase) onLivingUpdate
    jumpMovementFactor=isSprinting?jumpMovementFactorSprinting:jumpMovementFactorBase;
    // Return from (EntityPlayer) onLivingUpdate
  }
  public static void main(String[] args) {
    // See physicsEmulationTest for the setup and inputs
    StoneXYZPlayer player=new StoneXYZPlayer(0,80,0,0,-0.0784000015258789,0,0);
    IXYZMoveEntityHandler<? super StoneXYZPlayer> moveEntityHandler=new XYZMoveEntityHandlerFromSimpleWorld(new AbstractXYZBlockGrid(){
      @Override
      public Block getBlockAt(int x, int y, int z) {
        if (x==-1||x==0){
          if (y==79&&(z==-1||z==0||z==3||z==4||z==16||z==17||z==20)) return Blocks.fullBlock;
          if (y==83&&z==16||y==82&&z==20) return Blocks.fullBlock;
        }
        if (x==-1){
          if (y==80&&(z==7||z==8||z==13||z==14||z==15)) return Blocks.fullBlock;
          if ((y==81||y==82)&&z==10) return Blocks.fullBlock;
        }
        if (x==0){
          if (y==80&&(z==24||z==25||z==26)) return Blocks.fullBlock;
          if (y==81&&z==25) return Blocks.slabBottom;
        }
        if (z==26){
          if ((x==0||x==1||x==2)&&y==81) return Blocks.fullBlock;
          if (x==1&&y==82) return Blocks.slabBottom;
          if (x==2&&y==82) return Blocks.fullBlock;
        }
        if ((x==7||x==8)&&y==81&&(z==25||z==26)) return Blocks.fullBlock;
        return null;
      }
    });
    float keyStrafe=0.0F;
    float keyForward=0.0F;
    boolean keyJump=false;
    boolean keySprint=false;
    boolean keySneak=false;
    for (int t=0;t<150;t++){
      switch (t) {
        case 1:
          keyForward=MOVE_FORWARD;
          keySprint=true;
          break;
        case 4:
          keyJump=true;
          break;
        case 5:
          keyJump=false;
          break;
        case 16:
          keyJump=true;
          break;
        case 17:
          keyJump=false;
          break;
        case 24:
          player.moveCamera(-400);
          keyStrafe=STRAFE_RIGHT;
          break;
        case 29:
          player.moveCamera(100);
          break;
        case 32:
          player.moveCamera(300);
          keyStrafe=0.0F;
          keyJump=true;
          break;
        case 33:
          player.moveCamera(-280);
          keyStrafe=STRAFE_RIGHT;
          keyJump=false;
          break;
        case 44:
          player.moveCamera(280);
          keyStrafe=0.0F;
          keyJump=true;
          break;
        case 45:
          keyJump=false;
          keySprint=false;
          break;
        case 55:
          keySneak=true;
          break;
        case 59:
          keyStrafe=STRAFE_LEFT;
          break;
        case 79:
          keyForward=0.0F;
          keyStrafe=0.0F;
          break;
        case 81:
          keySneak=false;
          break;
        case 87:
          keyForward=MOVE_FORWARD;
          break;
        case 95:
          keyJump=true;
          break;
        case 96:
          keyJump=false;
          break;
        case 98:
          keySprint=true;
          break;
        case 108:
          keyJump=true;
          break;
        case 109:
          keyJump=false;
          break;
        case 111:
          keyJump=true;
          break;
        case 112:
          keyJump=false;
          break;
        case 122:
          player.moveCamera(-600);
          break;
        case 124:
          keyJump=true;
          break;
        case 125:
          keyJump=false;
          break;
        case 126:
          keyJump=true;
          break;
        case 127:
          keyJump=false;
          break;
        case 128:
          keyJump=true;
          break;
        case 129:
          keyJump=false;
          break;
        case 141:
          keyForward=0.0F;
          keySprint=false;
          break;
        default:
          break;
      }
      player.step(moveEntityHandler, keyStrafe, keyForward, keyJump, keySprint, keySneak);
      System.out.println(String.format("%3d %-10s %-22s %-22s %-22s %-22s %-22s %-22s %s",
        t,
        Utility.padSignFloat(player.yaw),
        Utility.padSignDouble(player.posX),
        Utility.padSignDouble(player.posY),
        Utility.padSignDouble(player.posZ),
        Utility.padSignDouble(player.velX),
        Utility.padSignDouble(player.velY),
        Utility.padSignDouble(player.velZ),
        (player.isSprinting?"S":" ")+
        (player.isCollidedHorizontally?"H":" ")+
        (player.isCollidedVertically?"V":" ")+
        (player.isCollided?"C":" ")+
        (player.onGround?"G":" ")));
    }
  }
}
