package minecraft_simulator.v1_14.block;

import java.util.ArrayList;
import java.util.List;

/**
 * Do something similar to {net.minecraft.util.shape.DoubleListPair}
 */
public class DoubleListPair {
  public final double[] mergedList; // {net.minecraft.util.shape.SimpleDoubleListPair.mergedList}

  public final int[] indexes1; // {net.minecraft.util.shape.SimpleDoubleListPair.field_1376}
  public final int[] indexes2; // {net.minecraft.util.shape.SimpleDoubleListPair.field_1378}

  public DoubleListPair(double[] mergedList, int[] indexes1, int[] indexes2) {
    this.mergedList = mergedList;
    this.indexes1 = indexes1;
    this.indexes2 = indexes2;
  }

  public DoubleListPair(List<Double> mergedList, List<Integer> indexes1, List<Integer> indexes2) {
    this(toDoubleArray(mergedList), toIntArray(indexes1), toIntArray(indexes2));
  }

  /**
   * See
   * {net.minecraft.util.shape.SimpleDoubleListPair.SimpleDoubleListPair(DoubleList,
   * DoubleList, boolean, boolean)}
   * 
   * @param first
   * @param second
   * @param onFirst
   * @param onSecond
   * @return
   */
  public static DoubleListPair simple(double[] first, double[] second, boolean onFirst, boolean onSecond) {
    int i1 = 0;
    int i2 = 0;
    double last = Double.NaN;
    int size1 = first.length;
    int size2 = second.length;
    int maxSize = size1 + size2;
    ArrayList<Double> mergedList = new ArrayList<>(maxSize);
    ArrayList<Integer> indexes1 = new ArrayList<>(maxSize);
    ArrayList<Integer> indexes2 = new ArrayList<>(maxSize);
    while (true) {
      boolean hasNext1 = i1 < size1;
      boolean hasNext2 = i2 < size2;
      if (!hasNext1 && !hasNext2)
        break;
      boolean nextInFirst = hasNext1 && (!hasNext2 || first[i1] < second[i2] + 1.0E-7);
      double next = nextInFirst ? first[i1++] : second[i2++];
      if ((i1 == 0 || !hasNext1) && !nextInFirst && !onSecond || (i2 == 0 || !hasNext2) && nextInFirst && !onFirst)
        continue;
      if (!(last >= next - 1.0E-7)) {
        indexes1.add(i1 - 1);
        indexes2.add(i2 - 1);
        mergedList.add(next);
        last = next;
        continue;
      }
      if (mergedList.isEmpty())
        continue;
      indexes1.set(indexes1.size() - 1, i1 - 1);
      indexes2.set(indexes2.size() - 1, i2 - 1);
    }
    if (mergedList.isEmpty()) {
      mergedList.add(Math.min(first[size1 - 1], second[size2 - 1]));
    }
    return new DoubleListPair(mergedList, indexes1, indexes2);
  }

  public static DoubleListPair fractional(int sections1, int sections2) {
    int lcm = (int)VoxelShapes.lcm(sections1, sections2);
    double[] mergedList = SimpleVoxelShape.emulateFractionalDoubleList(lcm);
    int[] indexes1 = new int[lcm + 1];
    int[] indexes2 = new int[lcm + 1];
    int per1 = lcm / sections1;
    int per2 = lcm / sections2;
    for (int i = 0; i <= lcm; i++) {
      indexes1[i] = i / per1;
      indexes2[i] = i / per2;
    }
    return new DoubleListPair(mergedList, indexes1, indexes2);
  }

  protected static int[] toIntArray(List<Integer> list) {
    int[] r = new int[list.size()];
    for (int i = 0; i < list.size(); i++)
      r[i] = list.get(i);
    return r;
  }

  protected static double[] toDoubleArray(List<Double> list) {
    double[] r = new double[list.size()];
    for (int i = 0; i < list.size(); i++)
      r[i] = list.get(i);
    return r;
  }
}
