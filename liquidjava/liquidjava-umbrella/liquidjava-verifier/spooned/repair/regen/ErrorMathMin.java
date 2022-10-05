package liquidjava;


public class ErrorMathMin {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ == 4")
        int m1 = java.lang.Math.min(4, 5);
        @liquidjava.specification.Refinement("_ < 5")
        int m2 = java.lang.Math.min(100, m1);
        @liquidjava.specification.Refinement("_ == 4")
        int m3 = java.lang.Math.min(100, m2);
        @liquidjava.specification.Refinement("_ == -1")
        int m4 = java.lang.Math.min((-1), (-m2));
    }
}

