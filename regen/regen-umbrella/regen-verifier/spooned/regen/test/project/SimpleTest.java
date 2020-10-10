package regen.test.project;


public class SimpleTest {
    @java.lang.SuppressWarnings("unused")
    public static void main(java.lang.String[] args) {
        // @Refinement("a > 0")
        // int a = 10;
        // @Refinement("t > 0")
        // int t = a + 1; //Missing info on a (a>0 and a == 10) -> Rule
        // @Refinement("t < 100")
        // int u;
        // u = 10;
        @repair.regen.specification.Refinement("b == 2 || b == 3 || b == 4")
        int b = 2;
        // @Refinement("c > 2")
        // int c = 2; // should emit error
        @repair.regen.specification.Refinement("d >= 2")
        int d = b;// should be okay

        @repair.regen.specification.Refinement("a > 0")
        int a = 10;
        @repair.regen.specification.Refinement("t > 2")
        int t = 2 + b;
    }
}

