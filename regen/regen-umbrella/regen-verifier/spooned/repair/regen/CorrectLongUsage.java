package repair.regen;


@java.lang.SuppressWarnings("unused")
public class CorrectLongUsage {
    @repair.regen.specification.Refinement("_ > 10")
    public static long doubleBiggerThanTen(@repair.regen.specification.Refinement("a > 10")
    int a) {
        return a * 2;
    }

    @repair.regen.specification.Refinement("_ > 40")
    public static long doubleBiggerThanTwenty(@repair.regen.specification.Refinement("a > 20")
    long a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 5")
        long a = 9L;
        if (a > 5) {
            @repair.regen.specification.Refinement("b > 50")
            long b = a * 10;
            @repair.regen.specification.Refinement("c < 0")
            long c = -a;
        }
        @repair.regen.specification.Refinement("d > 10")
        long d = repair.regen.CorrectLongUsage.doubleBiggerThanTen(100);
        @repair.regen.specification.Refinement("e > 10")
        long e = repair.regen.CorrectLongUsage.doubleBiggerThanTwenty((d * 2));
        @repair.regen.specification.Refinement("_ > 10")
        long f = repair.regen.CorrectLongUsage.doubleBiggerThanTwenty((2 * 80));
    }
}

