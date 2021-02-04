package repair.regen;


public class ErrorArithmeticBinaryOperations {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 100")
        int y = 50;
        @repair.regen.specification.Refinement("_ > 0")
        int z = y - 51;
    }
}

