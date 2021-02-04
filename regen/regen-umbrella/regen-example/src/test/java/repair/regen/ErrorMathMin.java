package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathMin {
	public static void main(String[] args) {
		@Refinement("_ == 4")
		int m1 = Math.min(4, 5);
		@Refinement("_ < 5")
		int m2 = Math.min(100, m1);
		@Refinement("_ == 4")
		int m3 = Math.min(100, m2);
		@Refinement("_ == -1")
		int m4 = Math.min(-1, -m2);
	}
}
