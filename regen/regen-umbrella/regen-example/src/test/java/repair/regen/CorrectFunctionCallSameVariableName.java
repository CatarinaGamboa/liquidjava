package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionCallSameVariableName {
	@Refinement("{a > 20}->{ \\v == a + 1}")
	private static int addOnes(int a) {
		return a+1;
	}

	public static void main(String[] args) {
		@Refinement("\\v > 0")
		int a = 6;

		@Refinement("\\v > 20")
		int b = addOnes(50) + a;

		@Refinement("\\v > 10")
		int c = addOnes(a+90);

		@Refinement("\\v < 0")
		int d = -addOnes(a+90);

		@Refinement("\\v > 0")
		int e = addOnes(a+100);

	}

}
