package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionDeclarations {
	
	@Refinement("_ >= a")
    public static int posMult(@Refinement("a == 10")int a, 
    						  @Refinement("_ < a && _ > 0" )int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }
	@Refinement("_ > 10")
    public static int positive() {
    	return 100;
    }
	
	@Refinement("_ >= d && _ < i")
	private static int range(@Refinement("d >= 0")int d, 
							 @Refinement("i > d")int i) {
		return d;
	}
	
	@Refinement("_ == 3 * x")
	private static int triplePositives(@Refinement("x > 0") int x) {
		return x+x+x;
	}
	
	@Refinement("(_ == -1) || (_ == a*b)")
	public int getPositiveMult(int a, int b) {
		int result;
		if(a > 0 && b > 0)
			result = a*b;
		else
			result = -1;
		return result;
	}

}
