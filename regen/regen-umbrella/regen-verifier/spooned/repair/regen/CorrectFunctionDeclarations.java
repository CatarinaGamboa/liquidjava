package repair.regen;


public class CorrectFunctionDeclarations {
    @repair.regen.specification.Refinement("{a == 10} -> {_ < a && _ > 0} -> {_ >= a}")
    public static int posMult(int a, int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @repair.regen.specification.Refinement("{_ > 10}")
    public static int positive() {
        return 100;
    }

    @repair.regen.specification.Refinement("{d >= 0}->{i > d}->{_ >= d && _ < i}")
    private static int range(int d, int i) {
        return d;
    }

    @repair.regen.specification.Refinement("{x > 0} -> {_ == 3 * x}")
    private static int triplePositives(int x) {
        return (x + x) + x;
    }
}

