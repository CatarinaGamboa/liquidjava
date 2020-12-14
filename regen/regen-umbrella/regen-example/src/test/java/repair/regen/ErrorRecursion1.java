package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorRecursion1 {
	
	@Refinement("{k >= 0}->{\\v == 0}")
	public static int untilZero(int k) {
		if(k == 1)
			return 0;
		else
			return untilZero(k-1);
	}
}
