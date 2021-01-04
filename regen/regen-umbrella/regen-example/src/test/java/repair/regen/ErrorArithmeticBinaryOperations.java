package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorArithmeticBinaryOperations {
	public static void main(String[] args) {
		@Refinement("\\v < 100")
		int y = 50;
		@Refinement("\\v > 0")
		int z = y - 51;
	}
}
