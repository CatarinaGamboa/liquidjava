package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {	
	public static void main(String[] args) {
//		@Refinement("_ > 3")
//		double a2 = Math.PI;
//		@Refinement("_ > 2")
//		double b2 = Math.E;
		@Refinement("_ > 20")
		double radius = 30;
		@Refinement("perimeter > 1")
		double perimeter = 2*Math.PI*radius;
		
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