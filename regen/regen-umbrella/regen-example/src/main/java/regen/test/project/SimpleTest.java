package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	public static void main(String[] args) {
		
	

		@Refinement("b > 0")
		int b = Math.abs(6);
		
		@Refinement("\\v >= 0")
		double c = Math.random();
//		@Refinement("b > 0")
//		int b = Math.addExact(6, 2);
		
		
		
		//		b = (a < 100)? three(): three()-1;
		//		@Refinement("c < 100")
		//		int c = (a < 100)? three(): a;
		//		c = (a < 100)? three()*3 : a*5;

	}



	//		@Refinement("\\v < 10")
	//		int a = 6;
	//		
	//		if(a > 3) {
	//			a = 7 + 1;
	//		}//else {
	//			@Refinement("b > 8")
	//			int b = a;
	//		
	//		}
	//		@Refinement("b > 8")
	//		int b = a;


}

