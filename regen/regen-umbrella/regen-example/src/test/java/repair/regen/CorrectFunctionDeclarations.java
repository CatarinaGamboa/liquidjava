package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionDeclarations {
	
	@Refinement("{a == 10} -> {\\v < a && \\v > 0} -> {\\v >= a}")
    public static int posMult(int a, int b) {
		@Refinement("y > 30")
		int y = 50;
    	return y-10;
    }
	@Refinement("{\\v > 10}")
    public static int positive() {
    	return 100;
    }
	
	@Refinement("{d >= 0}->{i > d}->{\\v >= d && \\v < i}")
	private static int range(int d, int i) {
		return d;
	}
	
	@Refinement("{x > 0} -> {\\v == 3 * x}")
	private static int triplePositives(int x) {
		return x+x+x;
	}

}
