package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSpecificValuesIf2 {
	public static void main(String[] args) {
		@Refinement("_ > 10")
		int a = 15;
		if(a > 14) {
			a = 12;
			@Refinement("_ < 11")
			int c = a;
		}
	}
}
