package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorIfAssignment {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int a = 5;
        if (a > 0) {
            @liquidjava.specification.Refinement("b > 0")
            int b = a;
            b++;
            a = 10;
        }
    }
}

