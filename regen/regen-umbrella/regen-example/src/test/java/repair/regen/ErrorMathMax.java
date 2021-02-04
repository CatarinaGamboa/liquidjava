package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathMax {
	public static void main(String[] args) {
		@Refinement("_ == 5")
		int m1 = Math.max(4, 5);
		@Refinement("_ > 5")
		int m2 = Math.max(100, m1);
		@Refinement("_ == 100")
		int m3 = Math.max(100, m2);
		@Refinement("_ == -1000")
		int m4 = Math.max(-1000, -m3);

	}
}
