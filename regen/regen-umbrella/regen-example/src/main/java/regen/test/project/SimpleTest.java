package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	public static void main(String[] args) {

//		@Refinement("\\v == 6")
//		int a = Math.abs(6);

		@Refinement("\\v < 1")
		int b = getZero();
		
		@Refinement("\\v > 0")
		int c = getOne();
		

		//		@Refinement("\\v > 0")
		//		double e = Math.sqrt(6);		
		//		@Refinement("\\v >= 0")
		//		double c = Math.random();
		//		@Refinement("b > 0")
		//		int b = Math.addExact(6, 2);



		//		b = (a < 100)? three(): three()-1;
		//		@Refinement("c < 100")
		//		int c = (a < 100)? three(): a;
		//		c = (a < 100)? three()*3 : a*5;

	}


	@Refinement("{\\v == 0}")
	private static int getZero() {
		return 0;
	}
	
	@Refinement("{\\v == 1}")
	private static int getOne() {
		@Refinement("\\v == 0")
		int a = getZero();
		return a+1;
	}

}