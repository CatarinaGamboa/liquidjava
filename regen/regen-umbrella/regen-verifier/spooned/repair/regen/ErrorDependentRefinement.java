package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorDependentRefinement {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int smaller = 5;
        @repair.regen.specification.Refinement("bigger > 20")
        int bigger = 50;
        @repair.regen.specification.Refinement("_ > smaller  && _ < bigger")
        int middle = 21;
    }
}

