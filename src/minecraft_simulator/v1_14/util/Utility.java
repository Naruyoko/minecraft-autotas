package minecraft_simulator.v1_14.util;

public class Utility {
  public static String padSignFloat(float value) {
    var r = Float.toString(value);
    if (value == 0)
      return ' ' + r;
    else if (value > 0)
      return '+' + r;
    else
      return r;
  }

  public static String padSignDouble(double value) {
    var r = Double.toString(value);
    if (value == 0)
      return ' ' + r;
    else if (value > 0)
      return '+' + r;
    else
      return r;
  }
}
