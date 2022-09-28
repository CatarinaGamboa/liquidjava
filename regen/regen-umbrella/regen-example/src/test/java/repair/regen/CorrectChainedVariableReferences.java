package repair.regen;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class CorrectChainedVariableReferences {
	public static void main(String[] args) {
		@Refinement("_ < 10")
		int a = 5;

		@Refinement("_ > a && _ < 20")
		int b = 18;

		@Refinement("_ > b && _ < 60")
		int c = 40;

		@Refinement("true")
		int d = c;

		@Refinement("_ > c")
		int e = 80;

		@Refinement("_ > (c+c)")
		int f = 8000;
	}
}
