package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSimpleIfElse {
	
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
		int a = 5;

		if(a < 0) {
			@Refinement("b < 0")
			int b = a;
		} else {
			@Refinement("b >= 0")
			int b = a;
		}
		
		//EXAMPLE 2
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

	}

}
