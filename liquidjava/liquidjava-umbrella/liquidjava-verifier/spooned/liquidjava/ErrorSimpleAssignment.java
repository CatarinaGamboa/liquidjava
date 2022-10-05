package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorSimpleAssignment {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("c > 2")
        int c = 2;// should emit error

    }
}

