package minecraft_simulator.v1_8_9.player;

import minecraft_simulator.v1_8_9.collision.XYZAxisAlignedBB;
import minecraft_simulator.v1_8_9.util.MathHelper;
import minecraft_simulator.v1_8_9.world.IXYZMoveEntityHandler;
import minecraft_simulator.v1_8_9.world.SimulationFlagsIn;
import minecraft_simulator.v1_8_9.world.SimulationFlagsOut;

/**
 * Simulates the player movement assuming with following restrictions: - Assumed
 * to be on ground with normal slipperiness and not in liquids or ladder
 */
public class StonePotionXYZPlayer extends AbstractXYZPlayer {
  public int sprintingTicksLeft = 0; // {net.minecraft.client.entity.EntityPlayerSP.sprintingTicksLeft}
  public boolean isSprinting = false; // {net.minecraft.entity.Entity.isSprinting()}
  public boolean hasSpeedEffect = false;
  public int speedEffectAmplifier = 0;
  public boolean hasSlownessEffect = false;
  public int slownessEffectAmplifier = 0;
  public boolean hasJumpBoostEffect = false;
  public int jumpBoostEffectAmplifier = 0;
  public double cachedMovementSpeedDouble = baseMovementSpeedDouble;
  public float cachedMovementSpeedFloat = (float)cachedMovementSpeedDouble;
  public float cachedFriction = cachedMovementSpeedFloat * friction_intermediate;
  public boolean isCollidedHorizontally = false; // {net.minecraft.entity.Entity.isCollidedHorizontally}
  public boolean isCollidedVertically; // {net.minecraft.entity.Entity.isCollidedVertically}
  public boolean isCollided; // {net.minecraft.entity.Entity.isCollided}
  public boolean onGround = true; // {net.minecraft.entity.Entity.onGround}
  public boolean jumpedLast = false; // Whether or not it jumped during the last tick, blocks jumping if true
  public float jumpMovementFactor = jumpMovementFactorBase; // {net.minecraft.entity.EntityLivingBase.jumpMovementFactor}
  public final SimulationFlagsIn flagsIn = new SimulationFlagsIn();
  public final SimulationFlagsOut flagsOut = new SimulationFlagsOut();

  public StonePotionXYZPlayer(XYZAxisAlignedBB boundingBox, double posX, double posY, double posZ, double velX,
      double velY, double velZ, float yaw, boolean checkSneaking, boolean checkStepping) {
    super(boundingBox, posX, posY, posZ, velX, velY, velZ, yaw);
    init(checkSneaking, checkStepping);
  }

  public StonePotionXYZPlayer(XYZAxisAlignedBB boundingBox, double posX, double posY, double posZ, double velX,
      double velY, double velZ, float yaw) {
    super(boundingBox, posX, posY, posZ, velX, velY, velZ, yaw);
    init();
  }

  public StonePotionXYZPlayer(double posX, double posY, double posZ, double velX, double velY, double velZ, float yaw) {
    super(posX, posY, posZ, velX, velY, velZ, yaw);
    init();
  }

  public StonePotionXYZPlayer() {
    super();
    init();
  }

  private void init(boolean checkSneaking, boolean checkStepping) {
    flagsIn.checkSneaking = checkSneaking;
    flagsIn.checkStepping = checkStepping;
  }

  private void init() { init(true, true); }

  @Override
  public StonePotionXYZPlayer clone() {
    StonePotionXYZPlayer other = new StonePotionXYZPlayer(boundingBox.clone(), posX, posY, posZ, velX, velY, velZ, yaw,
        flagsIn.checkSneaking, flagsIn.checkStepping);
    copy(other, this);
    return other;
  }

