package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorTypeInRefinements {
    public static void main(java.lang.String[] args) {
        int a = 10;
        @liquidjava.specification.Refinement("(b == 6)")
        boolean b = true;
    }
}

