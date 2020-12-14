package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectRecursion {
	@Refinement("{k >= 0}->{\\v == 0}")
	public static int untilZero(int k) {
		if(k == 0)
			return 0;
		else
			return untilZero(k-1);
	}

}
