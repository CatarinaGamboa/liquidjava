package repair.regen;


@java.lang.SuppressWarnings("unused")
public class ErrorSimpleAssignment {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("c > 2")
        int c = 2;// should emit error

    }
}

