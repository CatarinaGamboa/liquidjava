package repair.regen;


public class ErrorFunctionInvocation {
    @repair.regen.specification.Refinement("{a == 10} -> {_ < a && _ > 0} -> {_ >= a}")
    public static int posMult(int a, int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ > 10")
        int p = 10;
        p = repair.regen.ErrorFunctionInvocation.posMult(10, 4);
    }
}

