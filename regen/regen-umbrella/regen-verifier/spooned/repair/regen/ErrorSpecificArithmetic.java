package repair.regen;


public class ErrorSpecificArithmetic {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 5")
        int a = 10;
        @repair.regen.specification.Refinement("_ > 10")
        int b = a + 1;
        a = 6;
        b = a * 2;
        @repair.regen.specification.Refinement("_ > 20")
        int c = b * (-1);
    }
}

