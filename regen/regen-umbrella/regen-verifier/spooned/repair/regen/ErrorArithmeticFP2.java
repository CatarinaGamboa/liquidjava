package repair.regen;


public class ErrorArithmeticFP2 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 5.0")
        double a = 5.5;
        @repair.regen.specification.Refinement("\\v == 10.0")
        double c = a * 2.0;
    }
}

