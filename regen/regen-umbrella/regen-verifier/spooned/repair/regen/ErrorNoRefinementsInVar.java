package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorNoRefinementsInVar {
    public static void main(java.lang.String[] args) {
        int a = 11;
        @repair.regen.specification.Refinement("b < 10")
        int b = a;
    }
}

