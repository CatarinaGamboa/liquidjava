package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorSpecificVarInRefinement {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 6;
        @liquidjava.specification.Refinement("_ > a")
        int b = 9;
    }
}

