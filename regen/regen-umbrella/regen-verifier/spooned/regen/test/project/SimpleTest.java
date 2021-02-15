package regen.test.project;


// public void searchIndex(int[] l, @Refinement("i < len(l)") int i) {
// if(i >= l.length)
// return;
// else {
// @Refinement(" _ < len(l)")
// int i2 = i+1;
// searchIndex(l, i2);
// }
// }
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
    @repair.regen.specification.RefinementFunction("ghost int len(int[])")
    public int one() {
        return 1;
    }

    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("len(a) >= 0")
        int[] a = new int[5];// Cannot prove

    }
}

