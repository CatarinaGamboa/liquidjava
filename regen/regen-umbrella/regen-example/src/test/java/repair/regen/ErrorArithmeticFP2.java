package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorArithmeticFP2 {

	public static void main(String[] args) {
		@Refinement("\\v > 5.0")
		double a = 5.5;

		@Refinement("\\v == 10.0")
		double c = a*2.0;
	}
}
