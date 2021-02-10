package repair.regen.math.errorMultiplyExact;


public class ErrorMathMultiplyExact {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ == 40")
        int mul = java.lang.Math.multiplyExact(5, 8);
        @repair.regen.specification.Refinement("_ == -mul")
        int mul1 = java.lang.Math.multiplyExact(mul, (-1));
        @repair.regen.specification.Refinement("_ < 0")
        int mul2 = java.lang.Math.multiplyExact(mul1, mul1);
    }
}

