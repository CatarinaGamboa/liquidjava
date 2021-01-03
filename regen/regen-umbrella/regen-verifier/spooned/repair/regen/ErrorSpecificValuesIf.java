package repair.regen;


public class ErrorSpecificValuesIf {
    @repair.regen.specification.Refinement("{a > 0} -> {true}")
    public static void addZ(int a) {
        @repair.regen.specification.Refinement("\\v > 0")
        int d = a;
        if (d > 5) {
            @repair.regen.specification.Refinement("b > 5")
            int b = d;
        } else {
            @repair.regen.specification.Refinement("\\v <= 5")
            int c = d;
            d = 10;
            @repair.regen.specification.Refinement("b > 10")
            int b = d;
        }
    }
}

