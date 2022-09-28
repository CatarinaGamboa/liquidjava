package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorArithmeticFP2 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 5.0")
        double a = 5.5;
        @repair.regen.specification.Refinement("_ == 10.0")
        double c = a * 2.0;
    }
}

