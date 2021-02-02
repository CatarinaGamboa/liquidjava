package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorArithmeticFP4 {
	public static void main(String[] args) {
		@Refinement("\\v > 5.0")
		double a = 5.5;

		@Refinement("\\v < -5.5")
		double d = -(a - 2.0);
	}
}
