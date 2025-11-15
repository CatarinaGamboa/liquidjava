package testMultiple;

import liquidjava.specification.Refinement;

class MultipleErrorsExample {

    void test1() {
        @Refinement("a > 0")
        int a = -1;
    }

    void test2() {
        @Refinement("b > 0")
        int b = -2;

        @Refinement("c > 0")
        int c = 3;
    }
}
