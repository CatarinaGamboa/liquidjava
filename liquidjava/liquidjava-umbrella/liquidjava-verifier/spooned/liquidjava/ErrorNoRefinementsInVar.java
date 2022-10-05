package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorNoRefinementsInVar {
    public static void main(java.lang.String[] args) {
        int a = 11;
        @liquidjava.specification.Refinement("b < 10")
        int b = a;
    }
}

