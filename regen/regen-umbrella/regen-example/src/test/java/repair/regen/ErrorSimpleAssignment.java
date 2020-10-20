package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSimpleAssignment {
	public static void main(String[] args) {
		@Refinement("c > 2")                      
		int c = 2; // should emit error
	}
}
