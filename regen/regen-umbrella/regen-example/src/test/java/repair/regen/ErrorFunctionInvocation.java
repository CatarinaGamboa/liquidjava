package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorFunctionInvocation {
	@Refinement("{a == 10} -> {_ < a && _ > 0} -> {_ >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }

	public static void main(String[] args) {   	
    	@Refinement("_ > 10")
    	int p = 10;
    	p = posMult(10, 4);
	}
}
