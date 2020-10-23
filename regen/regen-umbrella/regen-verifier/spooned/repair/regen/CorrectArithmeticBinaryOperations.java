package repair.regen;


public class CorrectArithmeticBinaryOperations {
    public static void main(java.lang.String[] args) {
        // Arithmetic Binary Operations
        @repair.regen.specification.Refinement("a == 10")
        int a = 10;
        @repair.regen.specification.Refinement("b != 10")
        int b = 5;
        @repair.regen.specification.Refinement("t > 0")
        int t = a + 1;
        @repair.regen.specification.Refinement("\\v >= 9")
        int k = a - 1;
        @repair.regen.specification.Refinement("\\v >= 5")
        int l = k * t;
        @repair.regen.specification.Refinement("\\v > 0")
        int m = l / 2;
    }
}

