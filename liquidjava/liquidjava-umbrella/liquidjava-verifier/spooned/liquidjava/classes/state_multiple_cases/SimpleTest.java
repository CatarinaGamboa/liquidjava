package liquidjava.classes.state_multiple_cases;


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
@java.lang.SuppressWarnings("unused")
public class SimpleTest {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        java.io.InputStreamReader isr = new java.io.InputStreamReader(java.lang.System.in);
        @liquidjava.specification.Refinement("a > -90")
        int a = isr.read();
        isr.close();
        // isr.close();
    }
}
