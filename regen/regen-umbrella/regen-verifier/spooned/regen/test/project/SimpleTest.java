package regen.test.project;


// public static void main(String[] args) {
// int[] a = new int[10];
// getIndexWithValue(a, 0, max);
// getIndexWithValue(a, a.length, max);
// a = new int[0];
// getIndexWithValue(a, 0, 6);
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
    // @RefinementFunction("ghost int length(int[])")
    // @Refinement("(_ >= -1) && (_ < length(l))")
    // public static int getIndexWithValue(  @Refinement("length(l) > 0") int[] l,
    // @Refinement("i >= 0 && i < length(l)") int i,
    // int val) {
    // int result;
    // if(l[i] == val)
    // result = i;
    // else if(i >= l.length-1)
    // result = -1;
    // else
    // result = getIndexWithValue(l, i+1, val);
    // return result;
    // }
    @repair.regen.specification.Refinement("_ > 0")
    public static int toPositive(@repair.regen.specification.Refinement("a < 0")
    int a) {
        return -a;
    }

    @repair.regen.specification.Refinement("_ < 0")
    public static int toNegative(@repair.regen.specification.Refinement("a > 0")
    int a) {
        return -a;
    }

    public static void main(java.lang.String[] args) {
        // //EXAMPLE 2
        // @Refinement("_ < 10")
        // int ex_a = 5;
        // if(ex_a < 0) {
        // @Refinement("_ >= 10")
        // int ex_b = toPositive(ex_a)*10;
        // }else {
        // if(ex_a != 0) {
        // @Refinement("_ < 0")
        // int ex_d = toNegative(ex_a);
        // }
        // @Refinement("_ < ex_a")
        // int ex_c = -10;
        // }
    }

    public void have2(int a, int b) {
        @repair.regen.specification.Refinement("pos > 0")
        int pos = 10;
        if (a > 0) {
            if (a > b)
                pos = a - b;

        }
    }
}

