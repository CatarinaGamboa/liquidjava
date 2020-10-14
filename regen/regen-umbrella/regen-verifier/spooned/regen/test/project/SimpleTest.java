package regen.test.project;


public class SimpleTest {
    @java.lang.SuppressWarnings("unused")
    public static void main(java.lang.String[] args) {
        // Original
        @repair.regen.specification.Refinement("a > 0")
        int a = 1;
        @repair.regen.specification.Refinement("b == 2 || b == 3 || b == 4")
        int b = 2;
        // @Refinement("c > 2")
        // int c = 2; // should emit error
        @repair.regen.specification.Refinement("d >= 2")
        int d = b;// should be okay

        // Arithmetic Binary Operations
        @repair.regen.specification.Refinement("t > 0")
        int t = a + 1;
        // 
        // Assignment after declaration
        @repair.regen.specification.Refinement("(z > 0) && (z < 50)")
        int z = 1;
        @repair.regen.specification.Refinement("u < 100")
        int u = 10;
        u = 11 + z;
        u = z * 2;
        u = 30 + z;
        // u = 500; //error
        // //k--
        @repair.regen.specification.Refinement("k > 0")
        int k = 1;
        k = (z + k) + (1 * k);
        // //Arithmetic operation with variable - ????
        // @Refinement("a > 0")
        // int a = 10;
        // @Refinement("t > 10")
        // int t = 2 + a;
    }
}

