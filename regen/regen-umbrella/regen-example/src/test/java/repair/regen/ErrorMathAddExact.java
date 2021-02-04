package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathAddExact {
	public static void main(String[] args) {
		@Refinement("_ < 0")
		int y1 = Math.addExact(5, -6);
		@Refinement("_ == -5")
		int a3 = Math.addExact(-6, y1);
	}
}
