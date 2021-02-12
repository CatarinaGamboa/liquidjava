package repair.regen;


public class CorrectSumFunction {
    @repair.regen.specification.Refinement("_ >= 0 && _ >= n")
    public static int sum(int n) {
        if (n <= 0)
            return 0;
        else {
            int t1 = repair.regen.CorrectSumFunction.sum((n - 1));
            return n + t1;
        }
    }
}

