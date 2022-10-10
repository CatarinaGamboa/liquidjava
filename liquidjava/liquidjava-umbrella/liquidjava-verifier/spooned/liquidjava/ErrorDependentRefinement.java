package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorDependentRefinement {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int smaller = 5;
        @liquidjava.specification.Refinement("bigger > 20")
        int bigger = 50;
        @liquidjava.specification.Refinement("_ > smaller  && _ < bigger")
        int middle = 21;
    }
}

