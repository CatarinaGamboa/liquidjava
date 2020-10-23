package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionInInvocation {
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
    	@Refinement("\\v >= 0")
    	int p = 10;
    	p = posMult(ten(), 4);
	}
}
