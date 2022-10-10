package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorUnaryOperators {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ < 10")
        int v = 3;
        v--;
        @liquidjava.specification.Refinement("_ >= 10")
        int s = 10;
        s--;
    }
}

