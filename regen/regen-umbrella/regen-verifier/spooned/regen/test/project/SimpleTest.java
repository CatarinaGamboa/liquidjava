package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v < 10")
        int a = 5;
        if (a > 0) {
            @repair.regen.specification.Refinement("b > 0")
            int b = a;
        }
        // int a = 3;
        // @Refinement("b < 10")
        // int b = a;
    }
}

