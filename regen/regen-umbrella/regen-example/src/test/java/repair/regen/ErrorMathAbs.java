package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathAbs {
	public static void main(String[] args) {
		@Refinement("true")
		int ab = Math.abs(-9);

		@Refinement("\\v == 9")
		int ab1 = -ab;
	}
}
