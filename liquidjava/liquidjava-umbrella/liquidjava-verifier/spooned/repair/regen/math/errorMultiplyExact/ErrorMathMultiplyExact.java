package liquidjava.math.errorMultiplyExact;


@java.lang.SuppressWarnings("unused")
public class ErrorMathMultiplyExact {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ == 40")
        int mul = java.lang.Math.multiplyExact(5, 8);
        @liquidjava.specification.Refinement("_ == -mul")
        int mul1 = java.lang.Math.multiplyExact(mul, (-1));
        @liquidjava.specification.Refinement("_ < 0")
        int mul2 = java.lang.Math.multiplyExact(mul1, mul1);
    }
}

