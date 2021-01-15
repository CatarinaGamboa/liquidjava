package repair.regen;


public class CorrectPrimitiveNumbersTypes {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 5")
        int a = 10;
        @repair.regen.specification.Refinement("\\v > 5")
        long b = 100;
        @repair.regen.specification.Refinement("\\v > 5")
        short c = 10;
        @repair.regen.specification.Refinement("\\v > 5")
        float d = 7.4F;
        @repair.regen.specification.Refinement("\\v > 0")
        double e = repair.regen.CorrectPrimitiveNumbersTypes.fromType(a);
        e = repair.regen.CorrectPrimitiveNumbersTypes.fromType(b);
        e = repair.regen.CorrectPrimitiveNumbersTypes.fromType(c);
        @repair.regen.specification.Refinement("f > 0")
        float f = repair.regen.CorrectPrimitiveNumbersTypes.twice(c);
    }

    @repair.regen.specification.Refinement("{\\v > 0}->{\\v < i && \\v > 0}")
    private static double fromType(int i) {
        return i * 0.1;
    }

    @repair.regen.specification.Refinement("{\\v > 0}->{\\v < i && \\v > 0}")
    private static double fromType(long i) {
        return i * 0.1;
    }

    @repair.regen.specification.Refinement("{\\v > 0}->{\\v < i && \\v > 0}")
    private static double fromType(short i) {
        return i * 0.1;
    }

    @repair.regen.specification.Refinement("{i > 0}->{\\v > i}")
    private static float twice(short i) {
        return i * 2.0F;
    }
}

