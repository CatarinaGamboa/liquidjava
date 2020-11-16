package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorBooleanLiteral {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;

		@Refinement("\\v == true")
		boolean k = (a < 11);

		@Refinement("\\v == false")
		boolean t = !(a == 12);
	}
}
