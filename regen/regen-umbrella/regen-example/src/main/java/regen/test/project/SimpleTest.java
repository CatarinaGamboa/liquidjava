package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	@Refinement("{true}->{\\v == b*2}")
	private static int multTwo(int b) {
		return b*2;
	}

	public static void main(String[] args) {
		@Refinement("\\v < 6")
		int z = 5;
		
		@Refinement("\\v > 6")
		int x = multTwo(z);
		
		@Refinement("\\v == 20")
		int y = multTwo(x);
		
//		@Refinement("\\v > 5")
//		int prim = 10;
//		@Refinement("\\v > 6")
//		int seg = Math.incrementExact(prim);
//		@Refinement("\\v == 12")
//		int ter = Math.incrementExact(seg);
	
		//SEE ERROR
//		@Refinement("\\v > 5")
//		int x = 10;
//		
//		@Refinement("(x > 50) --> (y > 50)")
//		int y = x;
//		
//		@Refinement("y > 1 --> z > 2")
//		int z = y*2;
//
//		@Refinement("z > 0 --> \\v > 0")
//		int z1 = getPositiveDouble(z);
//		
		
//		@Refinement("(\\v == -5)")
//		float a7 = Math.copySign(-5, -500);
//
//		@Refinement("\\v == 5")
//		float a8 = Math.copySign(-5, 6);
//		@Refinement("\\v == -656")
//		float a9 = Math.copySign(656, a7);
////		
		
		//See error NaN
//		@Refinement("\\v > 4")
//		int d = Math.abs(-6);
//
//		@Refinement("\\v == -6")
//		int e = -Math.abs(-d);
		
		//See error NaN
//		@Refinement("true")
//		double b = 0/0;
//		@Refinement("\\v > 5")
//		double c = b;




		//		b = (a < 100)? three(): three()-1;
		//		@Refinement("c < 100")
		//		int c = (a < 100)? three(): a;
		//		c = (a < 100)? three()*3 : a*5;

	}











}