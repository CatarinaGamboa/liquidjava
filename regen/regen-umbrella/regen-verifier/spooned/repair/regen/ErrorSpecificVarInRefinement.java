package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorSpecificVarInRefinement {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 6;
        @repair.regen.specification.Refinement("_ > a")
        int b = 9;
    }
}

