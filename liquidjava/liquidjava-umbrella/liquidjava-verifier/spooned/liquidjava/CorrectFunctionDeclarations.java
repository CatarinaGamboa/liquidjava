package liquidjava;


public class CorrectFunctionDeclarations {
    @liquidjava.specification.Refinement("_ >= a")
    public static int posMult(@liquidjava.specification.Refinement("a == 10")
    int a, @liquidjava.specification.Refinement("_ < a && _ > 0")
    int b) {
        @liquidjava.specification.Refinement("y > 30")
        int y = 50;
        return y - 10;
    }

    @liquidjava.specification.Refinement("_ > 10")
    public static int positive() {
        return 100;
    }

    @liquidjava.specification.Refinement("_ >= d && _ < i")
    private static int range(@liquidjava.specification.Refinement("d >= 0")
    int d, @liquidjava.specification.Refinement("i > d")
    int i) {
        return d;
    }

    @liquidjava.specification.Refinement("_ == 3 * x")
    private static int triplePositives(@liquidjava.specification.Refinement("x > 0")
    int x) {
        return (x + x) + x;
    }

    @liquidjava.specification.Refinement("(_ == -1) || (_ == a*b)")
    public int getPositiveMult(int a, int b) {
        int result;
        if ((a > 0) && (b > 0))
            result = a * b;
        else
            result = -1;

        return result;
    }
}

