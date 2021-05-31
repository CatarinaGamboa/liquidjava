package sum;


import sum.Test;

import static sum.Test.sum;


@repair.regen.specification.RefinementAlias("Nat(int x) {x >= 0}")
public class Test {
    /**
     * The sum of all numbers between 0 and n
     *
     * @param n
     * 		
     * @return a positive value that represents the sum of all numbers between 0 and n
     */
    @repair.regen.specification.Refinement("Nat(_) && _ >= n")
    public static int sum(int n) {
        // correct: 0
        if (n <= 0)
            return 0;
        else {
            int t1 = sum((n - 1));
            return n + t1;
        }
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 10")
        int a = 10;
    }
}

