package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {	
	
	public static void addZ(@Refinement("a > 0")int a) {
		@Refinement("_ > 0")
		int d = a;
		if(d > 5) {
			@Refinement("b > 5")
			int b = d;
		}else {
			@Refinement("_ <= 5")
			int c = d;
			d = 10;
			@Refinement("b > 10")
			int b = d;
		}
	}



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