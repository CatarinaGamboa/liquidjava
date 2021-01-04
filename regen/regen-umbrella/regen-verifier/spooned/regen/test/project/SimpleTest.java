package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 5")
        int a = 10;
        @repair.regen.specification.Refinement("\\v > 10")
        int b = a + 1;
        if (b < a) {
            a = 3;
        }
    }
}

