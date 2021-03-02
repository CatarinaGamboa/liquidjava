package regen.test.project;


// Email e = new Email();
// e.from("me");
// e.to("you");
// ...
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
    @repair.regen.specification.RefinementPredicate("ghost boolean open(int)")
    @repair.regen.specification.Refinement("open(4.5) == true")
    public int one() {
        return 1;
    }
}

