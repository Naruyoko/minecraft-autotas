package minecraft_zigzag_1_8_9;

// See {net.minecraft.util.MathHelper}
public class MathHelper {
  // Required for accurate physics simulation
  // See https://www.mcpk.wiki/wiki/Angles
  public static final float[] SIN_TABLE=new float[65536];
  static {
    for (int i=0;i<65536;i++){
      SIN_TABLE[i]=(float)Math.sin((double)i*Math.PI*2D/65536D);
    }
  }
  public static float sin(float value){
    return SIN_TABLE[(int)(value*10430.378F)&65535];
  }
  public static float cos(float value){
    return SIN_TABLE[(int)(value*10430.378F+16384F)&65535];
  }

  public static float sqrt_float(float value){
    return (float)Math.sqrt((double)value);
  }
  public static int floor_double(double value){
    int i=(int)value;
    return value<(double)i?i-1:i;
  }

  public static double clamp_double(double num,double min,double max){
    return num<min?min:num>max?max:num;
  }
}
