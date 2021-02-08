package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {	
	
	
	public static void main(String[] args) {
		int a = 5;
		@Refinement("b > 4")
		int b = a;
		
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