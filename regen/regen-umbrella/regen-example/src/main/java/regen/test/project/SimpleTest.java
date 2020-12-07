package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	@Refinement("{a < 0 }->{\\v > 0}")
	public static int toPositive(int a) {
		return -a;
	}
	
	@Refinement("{a >= 0 }->{\\v <= 0}")
	public static int toNegative(int a) {
		return -a;
	}

	public static void main(String[] args) {

		@Refinement("\\v < 10")
		int a = 5;
//		@Refinement("\\v <= 0")
//		int c = a * (-10);	
		
		if(a < 0) {
			@Refinement("b > 0")
			int b = toPositive(a);
		}else {
				@Refinement("c < 1")
				int c = toNegative(a);	
			
//			@Refinement("\\v <= 0")
//			int c = a * -10;	
		}
	}


}












