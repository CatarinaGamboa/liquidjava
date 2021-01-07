package repair.regen;


public class ErrorSpecificVarInRefinement {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 6;
        @repair.regen.specification.Refinement("\\v > a")
        int b = 9;
    }
}

