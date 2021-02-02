package repair.regen;


public class ErrorArithmeticFP3 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 5.0")
        double a = 5.5;
        @repair.regen.specification.Refinement("\\v < -5.5")
        double d = -a;
    }
}

