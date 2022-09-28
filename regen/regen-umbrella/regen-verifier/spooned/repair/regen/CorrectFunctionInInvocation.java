package repair.regen;


@java.lang.SuppressWarnings("unused")
public class CorrectFunctionInInvocation {
    @repair.regen.specification.Refinement("_ >= a")
    public static int posMult(@repair.regen.specification.Refinement("a == 10")
    int a, @repair.regen.specification.Refinement("_ < a && _ > 0")
    int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @repair.regen.specification.Refinement("_ == 10")
    public static int ten() {
        return 10;
    }

    @repair.regen.specification.Refinement("_ == b*2")
    private static int multTwo(int b) {
        return b * 2;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ >= 0")
        int p = 10;
        p = repair.regen.CorrectFunctionInInvocation.posMult(repair.regen.CorrectFunctionInInvocation.ten(), 4);
        @repair.regen.specification.Refinement("_ < 6")
        int z = 5;
        @repair.regen.specification.Refinement("_ > 6")
        int x = repair.regen.CorrectFunctionInInvocation.multTwo(z);
        @repair.regen.specification.Refinement("_ == 20")
        int y = repair.regen.CorrectFunctionInInvocation.multTwo(x);
    }
}

