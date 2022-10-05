package liquidjava;


public class CorrectSumFunction {
    @liquidjava.specification.Refinement("_ >= 0 && _ >= n")
    public static int sum(int n) {
        if (n <= 0)
            return 0;
        else {
            int t1 = liquidjava.CorrectSumFunction.sum((n - 1));
            return n + t1;
        }
    }
}

