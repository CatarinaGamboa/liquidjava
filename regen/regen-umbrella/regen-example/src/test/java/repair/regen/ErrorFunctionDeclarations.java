package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorFunctionDeclarations {
	@Refinement("{d >= 0}->{i > d}->{\\v >= d && \\v < i}")
	private static int range(int d, int i) {
		return i+1;
	}
}
