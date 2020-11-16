package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSimpleAssignment {
	public static void main(String[] args) {
		@Refinement("a > 0")
		int a = 1;

		@Refinement("b == 2 || b == 3 || b == 4")
		int b = 2;

		@Refinement("d >= 2")
		int d = b; // should be okay
	}
}