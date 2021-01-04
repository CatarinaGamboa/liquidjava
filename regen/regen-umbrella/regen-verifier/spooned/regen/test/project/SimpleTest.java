package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 15;
        if (a > 14) {
            a = 9;
            // @Refinement("\\v < 11")
            // int c = a;
        }
    }
}

