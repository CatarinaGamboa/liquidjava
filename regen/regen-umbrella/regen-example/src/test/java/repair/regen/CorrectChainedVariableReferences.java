package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectChainedVariableReferences {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;

		@Refinement("\\v > a && \\v < 20")
		int b = 18;

		@Refinement("\\v > b && \\v < 60")
		int c = 40;

		@Refinement("true")
		int d = c;

		@Refinement("\\v > c")
		int e = 80;

		@Refinement("\\v > (c+c)")
		int f = 8000;
	}
}
