package test.currentlyTesting;

import liquidjava.specification.Refinement;

class SimpleTest {

    void test1() {
        @Refinement("x > 0")
        int x = -1;
    }

    void test2() {
        @Refinement("y > 0")
        int y = -2;

        @Refinement("z > 0")
        int z = 3;
    }
}
