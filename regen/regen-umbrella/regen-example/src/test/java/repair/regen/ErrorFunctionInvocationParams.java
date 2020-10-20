package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorFunctionInvocationParams {
	@Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }

	public static void main(String[] args) {   	
    	@Refinement("\\v >= 0")
    	int p = 10;
    	p = posMult(10, 12);
	}
}
