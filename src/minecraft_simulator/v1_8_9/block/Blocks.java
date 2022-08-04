package minecraft_simulator.v1_8_9.block;

public class Blocks {
  public static final Block fullBlock = new Block();
  public static final Block slabBottom = new Block(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
  public static final Block slabTop = new Block(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
  public static final Block singleChest = new Block(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  public static final Block doubleChestNegXSide = new Block(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
  public static final Block doubleChestPosXSide = new Block(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
  public static final Block doubleChestNegZSide = new Block(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
  public static final Block doubleChestPosZSide = new Block(0.0625F, 0.0F, 0.0625F, 0.0F, 0.875F, 0.9375F);
  public static final Block doubleChestWestSide = doubleChestNegXSide;
  public static final Block doubleChestEastSide = doubleChestPosXSide;
  public static final Block doubleChestNorthSide = doubleChestNegZSide;
  public static final Block doubleChestSouthSide = doubleChestPosZSide;
  public static final Block air = new PassableBlock();
}
