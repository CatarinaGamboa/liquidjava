package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectInvokeFromAnnotatedLibrary {
	public static void main(String[] args) {
		@Refinement("b > 0")
		int b = Math.abs(6);
		
		@Refinement("\\v >= 0")
		double c = Math.random();
		

		@Refinement("\\v == 6")
		int a = Math.abs(6);

		@Refinement("\\v > 4")
		int d = Math.abs(-6);

		@Refinement("\\v == -6")
		int e = -Math.abs(-d);

		@Refinement("\\v < 0")
		double f = -Math.random();
	}
}
