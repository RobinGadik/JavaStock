package acm2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main1 {

  static Map<Integer, Pair> mapCountToIndex = null;
  static List<Pair> list = null;

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    final int n = in.nextInt();
    final int a = in.nextInt();

    mapCountToIndex = new HashMap<>();
    list = new ArrayList<>(n);
    list.add(new Pair(a, 1));
    mapCountToIndex.put(1, new Pair(0, 1));
    final StringBuilder sb = new StringBuilder(n * 11 + 5);
    sb.append(1);
    for (int i = 1; i < n; i++) {
      final int y = in.nextInt();
      final int x = getXFromY(y);
      binarySearchAndUpdate(x);
      sb.append(" ");
      sb.append(x);
    }
    System.out.println(sb);
  }

  // 1 1 5 1 1
  // 1 0 4 -4 -4

  // 1 0 5
  // 1 -1 -1

  private static int getXFromY(final int y) {
    final long ai = list.get(mapCountToIndex.get(mapCountToIndex.size()).x).x;
    final long bi = list.get(mapCountToIndex.get(1).count - 1).x;
    if (ai != 0) return (int) ((y + bi) / ai);
    return 0;
  }

  private static void binarySearchAndUpdate(final int x) {
    for (final Entry<Integer, Pair> p : mapCountToIndex.entrySet()) {
      if (p.getValue().count == p.getValue().x) {
        continue;
      }
      final int count = p.getKey();
      final int from = p.getValue().x;
      final int to = p.getValue().count;
      final int pos = binarySearch(x, from, to - 1);
      if (pos > 0) {
        final Pair pair = list.remove(pos);
        pair.count++;
        final int nextCount = pair.count;
        mapCountToIndex.get(count).count--;
        final int rightBorder = mapCountToIndex.get(count).count;
        mapCountToIndex.putIfAbsent(nextCount, new Pair(rightBorder, rightBorder + 1));
        final Pair borders = mapCountToIndex.get(nextCount);
        final int newPos = -binarySearch(x, borders.x, borders.count - 2);

        list.add(newPos, pair);

        return;
      }
    }
    final Pair pair = new Pair(x, 1);
    for (final Entry<Integer, Pair> p : mapCountToIndex.entrySet()) {
      if (p.getKey() != 1) {
        p.getValue().x++;
      }
      p.getValue().count++;
    }
    Pair borders = mapCountToIndex.get(1);
    final int newPos = -binarySearch(x, borders.x, borders.count - 2);

    list.add(newPos, pair);
  }

  private static int binarySearch(final int x, final int from, final int to) {
    int low = from;
    int high = to;

    while (low <= high) {
      final int mid = (low + high) >>> 1;
      final Pair p = list.get(mid);
      final int cmpValue = x - p.x;

      if (cmpValue > 0) {
        low = mid + 1;
      } else if (cmpValue < 0) {
        high = mid - 1;
      } else {
        return mid; // key found
      }
    }
    return -(low);
  }

  static class Pair implements Comparable<Pair> {

    int x;
    int count;

    @Override
    public int compareTo(final Pair o) {
      return count > o.count || (count == o.count && x > o.x) ? 1 : -1;
    }

    Pair(final int x, final int count) {
      this.x = x;
      this.count = count;
    }

    @Override
    public String toString() {
      return "Pair{" +
          "x=" + x +
          ", count=" + count +
          '}';
    }
  }
}
