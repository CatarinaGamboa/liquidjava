package regen.test.project;


// @Refinement("_ > 0")
// public int fun (int[] arr) {
// return max(arr[0], 1);
// }
// 
// //@Refinement("_.length(x) >= 0") ==
// //	@Refinement("length(_, x) >= 0")
// //	int[] a1 = new int[5];
// K(.., ..)
// }
// See error NaN
// @Refinement("true")
// double b = 0/0;
// @Refinement("_ > 5")
// double c = b;
public class SimpleTest {
    public static void main(java.lang.String[] args) {
        @repair.regen.specification.Refinement("a == 10")
        int a = 10;
        @repair.regen.specification.Refinement("b != 10")
        int b = 5;
        @repair.regen.specification.Refinement("t > 0")
        int t = a + 1;
        @repair.regen.specification.Refinement("_ >= 9")
        int k = a - 1;
        @repair.regen.specification.Refinement("_ >= 5")
        int l = k * t;
        @repair.regen.specification.Refinement("_ > 0")
        int m = l / 2;
        // Email e = new Email();
        // e.from("me");
        // e.to("you");
        // ...
    }
}

