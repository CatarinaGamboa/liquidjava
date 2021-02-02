package repair.regen;


public class CorrectFunctionInInvocation {
    @repair.regen.specification.Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @repair.regen.specification.Refinement("{\\v == 10}")
    public static int ten() {
        return 10;
    }

    @repair.regen.specification.Refinement("{true}->{\\v == b*2}")
    private static int multTwo(int b) {
        return b * 2;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v >= 0")
        int p = 10;
        p = repair.regen.CorrectFunctionInInvocation.posMult(repair.regen.CorrectFunctionInInvocation.ten(), 4);
        @repair.regen.specification.Refinement("\\v < 6")
        int z = 5;
        @repair.regen.specification.Refinement("\\v > 6")
        int x = repair.regen.CorrectFunctionInInvocation.multTwo(z);
        @repair.regen.specification.Refinement("\\v == 20")
        int y = repair.regen.CorrectFunctionInInvocation.multTwo(x);
    }
}

