package repair.regen;


public class ErrorTypeInRefinements {
    public static void main(java.lang.String[] args) {
        int a = 10;
        @repair.regen.specification.Refinement("(b == 6)")
        boolean b = true;
    }
}

