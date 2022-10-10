package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorArithmeticFP2 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 5.0")
        double a = 5.5;
        @liquidjava.specification.Refinement("_ == 10.0")
        double c = a * 2.0;
    }
}

