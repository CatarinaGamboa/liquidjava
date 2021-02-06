package repair.regen;


public class ErrorSyntax1 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 100 +")
        int value = 90 + 4;
    }
}

