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
    @repair.regen.specification.RefinementFunction("ghost int length(int[])")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ < length(l))")
    public static int getIndexWithValue(@repair.regen.specification.Refinement("length(l) > 0")
    int[] l, @repair.regen.specification.Refinement("i >= 0 && i < length(l)")
    int i, int val) {
        if ((l[i]) == val)
            return i;

        // with or without -1
        if (i >= ((l.length) - 1))
            return -1;
        else
            return regen.test.project.SimpleTest.getIndexWithValue(l, (i + 1), val);

    }

    public static void main(java.lang.String[] args) {
        int max = java.lang.Integer.MAX_VALUE;
        int[] a = new int[10];
        regen.test.project.SimpleTest.getIndexWithValue(a, 0, max);
        regen.test.project.SimpleTest.getIndexWithValue(a, a.length, max);
        // a = new int[0];
        // getIndexWithValue(a, 0, 6);
        // //@Refinement("_.length(x) >= 0") ==
        // //	@Refinement("length(_, x) >= 0")
        // //	int[] a1 = new int[5]; //Cannot prove - len() built-in
        // K(.., ..)
    }
}

