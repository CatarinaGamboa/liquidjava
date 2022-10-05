package liquidjava;


@java.lang.SuppressWarnings("unused")
public class ErrorLongUsage2 {
    @liquidjava.specification.Refinement(" _ > 40")
    public static long doubleBiggerThanTwenty(@liquidjava.specification.Refinement("a > 20")
    long a) {
        return a * 2;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("a > 5")
        long a = 9L;
        @liquidjava.specification.Refinement("c > 40")
        long c = liquidjava.ErrorLongUsage2.doubleBiggerThanTwenty((a * 2));
    }
}

