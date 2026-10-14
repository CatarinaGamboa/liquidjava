package testSuite;

import liquidjava.specification.Refinement;

public class ErrorEmptyVarargsCall {

    static int first(@Refinement("length(xs) > 0") int... xs) {
        return xs[0];
    }

    public static void main(String[] args) {
        int a = first(); // Expect: Refinement Error
    }
}
