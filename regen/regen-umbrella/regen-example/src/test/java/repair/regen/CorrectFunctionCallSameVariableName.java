package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFunctionCallSameVariableName {
	@Refinement("{a > 20}->{ _ == a + 1}")
	private static int addOnes(int a) {
		return a+1;
	}

	public static void main(String[] args) {
		@Refinement("_ > 0")
		int a = 6;

		@Refinement("_ > 20")
		int b = addOnes(50) + a;

		@Refinement("_ > 10")
		int c = addOnes(a+90);

		@Refinement("_ < 0")
		int d = -addOnes(a+90);

		@Refinement("_ > 0")
		int e = addOnes(a+100);

	}

}
