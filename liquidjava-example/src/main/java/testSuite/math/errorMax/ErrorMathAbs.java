package testSuite.math.errorMax;

import liquidjava.specification.Refinement;

@SuppressWarnings("unused")
public class ErrorMathAbs {
  public static void main(String[] args) {
    @Refinement("true")
    int ab = Math.abs(-9);

    @Refinement("_ == 9")
    int ab1 = -ab;
  }
}
