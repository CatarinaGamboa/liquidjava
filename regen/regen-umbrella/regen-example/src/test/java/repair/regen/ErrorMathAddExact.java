package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathAddExact {
	public static void main(String[] args) {
		@Refinement("\\v < 0")
		int y1 = Math.addExact(5, -6);
		@Refinement("\\v == -5")
		int a3 = Math.addExact(-6, y1);
	}
}
