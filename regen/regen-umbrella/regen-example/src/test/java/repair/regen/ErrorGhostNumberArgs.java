package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;

public class ErrorGhostNumberArgs {
	
	@RefinementPredicate("ghost boolean open(int)")
	@Refinement("open(1,2) == true")
	public int one() {
		return 1;
	}

}
