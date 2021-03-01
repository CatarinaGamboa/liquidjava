package regen.test.project;


// @RefinementFunction("ghost int length(int[])")
// @Refinement("(_ >= -1) && (_ < length(l))")
// public static int getIndexWithValue(  @Refinement("length(l) > 0") int[] l,
// @Refinement("i >= 0 && i < length(l)") int i,
// int val) {
// if(l[i] == val)
// return i;
// if(i >= l.length - 1)
// return -1;
// else
// return getIndexWithValue(l, i+1, val);
// }
// int[] arr = new int[10+6];
// getIndexWithValue(arr, 0, 1000);
// //@Refinement("_.length(x) >= 0") ==
// //	@Refinement("length(_, x) >= 0")
// //	int[] a1 = new int[5];
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
@repair.regen.specification.RefinementAlias("PTgrade(int x) {x >= 0 && x <= 20}")
public class SimpleTest {
    public static void main(java.lang.String[] args) {
        int[] a = new int[10 + 3];
        @repair.regen.specification.Refinement("length(b) > 5")
        int[] b = a;
    }
}

