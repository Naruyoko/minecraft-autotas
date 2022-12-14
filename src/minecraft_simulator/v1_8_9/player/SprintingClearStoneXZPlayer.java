package minecraft_simulator.v1_8_9.player;

import minecraft_simulator.v1_8_9.collision.XZAxisAlignedBB;
import minecraft_simulator.v1_8_9.util.MathHelper;

/**
 * Simulates the player movement assuming with following restrictions: - Always
 * sprinting - No block interactions - Assumed to be on ground with normal
 * slipperiness - No falling (XZ-only) - No status effects
 */
public class SprintingClearStoneXZPlayer extends AbstractXZPlayer {
  public SprintingClearStoneXZPlayer(XZAxisAlignedBB boundingBox, double posX, double posZ, double velX, double velZ,
      float yaw) {
    super(boundingBox, posX, posZ, velX, velZ, yaw);
  }

  public SprintingClearStoneXZPlayer(double posX, double posZ, double velX, double velZ, float yaw) {
    super(posX, posZ, velX, velZ, yaw);
  }

  public SprintingClearStoneXZPlayer() { super(); }

  @Override
  public SprintingClearStoneXZPlayer clone() {
    return new SprintingClearStoneXZPlayer(boundingBox.clone(), posX, posZ, velX, velZ, yaw);
  }

  public static void copy(SprintingClearStoneXZPlayer target, SprintingClearStoneXZPlayer source) {
    AbstractXZPlayer.copy(target, source);
  }

  // See https://www.mcpk.wiki/wiki/Mouse_Movement and {net.minecraft.client.renderer.EntityRenderer.updateCameraAndRender(float, long)}
  public static final float mouseSensitivity = 0.5F;
  private static final float mouseMult_intermediate = mouseSensitivity * 0.6F + 0.2F;
  public static final float mouseMult = mouseMult_intermediate * mouseMult_intermediate * mouseMult_intermediate * 8.0F;

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
  public static final double baseMovementSpeed = 0.10000000149011612D;
  // {net.minecraft.entity.EntityLivingBase.setSprinting(boolean)} {net.minecraft.entity.EntityLivingBase.sprintingSpeedBoostModifier}
  public static final double sprintingSpeedBoostModifier = 0.30000001192092896D;
  // Set in {net.minecraft.entity.player.EntityPlayer.onLivingUpdate()}
  // {net.minecraft.entity.ai.attributes.ModifiableAttributeInstance.computeValue()}
  public static final float movementSpeed = (float)(baseMovementSpeed * (1.0D + sprintingSpeedBoostModifier));
  // {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
  private static final float blockFrictionFactor = slipperinessDefault * 0.91F;
  private static final float friction_intermediate = 0.16277136F
      / (blockFrictionFactor * blockFrictionFactor * blockFrictionFactor);
  public static final float friction = movementSpeed * friction_intermediate;
  public static final float STRAFE_LEFT = 1.0F;
  public static final float STRAFE_RIGHT = -1.0F;
  public static final float MOVE_FORWARD = 1.0F;
  public static final float MOVE_BACKWARD = -1.0F;

  /**
   * Simulate 1 tick of movement on a normal flat surface given the yaw while
   * springting See {net.minecraft.entity.EntityLivingBase.onLivingUpdate()} See
   * {net.minecraft.util.MovementInputFromOptions.updatePlayerMoveState()} and
   * {net.minecraft.client.entity.EntityPlayerSP.updateEntityActionState()} for
   * the source of the movement inputs
   * 
   * @param movementInputMoveStrafe  +1.0F if inputting strafe left, -1.0F if
   *                                 inputting strafe right
   * @param movementInputMoveForward +1.0F if inputting move forward, -1.0F if
   *                                 inputting move backward
   */
  public void step(final float movementInputMoveStrafe, final float movementInputMoveForward) {
    if (Math.abs(this.velX) < 0.005D)
      this.velX = 0.0D;
    if (Math.abs(this.velZ) < 0.005D)
      this.velZ = 0.0D;
    // Call to {net.minecraft.entity.player.EntityPlayer.jump()} if jumping, omitted
    final float moveStrafing = movementInputMoveStrafe * 0.98F;
    final float moveForward = movementInputMoveForward * 0.98F;
    // Call to {net.minecraft.entity.player.EntityPlayer.moveEntityWithHeading(float, float)}, immediately delagated to {net.minecraft.entity.EntityLivingBase.moveEntityWithHeading(float, float)}
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
    // Call to {net.minecraft.entity.Entity.moveEntity(double, double, double)}
    // Positions are calculated by manipulating the bounding boxes and updated by {net.minecraft.entity.Entity.resetPositionToBB()}
    boundingBox.mutatingOffset(velX, velZ);
    posX = (boundingBox.minX + boundingBox.maxX) / 2.0D;
    posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D;
    // Return from moveEntity
    velX *= (double)blockFrictionFactor;
    velZ *= (double)blockFrictionFactor;
    // Return from moveEntityWithHeading
  }
}
