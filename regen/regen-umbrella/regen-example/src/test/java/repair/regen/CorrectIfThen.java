package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectIfThen {
	public static void main(String[] args) {
		@Refinement("_ < 10")
		int a = 5;

		if(a > 0) {
			@Refinement("b > 0")
			int b = a;
			b++;
			if(b > 10) {
				@Refinement("_ > 0")
				int c = a;
				@Refinement("_ > 11")
				int d = b+1;
			}
			if(a > b) {
				@Refinement("_ > b")
				int c = a;
			}
		}
		

	}
}
