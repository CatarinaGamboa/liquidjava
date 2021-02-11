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
    // @Refinement("(a > 0)? (_ == a): (_ == -a)")
    // public static int abso(int a) {
    // if( a > 0)
    // return a;
    // else
    // return -a;
    // }
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("b == -8")
        double b = java.lang.Math.copySign(8, (-6));
        // @Refinement("(4 > 8)? ( _ == 7):( _ == 10)")
        // int a = 10;
    }
}

