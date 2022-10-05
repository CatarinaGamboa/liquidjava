package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorSpecificValuesIf2 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 10")
        int a = 15;
        if (a > 14) {
            a = 12;
            @liquidjava.specification.Refinement("_ < 11")
            int c = a;
        }
    }
}

