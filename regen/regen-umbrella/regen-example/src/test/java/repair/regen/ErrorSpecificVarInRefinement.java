package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSpecificVarInRefinement {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 6;
		
		@Refinement("\\v > a")
		int b = 9;

	}
}
