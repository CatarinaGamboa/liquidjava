package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorSpecificValuesIf {
    public static void addZ(@liquidjava.specification.Refinement("a > 0")
    int a) {
        @liquidjava.specification.Refinement("_ > 0")
        int d = a;
        if (d > 5) {
            @liquidjava.specification.Refinement("b > 5")
            int b = d;
        } else {
            @liquidjava.specification.Refinement("_ <= 5")
            int c = d;
            d = 10;
            @liquidjava.specification.Refinement("b > 10")
            int b = d;
        }
    }
}

