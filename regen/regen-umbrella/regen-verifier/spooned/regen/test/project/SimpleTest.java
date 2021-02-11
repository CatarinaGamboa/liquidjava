package regen.test.project;


// //correctImplies -rever!!!
// @Refinement("_ > 5")
// int x = 10;
// 
// @Refinement("(x > 50) --> (y > 50)")
// int y = x;
// See error NaN
// @Refinement("true")
// double b = 0/0;
// @Refinement("_ > 5")
// double c = b;
public class SimpleTest {
    // @RefinementFunction("ghost len")
    // public static int seven() {
    // return 7;
    // }
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("(4 > 3)? ( _ == 7):( _ == 10)")
        int a = 10;
    }
}

