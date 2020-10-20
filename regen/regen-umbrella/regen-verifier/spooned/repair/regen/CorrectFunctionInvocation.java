package repair.regen;


public class CorrectFunctionInvocation {
    @repair.regen.specification.Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v >= 0")
        int p = 10;
        p = repair.regen.CorrectFunctionInvocation.posMult(10, 3);
        p = repair.regen.CorrectFunctionInvocation.posMult(10, (15 - 6));
    }
}

