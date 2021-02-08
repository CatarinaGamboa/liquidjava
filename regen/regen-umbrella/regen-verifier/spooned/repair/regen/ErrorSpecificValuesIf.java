package repair.regen;


public class ErrorSpecificValuesIf {
    public static void addZ(@repair.regen.specification.Refinement("a > 0")
    int a) {
        @repair.regen.specification.Refinement("_ > 0")
        int d = a;
        if (d > 5) {
            @repair.regen.specification.Refinement("b > 5")
            int b = d;
        } else {
            @repair.regen.specification.Refinement("_ <= 5")
            int c = d;
            d = 10;
            @repair.regen.specification.Refinement("b > 10")
            int b = d;
        }
    }
}

