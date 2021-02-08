package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectRecursion {
	@Refinement("_ == 0")
	public static int untilZero(@Refinement("k >= 0")int k) {
		if(k == 0)
			return 0;
		else
			return untilZero(k-1);
	}

}
