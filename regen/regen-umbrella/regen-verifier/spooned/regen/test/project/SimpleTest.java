package regen.test.project;


public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("\\v > 10")
        int a = 15;
        if (a > 14) {
            a = 12;
            @repair.regen.specification.Refinement("\\v < 11")
            int c = a;
        }
        // @Refinement("(z > 0) && (z < 50)")
        // int z = 1;
        // @Refinement("u < 100")
        // int u = 10;
        // u = 11 + z;
        // u = z*2;
        // u = 30 + z;
        // @Refinement("\\v > 0")
        // int n = 1;
        // n = z + n + 1 * n;
        // @Refinement("y > 0")
        // int y = 15;
        // y = y*y;
    }
}

