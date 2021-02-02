package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathMax {
	public static void main(String[] args) {
		@Refinement("\\v == 5")
		int m1 = Math.max(4, 5);
		@Refinement("\\v > 5")
		int m2 = Math.max(100, m1);
		@Refinement("\\v == 100")
		int m3 = Math.max(100, m2);
		@Refinement("\\v == -1000")
		int m4 = Math.max(-1000, -m3);

	}
}
