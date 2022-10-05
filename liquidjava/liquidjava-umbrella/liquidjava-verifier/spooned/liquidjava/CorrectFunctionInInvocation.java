package liquidjava;


@java.lang.SuppressWarnings("unused")
public class CorrectFunctionInInvocation {
    @liquidjava.specification.Refinement("_ >= a")
    public static int posMult(@liquidjava.specification.Refinement("a == 10")
    int a, @liquidjava.specification.Refinement("_ < a && _ > 0")
    int b) {
        @liquidjava.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @liquidjava.specification.Refinement("_ == 10")
    public static int ten() {
        return 10;
    }

    @liquidjava.specification.Refinement("_ == b*2")
    private static int multTwo(int b) {
        return b * 2;
    }

    public static void main(java.lang.String[] args) {
        @liquidjava.specification.Refinement("_ >= 0")
        int p = 10;
        p = liquidjava.CorrectFunctionInInvocation.posMult(liquidjava.CorrectFunctionInInvocation.ten(), 4);
        @liquidjava.specification.Refinement("_ < 6")
        int z = 5;
        @liquidjava.specification.Refinement("_ > 6")
        int x = liquidjava.CorrectFunctionInInvocation.multTwo(z);
        @liquidjava.specification.Refinement("_ == 20")
        int y = liquidjava.CorrectFunctionInInvocation.multTwo(x);
    }
}

