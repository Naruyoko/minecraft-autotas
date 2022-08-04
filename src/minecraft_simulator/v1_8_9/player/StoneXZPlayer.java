package minecraft_simulator.v1_8_9.player;

import minecraft_simulator.v1_8_9.world.IXZMoveEntityHandler;
import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.util.MathHelper;
import minecraft_simulator.v1_8_9.world.SimulationFlagsIn;
import minecraft_simulator.v1_8_9.world.SimulationFlagsOut;

/**
 * Simulates the player movement assuming with following restrictions: - Assumed
 * to be on ground with normal slipperiness and not in liquids or ladder - No
 * falling (XZ-only) - No status effects
 */
public class StoneXZPlayer extends AbstractXZPlayer {
  public int sprintingTicksLeft = 0; // {net.minecraft.client.entity.EntityPlayerSP.sprintingTicksLeft}
  public boolean isSprinting = false; // {net.minecraft.entity.Entity.isSprinting()}
  public boolean isCollidedHorizontally = false; // {net.minecraft.entity.Entity.isCollidedHorizontally}
  public boolean isCollidedVertically; // {net.minecraft.entity.Entity.isCollidedVertically}
  public boolean isCollided; // {net.minecraft.entity.Entity.isCollided}
  public boolean onGround = true; // {net.minecraft.entity.Entity.onGround}
  public final SimulationFlagsIn flagsIn = new SimulationFlagsIn();
  public final SimulationFlagsOut flagsOut = new SimulationFlagsOut();

  public StoneXZPlayer(XZAxisAlignedBB boundingBox, double posX, double posZ, double velX, double velZ, float yaw,
      boolean checkSneaking, boolean checkStepping) {
    super(boundingBox, posX, posZ, velX, velZ, yaw);
    init(checkSneaking, checkStepping);
  }

  public StoneXZPlayer(XZAxisAlignedBB boundingBox, double posX, double posZ, double velX, double velZ, float yaw) {
    super(boundingBox, posX, posZ, velX, velZ, yaw);
    init();
  }

  public StoneXZPlayer(double posX, double posZ, double velX, double velZ, float yaw) {
    super(posX, posZ, velX, velZ, yaw);
    init();
  }

  public StoneXZPlayer() {
    super();
    init();
  }

  private void init(boolean checkSneaking, boolean checkStepping) {
    flagsIn.checkSneaking = checkSneaking;
    flagsIn.checkStepping = checkStepping;
  }

  private void init() { init(true, true); }

  @Override
  public StoneXZPlayer clone() {
    return new StoneXZPlayer(boundingBox.clone(), posX, posZ, velX, velZ, yaw, flagsIn.checkSneaking,
        flagsIn.checkStepping);
  }

  public static void copy(StoneXZPlayer target, StoneXZPlayer source) {
    AbstractXZPlayer.copy(target, source);
    target.sprintingTicksLeft = source.sprintingTicksLeft;
    target.isSprinting = source.isSprinting;
    target.isCollidedHorizontally = source.isCollidedHorizontally;
    target.isCollidedVertically = source.isCollidedVertically;
    target.isCollided = source.isCollided;
    target.onGround = source.onGround;
    target.mouseSensitivity = source.mouseSensitivity;
    target.mouseMult = source.mouseMult;
    target.flagsIn.checkSneaking = source.flagsIn.checkSneaking;
    target.flagsIn.checkSneaking = source.flagsIn.checkSneaking;
  }

  // See https://www.mcpk.wiki/wiki/Mouse_Movement and {net.minecraft.client.renderer.EntityRenderer.updateCameraAndRender(float, long)}
  public static final float defaultMouseSensitivity = 0.5F;
  private static final float defaultMouseMult_intermediate = defaultMouseSensitivity * 0.6F + 0.2F;
  public static final float defaultMouseMult = defaultMouseMult_intermediate * defaultMouseMult_intermediate
      * defaultMouseMult_intermediate * 8.0F;
  protected float mouseSensitivity = defaultMouseSensitivity;
  protected float mouseMult = defaultMouseMult;

  public float getMouseSensitivity() { return mouseSensitivity; }

  public float setMouseSensitivity(float mouseSensitivity) {
    this.mouseSensitivity = mouseSensitivity;
    final float mouseMult_intermediate = mouseSensitivity * 0.6F + 0.2F;
    return this.mouseMult = mouseMult_intermediate * mouseMult_intermediate * mouseMult_intermediate * 8.0F;
  }

  public float getMouseMult() { return mouseMult; }

  public void moveCamera(int pixels) {
    float yawD = (float)pixels * mouseMult;
    //See {net.minecraft.entity.Entity#setAngles}
    yaw = (float)((double)yaw + (double)yawD * 0.15D);
  }

