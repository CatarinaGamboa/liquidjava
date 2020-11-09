package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorTernaryExpression {
	@Refinement("{\\v == 3}")
	public static int three() {
		return 3;
	}
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;
		a = (a == 2)? 6+three() : 4*three();
	}
}
