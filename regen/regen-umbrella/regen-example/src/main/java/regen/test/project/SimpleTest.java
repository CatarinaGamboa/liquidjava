package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {	
	public static void main(String[] args) {

		//Math.abs(...)
		@Refinement("b > 0")
		int b = Math.abs(6);
		@Refinement("_ == 6")
		int a = Math.abs(6);
		@Refinement("_ > 4")
		int d = Math.abs(-6);
		@Refinement("_ == -6")
		int e = -Math.abs(-d);
		
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