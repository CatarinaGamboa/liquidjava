package testSuite;

import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class SimpleTest {
  public static void main(String[] args) {
    // Arithmetic Binary Operations
    @Refinement("a >= 10")
    int a = 10;
    @Refinement("t > 0")
    int t = a + 1;
  }
}
