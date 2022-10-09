package minecraft_simulator.v1_14.util;

import java.util.function.IntPredicate;

/**
 * See {net.minecraft.util.MathHelper}
 */
public class MathHelper {
  // Required for accurate physics simulation
  // See https://www.mcpk.wiki/wiki/Angles
  public static final float[] SIN_TABLE = new float[65536];
  static {
    for (int i = 0; i < 65536; i++) {
      SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2D / 65536D);
    }
  }

  public static float sin(float value) { return SIN_TABLE[(int)(value * 10430.378F) & 65535]; }

  public static float cos(float value) { return SIN_TABLE[(int)(value * 10430.378F + 16384F) & 65535]; }

  public static float sqrt(double value) { return (float)Math.sqrt(value); }

  public static float sqrt(float value) { return (float)Math.sqrt((double)value); }

  public static int floor(double d) {
    int i = (int)d;
    return d < (double)i ? i - 1 : i;
  }

  public static int clamp(int num, int min, int max) { return num < min ? min : num > max ? max : num; }

  public static int binarySearch(int i, int j, IntPredicate intPredicate) {
    int k = j - i;
    while (k > 0) {
      int l = k / 2;
      int m = i + l;
      if (intPredicate.test(m)) {
        k = l;
      } else {
        i = m + 1;
        k -= l + 1;
      }
    }
    return i;
  }
}
