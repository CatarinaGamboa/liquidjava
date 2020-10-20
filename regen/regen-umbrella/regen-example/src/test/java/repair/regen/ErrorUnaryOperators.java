package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorUnaryOperators {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int v = 3;
		v--;
		@Refinement("\\v >= 10")
		int s = 100;
		s--;

	}
}
