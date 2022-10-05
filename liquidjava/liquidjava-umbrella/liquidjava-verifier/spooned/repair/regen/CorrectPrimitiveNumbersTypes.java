package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectPrimitiveNumbersTypes {
    @liquidjava.specification.Refinement("_ < i && _ > 0")
    private static double fromType(@liquidjava.specification.Refinement("_ > 0")
    int i) {
        return i * 0.1;
    }

    @liquidjava.specification.Refinement(" _ < i && _ > 0")
    private static double fromType(@liquidjava.specification.Refinement("_ > 0")
    long i) {
        return i * 0.1;
    }

    @liquidjava.specification.Refinement(" _ < i && _ > 0")
    private static double fromType(@liquidjava.specification.Refinement("_ > 0")
    short i) {
        return i * 0.1;
    }

    @liquidjava.specification.Refinement("_ > i")
    private static float twice(@liquidjava.specification.Refinement("i > 0")
    short i) {
        return i * 2.0F;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ > 5")
        int a = 10;
        @liquidjava.specification.Refinement("_ > 5")
        long b = 100;
        @liquidjava.specification.Refinement("_ > 5")
        short c = 10;
        @liquidjava.specification.Refinement("_ > 5")
        float d = 7.4F;
        @liquidjava.specification.Refinement("_ > 0")
        double e = liquidjava.CorrectPrimitiveNumbersTypes.fromType(a);
        e = liquidjava.CorrectPrimitiveNumbersTypes.fromType(b);
        e = liquidjava.CorrectPrimitiveNumbersTypes.fromType(c);
        @liquidjava.specification.Refinement("f > 0")
        float f = liquidjava.CorrectPrimitiveNumbersTypes.twice(c);
    }
}

