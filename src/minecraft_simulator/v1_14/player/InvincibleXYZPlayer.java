package minecraft_simulator.v1_14.player;

import minecraft_simulator.v1_14.block.Block;
import minecraft_simulator.v1_14.block.BlockLiquid;
import minecraft_simulator.v1_14.collision.XYZBoundingBox;
import minecraft_simulator.v1_14.util.MathHelper;
import minecraft_simulator.v1_14.world.AbstractXYZBlockGrid;
import minecraft_simulator.v1_14.world.SimulationFlagsIn;
import minecraft_simulator.v1_14.world.SimulationFlagsOut;
import minecraft_simulator.v1_14.world.XYZMoveEntityHandlerFromBlockGrid;

/**
 * Simulates the player movement assuming it does not take any damage
 */
//TODO: update
public class InvincibleXYZPlayer extends AbstractXYZPlayer implements IPushedByWater {
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
  public boolean inWater = false;
  public int depthStriderModifier = 0;
  public float depthStriderModifierEffective = 0.0F;
  public float depthStriderModifierHalf = 0.0F;
  public boolean isCollidedHorizontally = false; // {net.minecraft.entity.Entity.isCollidedHorizontally}
  public boolean isCollidedVertically; // {net.minecraft.entity.Entity.isCollidedVertically}
  public boolean isCollided; // {net.minecraft.entity.Entity.isCollided}
  public boolean onGround = true; // {net.minecraft.entity.Entity.onGround}
  public int jumpTicks = 0; // {net.minecraft.entity.EntityLivingBase.jumpTicks}
  public float jumpMovementFactor = jumpMovementFactorBase; // {net.minecraft.entity.EntityLivingBase.jumpMovementFactor}
  public final SimulationFlagsIn flagsIn = new SimulationFlagsIn();
  public final SimulationFlagsOut flagsOut = new SimulationFlagsOut();

  public InvincibleXYZPlayer(XYZBoundingBox boundingBox, double posX, double posY, double posZ, double velX,
      double velY, double velZ, float yaw, boolean checkSneaking, boolean checkStepping, boolean checkWater) {
    super(boundingBox, posX, posY, posZ, velX, velY, velZ, yaw);
    init(checkSneaking, checkStepping, checkWater);
  }

  public InvincibleXYZPlayer(XYZBoundingBox boundingBox, double posX, double posY, double posZ, double velX,
      double velY, double velZ, float yaw) {
    super(boundingBox, posX, posY, posZ, velX, velY, velZ, yaw);
    init();
  }

  public InvincibleXYZPlayer(double posX, double posY, double posZ, double velX, double velY, double velZ, float yaw) {
    super(posX, posY, posZ, velX, velY, velZ, yaw);
    init();
  }

  public InvincibleXYZPlayer() {
    super();
    init();
  }

  private void init(boolean checkSneaking, boolean checkStepping, boolean checkWater) {
    flagsIn.checkSneaking = checkSneaking;
    flagsIn.checkStepping = checkStepping;
    flagsIn.checkWater = checkWater;
  }

  private void init() { init(true, true, true); }

  @Override
  public InvincibleXYZPlayer clone() {
    InvincibleXYZPlayer other = new InvincibleXYZPlayer(boundingBox.clone(), posX, posY, posZ, velX, velY, velZ, yaw,
        flagsIn.checkSneaking, flagsIn.checkStepping, flagsIn.checkWater);
    copy(other, this);
    return other;
  }

