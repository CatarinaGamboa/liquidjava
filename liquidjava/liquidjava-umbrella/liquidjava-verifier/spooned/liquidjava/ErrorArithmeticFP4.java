package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorArithmeticFP4 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 5.0")
        double a = 5.5;
        @liquidjava.specification.Refinement("_ < -5.5")
        double d = -(a - 2.0);
    }
}

