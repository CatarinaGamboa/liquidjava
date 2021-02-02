package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorLongUsage1 {
	public static void main(String[] args) {
		@Refinement("a > 5")
		long a = 9L;

		if(a > 5) {
			@Refinement("b < 50")
			long b = a*10;
		}

	}
}
