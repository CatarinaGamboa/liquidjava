package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorAfterIf2 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("k > 0 && k < 100")
        int k = 5;
        if (k > 7) {
            k = 9;
        }
        k = 50;
        @liquidjava.specification.Refinement("_ < 10")
        int m = k;
    }
}
