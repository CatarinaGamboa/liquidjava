package liquidjava;

import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class CorrectArithmeticBinaryOperations {
  public static void main(String[] args) {
    // Arithmetic Binary Operations
    @Refinement("a == 10")
    int a = 10;
    @Refinement("b != 10")
    int b = 5;
    @Refinement("t > 0")
    int t = a + 1;
    @Refinement("_ >= 9")
    int k = a - 1;
    @Refinement("_ >= 5")
    int l = k * t;
    @Refinement("_ > 0")
    int m = l / 2;
  }
}
