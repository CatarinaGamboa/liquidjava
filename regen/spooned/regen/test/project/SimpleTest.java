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
        regen.test.project.Order f = new regen.test.project.Order();
        // needs > 20 in totalPrice
        f.addItem("shirt", 1).pay(0).addGift().sendToAddress("home");
        // @Refinement("_ > 10")
        // int i = 10;
        // @Refinement("sum(_) > 30")
        // Account a = new Account(50);
        // a.deposit(60);
        // Account b = new Account(138);
        // b = a.transferTo(b, 10);
        // 
        // @Refinement("sum(_) == 148")
        // Account c = b;
        // Order o = new Order();
        // 
        // Order f = o.addItem("shirt", 60).addGift();
        // .getNewOrderPayThis().addItem("t", 6).addItem("t", 1);
        // o.addGift();
        // f.addItem("l", 1).pay(000).addGift().pay(000);//.addTransportCosts().sendToAddress("home");
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
