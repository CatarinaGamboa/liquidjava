package regen.test.project;


public class SimpleTest {
    @repair.regen.specification.Refinement("{a < 10} -> {b > a} -> {\\v > 10}")
    public static int posMult(int a, int b) {
        return a * b;
    }

    @java.lang.SuppressWarnings("unused")
    public static void main(java.lang.String[] args) {
        // Original
        // @Refinement("a > 0")
        // int a = 1;
        // 
        // 
        // @Refinement("b == 2 || b == 3 || b == 4")
        // int b = 2;
        // 
        // //		@Refinement("c > 2")
        // //    	int c = 2; // should emit error
        // 
        // 
        // @Refinement("d >= 2")
        // int d = b; // should be okay
        // 
        // //Arithmetic Binary Operations
        // @Refinement("t > 0")
        // int t = a + 1;
        // //
        // //Assignment after declaration
        // @Refinement("(z > 0) && (z < 50)")
        // int z = 1;
        // @Refinement("u < 100")
        // int u = 10;
        // u = 11 + z;
        // u = z*2;
        // u = 30 + z;
        // //    	u = 500; //error
        // //k--
        // @Refinement("k > 0")
        // int k = 1;
        // k = z + k + 1 * k;
        @repair.regen.specification.Refinement("\\v >= 0")
        int p = 10;
        p = regen.test.project.SimpleTest.posMult(7, 15);
        // //Arithmetic operation with variable - ????
        // @Refinement("a > 0")
        // int a = 10;
        // @Refinement("t > 10")
        // int t = 2 + a;
    }
}

