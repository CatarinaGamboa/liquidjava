package repair.regen;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class ErrorArithmeticFP3 {
	public static void main(String[] args) {
		@Refinement("_ > 5.0")
		double a = 5.5;
		@Refinement("_ < -5.5")
		double d = -a;

	}
}
