package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSimpleIfThen {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;

		if(a > 0) {
			@Refinement("b > 0")
			int b = a;
			b++;
			if(b > 10) {
				@Refinement("\\v > 0")
				int c = a;
				@Refinement("\\v > 11")
				int d = b+1;
			}
		}

	}
}
