package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorLongUsage1 {
    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("a > 5")
        long a = 9L;
        if (a > 5) {
            @liquidjava.specification.Refinement("b < 50")
            long b = a * 10;
        }
    }
}

