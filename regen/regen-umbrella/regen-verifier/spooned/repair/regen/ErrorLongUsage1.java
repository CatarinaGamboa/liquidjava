package repair.regen;


public class ErrorLongUsage1 {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 5")
        long a = 9L;
        if (a > 5) {
            @repair.regen.specification.Refinement("b < 50")
            long b = a * 10;
        }
    }
}

