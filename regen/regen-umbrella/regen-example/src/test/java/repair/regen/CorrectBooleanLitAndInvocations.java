package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectBooleanLitAndInvocations {	
	@Refinement("{true}->{ \\v == (n > 10) }")
	public static boolean greaterThanTen(int n) {
		return n > 10;
	}

	public static void main(String[] args) {

		@Refinement("\\v < 10")
		int a = 5;

		@Refinement("\\v == true")
		boolean k = (a < 11);

		@Refinement("\\v == true")
		boolean t = !(a == 12);

		@Refinement("\\v == false")
		boolean m = greaterThanTen(a);
	}

}
