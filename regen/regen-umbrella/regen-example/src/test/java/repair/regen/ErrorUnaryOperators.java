package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorUnaryOperators {
	public static void main(String[] args) {
		@Refinement("_ < 10")
		int v = 3;
		v--;
		@Refinement("_ >= 10")
		int s = 100;
		s--;

	}
}
