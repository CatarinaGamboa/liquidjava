package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorArithmeticFP1 {

	public static void main(String[] args) {
		@Refinement("_ > 5.0")
		double a = 5.0;
	}
}
