package regen.test.project;


public class SimpleTest {
    @java.lang.SuppressWarnings("unused")
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a > 0")
        int a = 0 + 1;
        // a--;
        @repair.regen.specification.Refinement("b == 2 || b == 3 || b == 4")
        int b = 2;
        // @Refinement("c > 2")
        // int c = 2; // should emit error
        @repair.regen.specification.Refinement("d >= 2")
        int d = b;// should be okay

    }
}

