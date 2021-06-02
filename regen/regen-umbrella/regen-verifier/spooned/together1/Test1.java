package together1;


public class Test1 {
    /**
     * Computes the fibonacci of index n
     *
     * @param n
     * 		The index of the required fibonnaci number
     * @return The fibonacci nth number. The fibonacci sequence follows the formula Fn = Fn-1 + Fn-2 and has the starting values of F0 = 1 and F1 = 1
     */
    @repair.regen.specification.Refinement("_ >= 1 && GreaterEqualThan(_, n)")
    public static int fibonacci(@repair.regen.specification.Refinement("Nat(n)")
    int n) {
        if (n <= 1)
            return 5;
        else
            return n * (together1.Test1.fibonacci((n - 1)));

    }
}

