package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorIfAssignment {
	public static void main(String[] args) {
		@Refinement("_ < 10")
		int a = 5;

		if(a > 0) {
			@Refinement("b > 0")
			int b = a;
			b++;
			a = 10;

		}

	}

}
