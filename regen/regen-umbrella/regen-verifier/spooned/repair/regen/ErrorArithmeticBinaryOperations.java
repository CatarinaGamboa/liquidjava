package repair.regen;


public class ErrorArithmeticBinaryOperations {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 100")
        int y = 50;
        @repair.regen.specification.Refinement("\\v > 0")
        int z = y - 3;
    }
}

