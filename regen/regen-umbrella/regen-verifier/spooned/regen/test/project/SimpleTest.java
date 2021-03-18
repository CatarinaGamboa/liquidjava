package regen.test.project;


// public static void main(String[] args) throws IOException{
// 
// TrafficLight tl = new TrafficLight();
// tl.transitionToAmber();
// //		tl.transitionToFlashingAmber();
// 
// //		@Refinement("size(al) < 4")
// //		ArrayList<Integer> al = new ArrayList<Integer>();
// //		al.add(1);
// //		al.add(1);
// //		al.get(2);
// 
// 
// //		@Refinement("size(t) == 3")
// //		ArrayList<Integer> t = al;
// 
// 
// //
// //		Order o = new Order();
// //		o.addItem("shirt", 5)
// //		 .addItem("shirt", 10)
// //		 .addItem("h", 20)
// //		 .addItem("h", 6);
// 
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
    // @Refinement("_ >= 0 && _ >= n")
    // public static int sum(int n) {
    // if(n <= 0)
    // return 0;
    // else {
    // int t1 = sum(n-1);
    // return n + t1;
    // }
    // }
    // 
    // @Refinement("_ >= 0 && _ >= n")
    // public static int absolute(int n) {
    // if(0 <= n)
    // return n;
    // else
    // return 0 - n;
    // }
    // From LiquidHaskell tutorial
    @repair.regen.specification.Refinement("length(_) == length(vec1)")
    static int[] sumVectors(int[] vec1, @repair.regen.specification.Refinement("length(vec1) == length(vec2)")
    int[] vec2) {
        int[] add = new int[vec1.length];
        if ((vec1.length) > 0)
            regen.test.project.SimpleTest.auxSum(add, vec1, vec2, 0);

        return add;
    }

    private static void auxSum(int[] addP, int[] vec1P, @repair.regen.specification.Refinement("length(vec1P) == length(_) && length(_) == length(addP)")
    int[] vec2P, @repair.regen.specification.Refinement("_ >= 0 && _ < length(vec2P)")
    int iP) {
        addP[iP] = (vec1P[iP]) + (vec2P[iP]);
        if (iP < ((addP.length) - 1))
            regen.test.project.SimpleTest.auxSum(addP, vec1P, vec2P, (iP + 1));

    }
}

