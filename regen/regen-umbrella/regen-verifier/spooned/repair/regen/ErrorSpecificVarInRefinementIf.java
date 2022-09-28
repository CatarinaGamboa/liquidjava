package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorSpecificVarInRefinementIf {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int a = 6;
        if (a > 0) {
            a = -2;
            @repair.regen.specification.Refinement("b < a")
            int b = -3;
        }
    }
}

