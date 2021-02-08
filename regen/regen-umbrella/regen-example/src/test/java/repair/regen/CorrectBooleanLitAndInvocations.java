package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectBooleanLitAndInvocations {	
	@Refinement("_ == (n > 10)")
	public static boolean greaterThanTen(int n) {
		return n > 10;
	}

	public static void main(String[] args) {

		@Refinement("_ < 10")
		int a = 5;

		@Refinement("_ == true")
		boolean k = (a < 11);

		@Refinement("_ == true")
		boolean o = !(a == 12);

		@Refinement("_ == false")
		boolean m = greaterThanTen(a);
	}

}
