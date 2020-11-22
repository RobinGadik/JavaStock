package acm2020;

import java.util.Scanner;

public class Main5 {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    final int n = in.nextInt();

    double r = n;
    long count = 0;
    long countA = 0;
    for (int y = 1; y <= n; y++) {
      long x = (long) Math.sqrt(r * r - y * y);
      count += (long) Math.sqrt(r * r - y * y);
      countA += r*r >= (x+1)*(x+1) + (y-1)*(y-1) ? 1 : 0;

    }
    count *= 4;
    countA *=4;
    System.out.println(count * 2 + " " + (count*2 + countA));

  }
}
