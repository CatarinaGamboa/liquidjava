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
    @repair.regen.specification.Refinement("_ < 10")
    public static int getYear() {
        return 8;
    }

    public static void main(java.lang.String[] args) {
        int a = 1998;
        regen.test.project.Car c = new regen.test.project.Car();
        c.setYear(a);
        @repair.regen.specification.Refinement("_ < 11")
        int j = regen.test.project.SimpleTest.getYear();
    }
}

