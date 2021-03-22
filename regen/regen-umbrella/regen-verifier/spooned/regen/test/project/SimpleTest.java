package regen.test.project;


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
        // Order o = new Order();
        // o.addItem("shirt", 10);
        // o.addItem("shirt2", 15);
        // o.pay(00000000);
        // o.addGift();
        // o.sendToAddress("my home");
        regen.test.project.Order o = new regen.test.project.Order();
        o.addItem("shirt", 10);// .pay(00000).getNewOrder().addItem("shirt", 5);

        // TrafficLight tl = new TrafficLight();
        // tl.transitionToAmber();
        // 
        // TrafficLight tl2 = tl.getTrafficLightStartingRed();
        // tl2.transitionToFlashingAmber();
        // tl.transitionToAmber();
        // tl.transitionToAmber();
        // tl.passagersCross();
        // tl.intermitentMalfunction();
        // tl.transitionToFlashingAmber();
        // @Refinement("size(al) < 4")
        // ArrayList<Integer> al = new ArrayList<Integer>();
        // al.add(1);
        // al.add(1);
        // al.get(2);
        // @Refinement("size(t) == 3")
        // ArrayList<Integer> t = al;
        // 
        // Order o = new Order();
        // o.addItem("shirt", 5)
        // .addItem("shirt", 10)
        // .addItem("h", 20)
        // .addItem("h", 6);
    }
}

