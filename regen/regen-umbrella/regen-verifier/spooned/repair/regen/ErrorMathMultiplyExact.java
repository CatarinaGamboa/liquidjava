package repair.regen;


public class ErrorMathMultiplyExact {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v == 40")
        int mul = java.lang.Math.multiplyExact(5, 8);
        @repair.regen.specification.Refinement("\\v == -mul")
        int mul1 = java.lang.Math.multiplyExact(mul, (-1));
        @repair.regen.specification.Refinement("\\v < 0")
        int mul2 = java.lang.Math.multiplyExact(mul1, mul1);
    }
}

