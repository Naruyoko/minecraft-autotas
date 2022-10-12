package minecraft_simulator.v1_14.util;

/**
 * See {net.minecraft.util.BooleanBiFunction}
 * 
 * Easily replicable; copied from decompiled file to save time
 */
public interface BooleanBiFunction {
  public static final BooleanBiFunction FALSE = (a, b) -> false;
  public static final BooleanBiFunction NOT_OR = (a, b) -> !a && !b;
  public static final BooleanBiFunction ONLY_SECOND = (a, b) -> b && !a;
  public static final BooleanBiFunction NOT_FIRST = (a, b) -> !a;
  public static final BooleanBiFunction ONLY_FIRST = (a, b) -> a && !b;
  public static final BooleanBiFunction NOT_SECOND = (a, b) -> !b;
  public static final BooleanBiFunction NOT_SAME = (a, b) -> a != b;
  public static final BooleanBiFunction NOT_AND = (a, b) -> !a || !b;
  public static final BooleanBiFunction AND = (a, b) -> a && b;
  public static final BooleanBiFunction SAME = (a, b) -> a == b;
  public static final BooleanBiFunction SECOND = (a, b) -> b;
  public static final BooleanBiFunction CAUSES = (a, b) -> !a || b;
  public static final BooleanBiFunction FIRST = (a, b) -> a;
  public static final BooleanBiFunction CAUSED_BY = (a, b) -> a || !b;
  public static final BooleanBiFunction OR = (a, b) -> a || b;
  public static final BooleanBiFunction TRUE = (a, b) -> true;

  public boolean apply(boolean a, boolean b);
}