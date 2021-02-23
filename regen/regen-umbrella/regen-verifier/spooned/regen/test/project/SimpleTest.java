package regen.test.project;


// //@Refinement("_.length(x) >= 0") ==
// //	@Refinement("length(_, x) >= 0")
// //	int[] a1 = new int[5]; //Cannot prove - len() built-in
// K(.., ..)
// }
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
    // @Refinement("_ > 7")
    // public int v() {
    // int a = Integer.MAX_VALUE;
    // return a;
    // }
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ < 10")
        int v = 3;
        v--;
        @repair.regen.specification.Refinement("_ >= 10")
        int s = 10;
        s--;
    }
}

