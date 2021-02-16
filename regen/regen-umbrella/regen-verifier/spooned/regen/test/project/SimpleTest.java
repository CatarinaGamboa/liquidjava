package regen.test.project;


// CHECK
// @Refinement("i >= 0")
// int i = sum(10);
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
    // @RefinementFunction("ghost int length(int[])")
    // public void something() {}
    // 
    // 
    // 
    // public static void searchIndex(int[] l, @Refinement("i >= 0") int i) {
    // if(i >= l.length)
    // return;
    // else {
    // @Refinement(" _ <= length(l)")
    // int i2 = i+1;
    // searchIndex(l, i2);
    // }
    // }
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("_ == 3.141592653589793")
        double p = 0;
        @repair.regen.specification.Refinement("a.length == 15")
        int[] a = new int[15];
        // searchIndex(a, 0);
        // @Refinement("_ >= 0 && _ < length(a)")
        // int index = 14;
        // 
        // @Refinement("_.length(x) >= 0") ==
        // @Refinement("length(_, x) >= 0")
        // int[] a1 = new int[5]; //Cannot prove - len() built-in
    }
}

