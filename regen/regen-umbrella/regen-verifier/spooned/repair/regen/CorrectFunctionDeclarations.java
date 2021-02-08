package repair.regen;


public class CorrectFunctionDeclarations {
    @repair.regen.specification.Refinement("_ >= a")
    public static int posMult(@repair.regen.specification.Refinement("a == 10")
    int a, @repair.regen.specification.Refinement("_ < a && _ > 0")
    int b) {
        @repair.regen.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @repair.regen.specification.Refinement("_ > 10")
    public static int positive() {
        return 100;
    }

    @repair.regen.specification.Refinement("_ >= d && _ < i")
    private static int range(@repair.regen.specification.Refinement("d >= 0")
    int d, @repair.regen.specification.Refinement("i > d")
    int i) {
        return d;
    }

    @repair.regen.specification.Refinement("_ == 3 * x")
    private static int triplePositives(@repair.regen.specification.Refinement("x > 0")
    int x) {
        return (x + x) + x;
    }
}

