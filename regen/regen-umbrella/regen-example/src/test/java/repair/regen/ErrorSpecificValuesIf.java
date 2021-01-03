package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSpecificValuesIf {
	@Refinement("{a > 0} -> {true}")
	public static void addZ(int a) {
		@Refinement("\\v > 0")
		int d = a;
		if(d > 5) {
			@Refinement("b > 5")
			int b = d;
		}else {
			@Refinement("\\v <= 5")
			int c = d;
			d = 10;
			@Refinement("b > 10")
			int b = d;
		}
	}

}
