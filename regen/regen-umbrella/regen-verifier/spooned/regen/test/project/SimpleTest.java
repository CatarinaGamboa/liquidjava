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
    public void something() {
    }

    public void searchIndex(int[] l, @repair.regen.specification.Refinement("i < len(l)")
    int i) {
        if (i >= (l.length))
            return;
        else {
            @repair.regen.specification.Refinement(" _ < len(l)")
            int i2 = i + 1;
            searchIndex(l, i2);
        }
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("length(_) == 15")
        int[] a = new int[15];
        @repair.regen.specification.Refinement("_ >= 0 && _ < length(a)")
        int index = 14;
        // @Refinement("_.length(x) >= 0") ==
        // @Refinement("length(_, x) >= 0")
        // int[] a1 = new int[5]; //Cannot prove - len() built-in
    }
}

