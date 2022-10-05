package liquidjava.math.errorAbs;


@java.lang.SuppressWarnings("unused")
public class ErrorMathAbs {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("true")
        int ab = java.lang.Math.abs((-9));
        @liquidjava.specification.Refinement("_ == 9")
        int ab1 = -ab;
    }
}