  // See https://www.mcpk.wiki/wiki/45_Strafe and {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
  // {net.minecraft.block.Block.slipperiness}
  public static final float slipperinessDefault = 0.6F;
  // {net.minecraft.block.BlockIce.slipperiness}
  public static final float slipperinessIce = 0.98F;
  // {net.minecraft.block.BlockPackedIce.slipperiness}
  public static final float slipperinessPackedIce = 0.98F;
  // {net.minecraft.block.BlockSlime.slipperiness}
  public static final float slipperinessSlime = 0.8F;
  // {net.minecraft.entity.player.EntityPlayer.applyEntityAttributes()}
  public static final double baseMovementSpeedDouble = 0.10000000149011612D;
  // {net.minecraft.entity.EntityLivingBase.setSprinting(boolean)} {net.minecraft.entity.EntityLivingBase.sprintingSpeedBoostModifier}
  public static final double sprintingSpeedBoostModifier = 0.30000001192092896D;
  // Set in {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
  // {net.minecraft.entity.ai.attributes.ModifiableAttributeInstance.computeValue()}
  public static final double sprintingMovementSpeedDouble = baseMovementSpeedDouble
      * (1.0D + sprintingSpeedBoostModifier);
  // {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
  private static final float blockFrictionFactor = slipperinessDefault * 0.91F;
  private static final double groundFriction = (double)blockFrictionFactor;
  private static final float friction_intermediate = 0.16277136F
      / (blockFrictionFactor * blockFrictionFactor * blockFrictionFactor);
  public static final float frictionBase = ((float)baseMovementSpeedDouble) * friction_intermediate;
  public static final float frictionSprinting = ((float)sprintingMovementSpeedDouble) * friction_intermediate;
  public static final float STRAFE_LEFT = 1.0F;
  public static final float STRAFE_RIGHT = -1.0F;
  public static final float MOVE_FORWARD = 1.0F;
  public static final float MOVE_BACKWARD = -1.0F;

  /**
   * Simulate 1 tick of horizontal movement See
   * {net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()} See
   * {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()} and
   * {net.minecraft.client.entity.EntityPlayerSP.updateEntityActionState()} for
   * the source of the movement inputs
   * 
   * @param moveEntityHandler
   * @param keyStrafe         +1.0F if inputting strafe left, -1.0F if inputting
   *                          strafe right
   * @param keyForward        +1.0F if inputting move forward, -1.0F if inputting
   *                          move backward
   * @param keySprint
   * @param keySneak
   */
  public void step(final IXZMoveEntityHandler<? super StoneXZPlayer> moveEntityHandler, final float keyStrafe,
      final float keyForward, final boolean keySprint, final boolean keySneak) {
    if (sprintingTicksLeft > 0 && --sprintingTicksLeft == 0)
      isSprinting = false;
    // Call to {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()}
    final float movementInputMoveStrafing = keySneak ? (float)((double)keyStrafe * 0.3D) : keyStrafe;
    final float movementInputMoveForward = keySneak ? (float)((double)keyForward * 0.3D) : keyForward;
    // Return from updatePlayerMoveState
    if (!isSprinting && keySprint && movementInputMoveForward >= 0.8F) {
      isSprinting = true;
      sprintingTicksLeft = 600;
    }
    if (isSprinting && (isCollidedHorizontally || movementInputMoveForward < 0.8F)) {
      isSprinting = false;
      sprintingTicksLeft = 0;
    }
    // Call to {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
    // Call to {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
    if (Math.abs(this.velX) < 0.005D)
      this.velX = 0.0D;
    if (Math.abs(this.velZ) < 0.005D)
      this.velZ = 0.0D;
    // Call to {net.minecraft.entity.player.EntityPlayer.jump()} if jumping, omitted
    final float moveStrafing = movementInputMoveStrafing * 0.98F;
    final float moveForward = movementInputMoveForward * 0.98F;
    // Call to {net.minecraft.entity.player.EntityPlayer.moveEntityWithHeading(float, float)}, immediately delagated to {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
    final float friction = isSprinting ? frictionSprinting : frictionBase;
    // Call to {net.minecraft.entity.Entity.moveFlying(float, float, float)}
    float movementDistance = moveStrafing * moveStrafing + moveForward * moveForward;
    if (movementDistance >= 1E-4F) {
      movementDistance = MathHelper.sqrt_float(movementDistance);
      if (movementDistance < 1F)
        movementDistance = 1F;
      movementDistance = friction / movementDistance;
      final float strafe = moveStrafing * movementDistance;
      final float forward = moveForward * movementDistance;
      float yawRad = yaw * (float)Math.PI / 180F;
      final float sinYaw = MathHelper.sin(yawRad);
      final float cosYaw = MathHelper.cos(yawRad);
      velX += (double)(strafe * cosYaw - forward * sinYaw);
      velZ += (double)(forward * cosYaw + strafe * sinYaw);
    }
    // Return from moveFlying
    flagsIn.isSneaking = keySneak;
    moveEntityHandler.moveEntity(this, velX, velZ, flagsIn, flagsOut); // {net.minecraft.entity.Entity.moveEntity(double, double, double)}
    isCollidedHorizontally = flagsOut.isCollidedHorizontally;
    isCollidedVertically = flagsOut.isCollidedVertically;
    isCollided = flagsOut.isCollided;
    onGround = flagsOut.onGround;
    velX *= groundFriction;
    velZ *= groundFriction;
    // Return from moveEntityWithHeading
    // Return from (EntityLivingBase) onLivingUpdate
    // Return from (EntityPlayer) onLivingUpdate
  }
}
