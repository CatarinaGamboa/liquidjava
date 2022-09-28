package repair.regen;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class ErrorTypeInRefinements {

	public static void main(String[] args) {
		int a = 10;
		
		@Refinement("(b == 6)")
		boolean b = true;
	}
}
