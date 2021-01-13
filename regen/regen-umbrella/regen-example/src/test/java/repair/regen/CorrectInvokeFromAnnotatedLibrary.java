package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectInvokeFromAnnotatedLibrary {
	public static void main(String[] args) {
		@Refinement("b > 0")
		int b = Math.abs(6);
		
		@Refinement("\\v >= 0")
		double c = Math.random();
	}
}
