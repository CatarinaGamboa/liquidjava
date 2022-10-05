package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorSpecificVarInRefinementIf {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 6;
        if (a > 0) {
            a = -2;
            @liquidjava.specification.Refinement("b < a")
            int b = -3;
        }
    }
}

