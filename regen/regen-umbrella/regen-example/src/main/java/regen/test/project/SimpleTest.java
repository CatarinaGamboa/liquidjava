package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	@Refinement("{a < 0 }->{\\v > 0}")
	public static int toPositive(int a) {
		return -a;
	}

	@Refinement("{a > 0 }->{\\v < 0}")
	public static int toNegative(int a) {
		return -a;
	}

	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int ex_a = 5;
		if(ex_a < 0) {
			@Refinement("\\v >= 10")
			int ex_b = toPositive(ex_a)*10;
		}else {
			if(ex_a != 0) {
				@Refinement("\\v < 0")
				int ex_d = toNegative(ex_a);
			}
			@Refinement("\\v < ex_a")
			int ex_c = -10;
			
		}

		//		OTHER ERROR: if(!(a == 0))...
		//		//SHOULD BE ERROR -inconsitency a < 0 and a == 100 -> False prove->True 
		//		@Refinement("\\v < 10")
		//		int a = 5;
		//		if(a < 0) 
		//			a = 100;

	}


}












