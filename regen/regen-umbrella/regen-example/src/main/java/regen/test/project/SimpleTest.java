package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	@Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }
	@Refinement("{\\v == 10}")
    public static int ten() {
		return 10;
    }
	
	
		public static void main(String[] args) {
		
//		@Refinement("\\v < 10")
//		int a = 5;
//
//		if(a > 0) {
//			@Refinement("b > 0")
//			int b = a;
//			b++;
//			a = 10;
//
//		}
//		@Refinement("\\v < 10")
//		int a = 5;
//		if(a == 2) {
//			@Refinement("b < 5")
//			int b = a;
//			
//			@Refinement("(c % 2) == 0")
//			int c = a*5;
//		}
	}


}












