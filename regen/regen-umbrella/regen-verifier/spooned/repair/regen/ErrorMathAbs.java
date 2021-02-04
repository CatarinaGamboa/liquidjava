package repair.regen;


public class ErrorMathAbs {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("true")
        int ab = java.lang.Math.abs((-9));
        @repair.regen.specification.Refinement("_ == 9")
        int ab1 = -ab;
    }
}

