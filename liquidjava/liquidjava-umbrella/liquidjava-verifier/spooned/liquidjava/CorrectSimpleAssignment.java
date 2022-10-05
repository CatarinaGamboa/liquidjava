package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectSimpleAssignment {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("a > 0")
        int a = 1;
        @liquidjava.specification.Refinement("b == 2 || b == 3 || b == 4")
        int b = 2;
        @liquidjava.specification.Refinement("d >= 2")
        int d = b;// should be okay

    }
}

