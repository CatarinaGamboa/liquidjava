package testSuite;

import liquidjava.specification.Refinement;

public class ErrorVarargsCall {

    static int pick(@Refinement("k >= 0") int k, int... xs) {
        return k;
    }

    public static void main(String[] args) {
        int i = pick(-1, 2, 3); // Expect: Refinement Error
    }
}
