package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {

		@Refinement("\\v > 0")
		double a = Math.abs(15.3);
		@Refinement("\\v > 10")
		long b = Math.abs(-13);
		@Refinement("\\v > 10")
		float c = Math.abs(-13f);

//				@Refinement("\\v > 0")
//				double e = Math.sqrt(6);		
//				@Refinement("\\v >= 0")
//				double c = Math.random();
//				@Refinement("b > 0")
//				int b = Math.addExact(6, 2);



		//		b = (a < 100)? three(): three()-1;
		//		@Refinement("c < 100")
		//		int c = (a < 100)? three(): a;
		//		c = (a < 100)? three()*3 : a*5;

	}


}