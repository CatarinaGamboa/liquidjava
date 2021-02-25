package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementFunction;


public class SimpleTest {

//	@Refinement("_ > 1800")
//	public static int getYear() {
//		return 1856;
//	}

	public static void main(String[] args) {
		int a = 1998;
		Car c = new Car();
		c.setYear(a);

//		@Refinement("_ > 1700")
//		int j = getYear();
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//		//@Refinement("_.length(x) >= 0") ==
	////	@Refinement("length(_, x) >= 0")
	////	int[] a1 = new int[5];
	//K(.., ..)

	//	}

	////correctImplies -rever!!!
	//	@Refinement("_ > 5")
	//	int x = 10;
	//	
	//	@Refinement("(x > 50) --> (y > 50)")
	//	int y = x;


	//See error NaN
	//		@Refinement("true")
	//		double b = 0/0;
	//		@Refinement("_ > 5")
	//		double c = b;





}