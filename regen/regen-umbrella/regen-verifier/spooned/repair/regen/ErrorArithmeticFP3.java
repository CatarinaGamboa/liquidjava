package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorArithmeticFP3 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 5.0")
        double a = 5.5;
        @repair.regen.specification.Refinement("_ < -5.5")
        double d = -a;
    }
}

