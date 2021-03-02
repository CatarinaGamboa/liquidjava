package repair.regen;


public class CorrectPrimitiveNumbersTypes {
    @repair.regen.specification.Refinement("_ < i && _ > 0")
    private static double fromType(@repair.regen.specification.Refinement("_ > 0")
    int i) {
        return i * 0.1;
    }

    @repair.regen.specification.Refinement(" _ < i && _ > 0")
    private static double fromType(@repair.regen.specification.Refinement("_ > 0")
    long i) {
        return i * 0.1;
    }

    @repair.regen.specification.Refinement(" _ < i && _ > 0")
    private static double fromType(@repair.regen.specification.Refinement("_ > 0")
    short i) {
        return i * 0.1;
    }

    @repair.regen.specification.Refinement("_ > i")
    private static float twice(@repair.regen.specification.Refinement("i > 0")
    short i) {
        return i * 2.0F;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 5")
        int a = 10;
        @repair.regen.specification.Refinement("_ > 5")
        long b = 100;
        @repair.regen.specification.Refinement("_ > 5")
        short c = 10;
        @repair.regen.specification.Refinement("_ > 5")
        float d = 7.4F;
        @repair.regen.specification.Refinement("_ > 0")
        double e = repair.regen.CorrectPrimitiveNumbersTypes.fromType(a);
        e = repair.regen.CorrectPrimitiveNumbersTypes.fromType(b);
        e = repair.regen.CorrectPrimitiveNumbersTypes.fromType(c);
        @repair.regen.specification.Refinement("f > 0")
        float f = repair.regen.CorrectPrimitiveNumbersTypes.twice(c);
    }
}

