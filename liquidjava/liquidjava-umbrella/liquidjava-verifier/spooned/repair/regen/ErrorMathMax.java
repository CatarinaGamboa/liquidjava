package liquidjava;


public class ErrorMathMax {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ == 5")
        int m1 = java.lang.Math.max(4, 5);
        @liquidjava.specification.Refinement("_ > 5")
        int m2 = java.lang.Math.max(100, m1);
        @liquidjava.specification.Refinement("_ == 100")
        int m3 = java.lang.Math.max(100, m2);
        @liquidjava.specification.Refinement("_ == -1000")
        int m4 = java.lang.Math.max((-1000), (-m3));
    }
}