  public static void copy(InvincibleXYZPlayer target, InvincibleXYZPlayer source) {
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
    target.inWater = source.inWater;
    target.depthStriderModifier = source.depthStriderModifier;
    target.depthStriderModifierEffective = source.depthStriderModifierEffective;
    target.depthStriderModifierHalf = source.depthStriderModifierHalf;
    target.isCollidedHorizontally = source.isCollidedHorizontally;
    target.isCollidedVertically = source.isCollidedVertically;
    target.isCollided = source.isCollided;
    target.onGround = source.onGround;
    target.jumpTicks = source.jumpTicks;
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

  public void setDepthStrider(int depthStriderModifier) {
    this.depthStriderModifier = depthStriderModifier;
    depthStriderModifierEffective = (float)depthStriderModifier;
    if (depthStriderModifierEffective > 3.0F)
      depthStriderModifierEffective = 3.0F;
    depthStriderModifierHalf = depthStriderModifierEffective * 0.5F;
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
    // cachedFriction = cachedMovementSpeedFloat * friction_intermediate;
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
  private static final double airFriction = (double)0.91F;
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
  public static final double waterAcceleration = 0.014D;

  /**
   * See {net.minecraft.entity.Entity.handleWaterMovement()} and
   * {net.minecraft.world.World.handleMaterialAcceleration(BoundingBox, Material,
   * Entity)}
   */
  @Override
  public void handleWaterMovement(AbstractXYZBlockGrid blockGrid) {
    inWater = false;
    final XYZBoundingBox bb = boundingBox.expandAndContract(0.0D, -0.4000000059604645D, 0.0D, 0.001D, 0.001D, 0.001D);
    final int minX = MathHelper.floor(bb.minX);
    final int maxX = MathHelper.floor(bb.maxX + 1.0D);
    final int minY = MathHelper.floor(bb.minY);
    final int maxY = MathHelper.floor(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor(bb.minZ);
    final int maxZ = MathHelper.floor(bb.maxZ + 1.0D);
    double pushX = 0.0D;
    double pushY = 0.0D;
    double pushZ = 0.0D;
    for (int x = minX; x < maxX; x++) {
      for (int y = minY; y < maxY; y++) {
        for (int z = minZ; z < maxZ; z++) {
          Block block = blockGrid.getBlockAt(x, y, z);
          if (block instanceof BlockLiquid) {
            BlockLiquid liquidBlock = (BlockLiquid)block;
            if (liquidBlock.isWater && (double)maxY >= (double)((float)(y + 1) - liquidBlock.liquidHeightPercent)) {
              inWater = true;
              pushX = pushX + liquidBlock.flowX;
              pushY = pushY + liquidBlock.flowY;
              pushZ = pushZ + liquidBlock.flowZ;
            }
          }
        }
      }
    }
    double pushMag = (double)MathHelper.sqrt(pushX * pushX + pushY * pushY + pushZ * pushZ);
    if (pushMag > 0.0D) {
      if (pushMag < 1.0E-4D) {
        pushX = pushY = pushZ = 0.0D;
      } else {
        pushX = pushX / pushMag;
        pushY = pushY / pushMag;
        pushZ = pushZ / pushMag;
      }
      velX += pushX * waterAcceleration;
      velY += pushY * waterAcceleration;
      velZ += pushZ * waterAcceleration;
    }
  }

  /**
   * See {net.minecraft.entity.Entity.isInLava()}
   */
  private boolean isInLava(AbstractXYZBlockGrid blockGrid) {
    final XYZBoundingBox bb = boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D);
    final int minX = MathHelper.floor(bb.minX);
    final int maxX = MathHelper.floor(bb.maxX + 1.0D);
    final int minY = MathHelper.floor(bb.minY);
    final int maxY = MathHelper.floor(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor(bb.minZ);
    final int maxZ = MathHelper.floor(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int y = minY; y < maxY; y++) {
        for (int z = minZ; z < maxZ; z++) {
          Block block = blockGrid.getBlockAt(x, y, z);
          if (block instanceof BlockLiquid && ((BlockLiquid)block).isLava)
            return true;
        }
      }
    }
    return false;
  }

  /**
   * See {net.minecraft.entity.Entity.isOffsetPositionInLiquid(double, double,
   * double)}
   */
  private boolean isOffsetPositionInLiquid(AbstractXYZBlockGrid blockGrid, double offsetX, double offsetY,
      double offsetZ) {
    final XYZBoundingBox bb = boundingBox.offset(offsetX, offsetY, offsetZ);
    if (!blockGrid.hasAnyCollidingBoundingBoxes(bb)) {
      final int minX = MathHelper.floor(bb.minX);
      final int maxX = MathHelper.floor(bb.maxX + 1.0D);
      final int minY = MathHelper.floor(bb.minY);
      final int maxY = MathHelper.floor(bb.maxY + 1.0D);
      final int minZ = MathHelper.floor(bb.minZ);
      final int maxZ = MathHelper.floor(bb.maxZ + 1.0D);
      for (int x = minX; x < maxX; x++) {
        for (int y = minY; y < maxY; y++) {
          for (int z = minZ; z < maxZ; z++) {
            if (blockGrid.getBlockAt(x, y, z) instanceof BlockLiquid)
              return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * See {net.minecraft.entity.EntityLivingBase.isOnLadder()}
   */
  private boolean isOnLadder(AbstractXYZBlockGrid blockGrid) {
    final XYZBoundingBox bb = boundingBox;
    final int minX = MathHelper.floor(bb.minX);
    final int maxX = MathHelper.floor(bb.maxX + 1.0D);
    final int minY = MathHelper.floor(bb.minY);
    final int maxY = MathHelper.floor(bb.maxY + 1.0D);
    final int minZ = MathHelper.floor(bb.minZ);
    final int maxZ = MathHelper.floor(bb.maxZ + 1.0D);
    for (int x = minX; x < maxX; x++) {
      for (int y = minY; y < maxY; y++) {
        for (int z = minZ; z < maxZ; z++) {
          if (blockGrid.getBlockAt(x, y, z).isLadder)
            return true;
        }
      }
    }
    return false;
  }

  /**
   * Calls {net.minecraft.entity.Entity.moveEntity(double, double, double)} while
   * setting and retrieving some flags
   * 
   * @param moveEntityHandler
   * @param keySneak
   */
  private void moveEntity(final XYZMoveEntityHandlerFromBlockGrid moveEntityHandler, final boolean keySneak) {
    flagsIn.onGround = onGround;
    flagsIn.isSneaking = keySneak;
    flagsIn.inWater = inWater;
    moveEntityHandler.moveEntity(this, velX, velY, velZ, flagsIn, flagsOut); // {net.minecraft.entity.Entity.moveEntity(double, double, double)}
    isCollidedHorizontally = flagsOut.isCollidedHorizontally;
    isCollidedVertically = flagsOut.isCollidedVertically;
    isCollided = flagsOut.isCollided;
    onGround = flagsOut.onGround;
  }

  /**
   * Simulate 1 tick of horizontal movement. See
   * {net.minecraft.client.entity.EntityPlayerSP.onUpdate()} See
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
  public void step(final XYZMoveEntityHandlerFromBlockGrid moveEntityHandler, final float keyStrafe,
      final float keyForward, final boolean keyJump, final boolean keySprint, final boolean keySneak) {
    AbstractXYZBlockGrid blockGrid = moveEntityHandler.blockGrid;
    // Start {net.minecraft.client.entity.EntityLiving.onEntityUpdate()}
    // Call to {net.minecraft.entity.Entity.handleWaterMovement()}, see {net.minecraft.world.World.handleMaterialAcceleration(BoundingBox, Material, Entity)}
    handleWaterMovement(blockGrid);
    // Start {net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate()}
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
    if (jumpTicks > 0)
      --jumpTicks;
    if (Math.abs(this.velX) < 0.005D)
      this.velX = 0.0D;
    if (Math.abs(this.velY) < 0.005D)
      this.velY = 0.0D;
    if (Math.abs(this.velZ) < 0.005D)
      this.velZ = 0.0D;
    final boolean isInLava = !inWater && isInLava(blockGrid); // Only check whether in lava once if not in water
    if (keyJump) {
      if (inWater) { // {net.minecraft.entity.Entity.isInWater()}
        velY += 0.03999999910593033D; // {net.minecraft.entity.EntityLivingBase.updateAITick()}
      } else if (isInLava) { // {net.minecraft.entity.Entity.isInLava()}
        velY += 0.03999999910593033D; // {net.minecraft.entity.EntityLivingBase.handleJumpLava()}
      } else {
        if (onGround && jumpTicks == 0) {
          // Call to {net.minecraft.entity.player.EntityPlayer.jump()}
          velY = jumpUpwardsMotionDouble;
          if (hasJumpBoostEffect)
            velY += (double)((float)(jumpBoostEffectAmplifier + 1) * 0.1F);
          if (isSprinting) {
            final float yawRad = yaw * 0.017453292F;
            velX -= (double)(MathHelper.sin(yawRad) * 0.2F);
            velZ += (double)(MathHelper.cos(yawRad) * 0.2F);
          }
          // Return from jump
        }
        jumpTicks = 10;
      }
    } else
      jumpTicks = 0;
    final float moveStrafing = movementInputMoveStrafing * 0.98F;
    final float moveForward = movementInputMoveForward * 0.98F;
    // Call to {net.minecraft.entity.player.EntityPlayer.moveEntityWithHeading(float, float)}, immediately delagated to {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
    if (inWater) { // {net.minecraft.entity.Entity.isInWater()}
      final double oldPosY = posY;
      float dragFactor = 0.8F;
      float friction = 0.02F;
      final float depthStriderLevel = onGround ? depthStriderModifierEffective : depthStriderModifierHalf;
      if (depthStriderLevel > 0.0F) {
        dragFactor += (0.54600006F - dragFactor) * depthStriderLevel / 3.0F;
        friction += (cachedMovementSpeedFloat * 1.0F - friction) * depthStriderLevel / 3.0F;
      }
      // Call to {net.minecraft.entity.Entity.moveFlying(float, float, float)}
      float movementDistance = moveStrafing * moveStrafing + moveForward * moveForward;
      if (movementDistance >= 1E-4F) {
        movementDistance = MathHelper.sqrt(movementDistance);
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
      moveEntity(moveEntityHandler, keySneak);
      velX *= (double)dragFactor;
      velY *= 0.800000011920929D;
      velZ *= (double)dragFactor;
      velY -= 0.02D;
      if (isCollidedHorizontally
          && isOffsetPositionInLiquid(blockGrid, velX, velY + 0.6000000238418579D - posY + oldPosY, velZ))
        velY = 0.30000001192092896D;
    } else if (isInLava) { // {net.minecraft.entity.Entity.isInLava()}
      final double oldPosY = posY;
      // Call to {net.minecraft.entity.Entity.moveFlying(float, float, float)}
      float movementDistance = moveStrafing * moveStrafing + moveForward * moveForward;
      if (movementDistance >= 1E-4F) {
        movementDistance = MathHelper.sqrt(movementDistance);
        if (movementDistance < 1F)
          movementDistance = 1F;
        movementDistance = 0.02F / movementDistance;
        final float strafe = moveStrafing * movementDistance;
        final float forward = moveForward * movementDistance;
        float yawRad = yaw * (float)Math.PI / 180F;
        final float sinYaw = MathHelper.sin(yawRad);
        final float cosYaw = MathHelper.cos(yawRad);
        velX += (double)(strafe * cosYaw - forward * sinYaw);
        velZ += (double)(forward * cosYaw + strafe * sinYaw);
      }
      // Return from moveFlying
      moveEntity(moveEntityHandler, keySneak);
      velX *= 0.5D;
      velY *= 0.5D;
      velZ *= 0.5D;
      velY -= 0.02D;
      if (isCollidedHorizontally
          && isOffsetPositionInLiquid(blockGrid, velX, velY + 0.6000000238418579D - posY + oldPosY, velZ))
        velY = 0.30000001192092896D;
    } else {
      final float friction = onGround
          ? cachedMovementSpeedFloat * blockGrid.getBlockAt(MathHelper.floor(posX),
              MathHelper.floor(boundingBox.minY) - 1, MathHelper.floor(posZ)).friction_intermediate
          : jumpMovementFactor;
      // Call to {net.minecraft.entity.Entity.moveFlying(float, float, float)}
      float movementDistance = moveStrafing * moveStrafing + moveForward * moveForward;
      if (movementDistance >= 1E-4F) {
        movementDistance = MathHelper.sqrt(movementDistance);
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
      final double XZdampFactor = onGround ? blockGrid.getBlockAt(MathHelper.floor(posX),
          MathHelper.floor(boundingBox.minY) - 1, MathHelper.floor(posZ)).groundFriction : airFriction;
      if (isOnLadder(blockGrid)) {
        final float ladderVelocityCap = 0.15F;
        velX = MathHelper.clamp(velX, (double)(-ladderVelocityCap), (double)ladderVelocityCap);
        velZ = MathHelper.clamp(velZ, (double)(-ladderVelocityCap), (double)ladderVelocityCap);
        if (keySneak && velY < 0.0D)
          velY = 0.0D;
        else if (velY < -0.15D)
          velY = -0.15D;
      }
      moveEntity(moveEntityHandler, keySneak);
      if (isCollidedHorizontally && isOnLadder(blockGrid))
        velY = 0.2D;
      velY -= 0.08D;
      velY *= 0.9800000190734863D;
      velX *= XZdampFactor;
      velZ *= XZdampFactor;
    }
    // Return from moveEntityWithHeading
    // Return from (EntityLivingBase) onLivingUpdate
    jumpMovementFactor = isSprinting ? jumpMovementFactorSprinting : jumpMovementFactorBase;
    // setAIMoveSpeed
    // Return from (EntityPlayer) onLivingUpdate
  }
}
