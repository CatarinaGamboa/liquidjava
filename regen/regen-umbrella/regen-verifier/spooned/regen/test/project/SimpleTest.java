package regen.test.project;


// public static void main(String[] args) throws IOException{
// Bus b = new Bus();
// b.setYear(1500);
// }
// InputStreamReader isr = new InputStreamReader(System.in);
// isr.read();
// isr.read();
// isr.read();
// isr.close();
// 
// //...
// isr.read();
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
    @repair.regen.specification.Refinement("(a > b)? (_ == a):( _ == b)")
    public static int max(int a, int b) {
        if (a > b)
            return a;
        else
            return b;

    }

    @repair.regen.specification.Refinement("_ > 1")
    public int fun(int[] arr) {
        return regen.test.project.SimpleTest.max(arr[0], 1);
    }
}

