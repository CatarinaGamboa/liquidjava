package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSimpleIfElse {
	
	@Refinement("_ > 0")
	public static int toPositive(@Refinement("a < 0") int a) {
		return -a;
	}
	
	@Refinement("_ < 0")
	public static int toNegative(@Refinement("a > 0")int a) {
		return -a;
	}
	
	public static void main(String[] args) {
		@Refinement("_ < 10")
		int a = 5;

		if(a < 0) {
			@Refinement("b < 0")
			int b = a;
		} else {
			@Refinement("b >= 0")
			int b = a;
		}
		
		//EXAMPLE 2
		@Refinement("_ < 10")
		int ex_a = 5;
		if(ex_a < 0) {
			@Refinement("_ >= 10")
			int ex_b = toPositive(ex_a)*10;
		}else {
			if(ex_a != 0) {
				@Refinement("_ < 0")
				int ex_d = toNegative(ex_a);
			}
			@Refinement("_ < ex_a")
			int ex_c = -10;
			
		}

	}

}
