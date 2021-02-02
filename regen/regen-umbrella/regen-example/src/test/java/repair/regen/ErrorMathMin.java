package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathMin {
	public static void main(String[] args) {
		@Refinement("\\v == 4")
		int m1 = Math.min(4, 5);
		@Refinement("\\v < 5")
		int m2 = Math.min(100, m1);
		@Refinement("\\v == 4")
		int m3 = Math.min(100, m2);
		@Refinement("\\v == -1")
		int m4 = Math.min(-1, -m2);
	}
}
