package repair.regen;


public class CorrectFunctionsTutorial {
    @repair.regen.specification.Refinement("_ >= 0 && _ >= n")
    public static int sum(int n) {
        if (n <= 0)
            return 0;
        else {
            int t1 = repair.regen.CorrectFunctionsTutorial.sum((n - 1));
            return n + t1;
        }
    }

    @repair.regen.specification.Refinement("_ >= 0 && _ >= n")
    public static int absolute(int n) {
        if (0 <= n)
            return n;
        else
            return 0 - n;

    }
}

