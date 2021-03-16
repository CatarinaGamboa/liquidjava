package repair.regen.classes.arraylist_correct;


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
    public static void main(java.lang.String[] args) throws java.io.IOException {
        @repair.regen.specification.Refinement("size(al) < 4")
        java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<java.lang.Integer>();
        al.add(1);
        al.add(1);
        al.add(1);
        @repair.regen.specification.Refinement("size(t) == 3")
        java.util.ArrayList<java.lang.Integer> t = al;
        // Order o = new Order();
        // o.addItem("shirt", 5)
        // .addItem("shirt", 10)
        // .addItem("h", 20)
        // .hasThree();
        // .addItem("h", 6);
    }
}

