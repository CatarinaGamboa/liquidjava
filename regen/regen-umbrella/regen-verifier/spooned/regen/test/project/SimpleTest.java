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
    // @RefinementFunction("ghost int length(int[])")
    // public void something() {}
    // public static void searchIndex(@Refinement("length(l) > 0")int[] l,
    // @Refinement("i >= 0 && i <= length(l)") int i) {
    // if(i >= l.length)
    // return;
    // else
    // searchIndex(l, i+1);
    // }
    // @Refinement("_ >= -1 && _ < length(l)")
    public static int getIndexWithVal(@repair.regen.specification.Refinement("length(l) > 0")
    int[] l, @repair.regen.specification.Refinement("i >= 0 && i <= length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        if (i >= (l.length))
            return -1;
        else
            return regen.test.project.SimpleTest.getIndexWithVal(l, (i + 1), val);

    }

    public static void main(java.lang.String[] args) {
        // @Refinement("length(a) >= 0")
        int[] a = new int[0];// Remove comments predicate

        regen.test.project.SimpleTest.getIndexWithVal(a, 0, 6);
        // 
        // //@Refinement("_.length(x) >= 0") ==
        // //		@Refinement("length(_, x) >= 0")
        // //		int[] a1 = new int[5]; //Cannot prove - len() built-in
    }
}