  public static void copy(StonePotionXYZPlayer target, StonePotionXYZPlayer source) {
    AbstractXYZPlayer.copy(target, source);
    target.sprintingTicksLeft = source.sprintingTicksLeft;
    target.isSprinting = source.isSprinting;
    target.hasSpeedEffect = source.hasSpeedEffect;
    target.speedEffectAmplifier = source.speedEffectAmplifier;
    target.hasSlownessEffect = source.hasSlownessEffect;
    target.slownessEffectAmplifier = source.slownessEffectAmplifier;
    target.hasJumpBoostEffect = source.hasJumpBoostEffect;
    target.jumpBoostEffectAmplifier = source.jumpBoostEffectAmplifier;
    target.cachedMovementSpeedDouble = source.cachedMovementSpeedDouble;
    target.cachedMovementSpeedFloat = source.cachedMovementSpeedFloat;
    target.cachedFriction = source.cachedFriction;
    target.isCollidedHorizontally = source.isCollidedHorizontally;
    target.isCollidedVertically = source.isCollidedVertically;
    target.isCollided = source.isCollided;
    target.onGround = source.onGround;
    target.jumpedLast = source.jumpedLast;
    target.jumpMovementFactor = source.jumpMovementFactor;
    target.mouseSensitivity = source.mouseSensitivity;
    target.mouseMult = source.mouseMult;
    SimulationFlagsIn.copy(target.flagsIn, source.flagsIn);
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

  public void setSprinting(boolean isSprinting) {
    this.isSprinting = isSprinting;
    sprintingTicksLeft = isSprinting ? 600 : 0;
    recalculateMovementSpeed();
  }

  public void setSpeedEffect(boolean grant, int amplifier) {
    hasSpeedEffect = grant;
    speedEffectAmplifier = amplifier;
    recalculateMovementSpeed();
  }

  public void setSlownessEffect(boolean grant, int amplifier) {
    hasSlownessEffect = grant;
    slownessEffectAmplifier = amplifier;
    recalculateMovementSpeed();
  }

  public void setJumpBoostEffect(boolean grant, int amplifier) {
    hasJumpBoostEffect = grant;
    jumpBoostEffectAmplifier = amplifier;
    recalculateMovementSpeed();
  }

  // See https://www.mcpk.wiki/wiki/45_Strafe and {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
  // {net.minecraft.entity.player.EntityPlayer.applyEntityAttributes()}
  // {net.minecraft.entity.EntityLivingBase.setSprinting(boolean)} {net.minecraft.entity.EntityLivingBase.sprintingSpeedBoostModifier}
  public static final double sprintingSpeedBoostModifier = 0.30000001192092896D;
  // {net.minecraft.potion.Potion.moveSpeed}
  public static final double speedEffectModifier = 0.20000000298023224D;
  // {net.minecraft.potion.PospeedInAir =moveSlowdown}
  public static final double slownessEffectModifier = -0.15000000596046448D;
  public static final double baseMovementSpeedDouble = 0.10000000149011612D;

  /**
   * See {net.minecraft.entity.player.EntityPlayer.getAIMoveSpeed()} and
   * {net.minecraft.entity.ai.attributes.ModifiableAttributeInstance.computeValue()}
   * Note: This is ordered as would be using OpenJDK. Because of how HashMap is
   * iterated, we always get the order of slowness, sprinting, then speed.
   */
  public void recalculateMovementSpeed() {
    cachedMovementSpeedDouble = baseMovementSpeedDouble;
    if (hasSlownessEffect)
      cachedMovementSpeedDouble *= 1.0D + slownessEffectModifier * (double)(slownessEffectAmplifier + 1);
    if (isSprinting)
      cachedMovementSpeedDouble *= 1.0D + sprintingSpeedBoostModifier;
    if (hasSpeedEffect)
      cachedMovementSpeedDouble *= 1.0D + speedEffectModifier * (double)(speedEffectAmplifier + 1);
    cachedMovementSpeedFloat = (float)cachedMovementSpeedDouble;
    cachedFriction = cachedMovementSpeedFloat * friction_intermediate;
  }

  // {net.minecraft.block.Block.slipperiness}
  public static final float slipperinessDefault = 0.6F;
  // {net.minecraft.block.BlockIce.slipperiness}
  public static final float slipperinessIce = 0.98F;
  // {net.minecraft.block.BlockPackedIce.slipperiness}
  public static final float slipperinessPackedIce = 0.98F;
  // {net.minecraft.block.BlockSlime.slipperiness}
  public static final float slipperinessSlime = 0.8F;
  // {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
  private static final float blockFrictionFactor = slipperinessDefault * 0.91F;
  private static final double groundFriction = (double)blockFrictionFactor;
  private static final double airFriction = (double)0.91F;
  private static final float friction_intermediate = 0.16277136F
      / (blockFrictionFactor * blockFrictionFactor * blockFrictionFactor);
  // See {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
  public static final float speedInAir = 0.02F; // {net.minecraft.entity.player.EntityPlayer.speedInAir}
  public static final double jumpUpwardsMotionDouble = (double)0.42F; // {net.minecraft.entity.EntityLivingBase.getJumpUpwardsMotion()}
  public static final float jumpMovementFactorBase = speedInAir;
  public static final float jumpMovementFactorSprinting = (float)((double)jumpMovementFactorBase
      + (double)speedInAir * 0.3D);
  public static final float STRAFE_LEFT = 1.0F;
  public static final float STRAFE_RIGHT = -1.0F;
  public static final float MOVE_FORWARD = 1.0F;
  public static final float MOVE_BACKWARD = -1.0F;

  /**
   * Simulate 1 tick of horizontal movement. See
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
   * @param keyJump
   * @param keySprint
   * @param keySneak
   */
  public void step(final IXYZMoveEntityHandler<? super StonePotionXYZPlayer> moveEntityHandler, final float keyStrafe,
      final float keyForward, final boolean keyJump, final boolean keySprint, final boolean keySneak) {
    if (sprintingTicksLeft > 0 && --sprintingTicksLeft == 0)
      setSprinting(false);
    // Call to {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()}
    final float movementInputMoveStrafing = keySneak ? (float)((double)keyStrafe * 0.3D) : keyStrafe;
    final float movementInputMoveForward = keySneak ? (float)((double)keyForward * 0.3D) : keyForward;
    // Return from updatePlayerMoveState
    if (!isSprinting && keySprint && movementInputMoveForward >= 0.8F)
      setSprinting(true);
    if (isSprinting && (isCollidedHorizontally || movementInputMoveForward < 0.8F))
      setSprinting(false);
    // Call to {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
    // Call to {net.minecraft.entity.EntityLivingBase.onLivingUpdate()}
    if (Math.abs(this.velX) < 0.005D)
      this.velX = 0.0D;
    if (Math.abs(this.velY) < 0.005D)
      this.velY = 0.0D;
    if (Math.abs(this.velZ) < 0.005D)
      this.velZ = 0.0D;
    jumpedLast = keyJump && onGround && !jumpedLast;
    // Call to {net.minecraft.entity.player.EntityPlayer.jump()} if jumping
    if (jumpedLast) {
      velY = jumpUpwardsMotionDouble;
      if (hasJumpBoostEffect)
        velY += (double)((float)(jumpBoostEffectAmplifier + 1) * 0.1F);
      if (isSprinting) {
        final float yawRad = yaw * 0.017453292F;
        velX -= (double)(MathHelper.sin(yawRad) * 0.2F);
        velZ += (double)(MathHelper.cos(yawRad) * 0.2F);
      }
    }
    // Return from jump
    final float moveStrafing = movementInputMoveStrafing * 0.98F;
    final float moveForward = movementInputMoveForward * 0.98F;
    // Call to {net.minecraft.entity.player.EntityPlayer.moveEntityWithHeading(float, float)}, immediately delagated to {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
    final float friction = onGround ? cachedFriction : jumpMovementFactor;
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
    // Note: friction is recalculated when on ground, assumption uses default sliperiness
    final double XZdampFactor = onGround ? groundFriction : airFriction;
    flagsIn.onGround = onGround;
    flagsIn.isSneaking = keySneak;
    moveEntityHandler.moveEntity(this, velX, velY, velZ, flagsIn, flagsOut); // {net.minecraft.entity.Entity.moveEntity(double, double, double)}
    isCollidedHorizontally = flagsOut.isCollidedHorizontally;
    isCollidedVertically = flagsOut.isCollidedVertically;
    isCollided = flagsOut.isCollided;
    onGround = flagsOut.onGround;
    velY -= 0.08D;
    velY *= 0.9800000190734863D;
    velX *= XZdampFactor;
    velZ *= XZdampFactor;
    // Return from moveEntityWithHeading
    // Return from (EntityLivingBase) onLivingUpdate
    jumpMovementFactor = isSprinting ? jumpMovementFactorSprinting : jumpMovementFactorBase;
    // setAIMoveSpeed
    // Return from (EntityPlayer) onLivingUpdate
  }
}
