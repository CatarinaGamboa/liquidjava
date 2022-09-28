package repair.regen;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class ErrorArithmeticBinaryOperations {
	public static void main(String[] args) {
		@Refinement("_ < 100")
		int y = 50;
		@Refinement("_ > 0")
		int z = y - 51;
	}
}
