package repair.regen;


public class ErrorLongUsage2 {
    @repair.regen.specification.Refinement("{a > 20}->{ _ > 40}")
    public static long doubleBiggerThanTwenty(long a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 5")
        long a = 9L;
        @repair.regen.specification.Refinement("c > 40")
        long c = repair.regen.ErrorLongUsage2.doubleBiggerThanTwenty((a * 2));
    }
}

