package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSpecificVarInRefinementIf {
	public static void main(String[] args) {
		@Refinement("_ < 10")
		int a = 6;
		if(a > 0) {
			a = -2;
			@Refinement("b < a")
			int b = -3;

		}
	}
}
