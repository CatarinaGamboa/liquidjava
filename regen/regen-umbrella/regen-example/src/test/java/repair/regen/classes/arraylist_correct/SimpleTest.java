package repair.regen.classes.arraylist_correct;

import java.io.IOException;
import java.util.ArrayList;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	public static void main(String[] args) throws IOException{

		@Refinement("size(al) < 4")
		ArrayList<Integer> al = new ArrayList<Integer>();
		al.add(1);
		al.add(1);
		al.add(1);
		
		
		@Refinement("size(t) == 3")
		ArrayList<Integer> t = al;
		
		
		
//		Order o = new Order();
//		o.addItem("shirt", 5)
//		 .addItem("shirt", 10)
//		 .addItem("h", 20)
//		 .hasThree();
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