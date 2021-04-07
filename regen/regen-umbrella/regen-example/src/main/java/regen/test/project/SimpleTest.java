package regen.test.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;

import bufferedreader.Account;
import bufferedreader.Order;
import bufferedreader.OrderSimple;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateSet;

//@RefinementAlias("type Percentage(int x) { 0 <= x && x >= 100}")
public class SimpleTest {
	
	
//	@RefinementPredicate("int length(int[] a)")
	public static void addZ(@Refinement("a > 0")int a) {
//		@Refinement("_ > 0")
//		int d = a;
//		if(d > 5) {
//			@Refinement("b > 5")
//			int b = d;
//		}else {
//			@Refinement("_ <= 5")
//			int c = d;
//			d = 10;
//			@Refinement("b > 10")
//			int b = d;
//		}
	}
	
	public static void main(String[] args) throws IOException{
		
		
//		@Refinement("_ > 10")
//		int i = 10;

		
//		@Refinement("sum(_) > 30")
//		Account a = new Account(50);
//		a.deposit(60);

//		Account b = new Account(138);
//		b = a.transferTo(b, 10);
//		
//		@Refinement("sum(_) == 148")
//		Account c = b;
		
		
//		Order o = new Order();
//		
//		Order f = o.addItem("shirt", 60).addGift();
//				.getNewOrderPayThis().addItem("t", 6).addItem("t", 1);
//		o.addGift();
//		f.addItem("l", 1).pay(000).addGift().pay(000);//.addTransportCosts().sendToAddress("home");

		
//		TrafficLight tl = new TrafficLight();
//		tl.transitionToAmber();
//		
		
//		TrafficLight tl2 = tl.getTrafficLightStartingRed();
//		tl2.transitionToFlashingAmber();
		
		
//		tl.transitionToAmber();
//		tl.transitionToAmber();
		
//		tl.passagersCross();
//		tl.intermitentMalfunction();
		
//		tl.transitionToFlashingAmber();

//		@Refinement("size(al) < 4")
//		ArrayList<Integer> al = new ArrayList<Integer>();
//		al.add(1);
//		al.add(1);
//		al.get(2);
		
		
//		@Refinement("size(t) == 3")
//		ArrayList<Integer> t = al;
		
		
//		
//		Order o = new Order();
//		o.addItem("shirt", 5)
//		 .addItem("shirt", 10)
//		 .addItem("h", 20)
//		 .addItem("h", 6);

	}
	
	
	
	
	
//	InputStreamReader isr = new InputStreamReader(System.in);
//	isr.read();
//	isr.read();
//	isr.read();
//	isr.close();
//	
//	//...
//	isr.read();

	




	//	@Refinement("_ > 0")
	//	public int fun (int[] arr) {
	//		return max(arr[0], 1);
	//	}
	//	



	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5];
	//K(.., ..)

	//	}


	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;





}