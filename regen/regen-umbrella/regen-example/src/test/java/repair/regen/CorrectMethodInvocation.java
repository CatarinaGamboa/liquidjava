package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectMethodInvocation {

	@Refinement("{\\v == 2}")
	private static int getTwo() {
		return 1+1;
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
	
	public static void main(String[] args) {
		@Refinement("\\v < 1")
		int b = getZero();
		
		@Refinement("\\v > 0")
		int c = getOne();
		c = getTwo();
	}




}
