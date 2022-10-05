package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectLongUsage {
    @liquidjava.specification.Refinement("_ > 10")
    public static long doubleBiggerThanTen(@liquidjava.specification.Refinement("a > 10")
    int a) {
        return a * 2;
    }

    @liquidjava.specification.Refinement("_ > 40")
    public static long doubleBiggerThanTwenty(@liquidjava.specification.Refinement("a > 20")
    long a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("a > 5")
        long a = 9L;
        if (a > 5) {
            @liquidjava.specification.Refinement("b > 50")
            long b = a * 10;
            @liquidjava.specification.Refinement("c < 0")
            long c = -a;
        }
        @liquidjava.specification.Refinement("d > 10")
        long d = liquidjava.CorrectLongUsage.doubleBiggerThanTen(100);
        @liquidjava.specification.Refinement("e > 10")
        long e = liquidjava.CorrectLongUsage.doubleBiggerThanTwenty((d * 2));
        @liquidjava.specification.Refinement("_ > 10")
        long f = liquidjava.CorrectLongUsage.doubleBiggerThanTwenty((2 * 80));
    }
}

