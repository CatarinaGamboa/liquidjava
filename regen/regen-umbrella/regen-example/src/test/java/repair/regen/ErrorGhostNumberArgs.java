package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementFunction;

public class ErrorGhostNumberArgs {
	
	@RefinementFunction("ghost boolean open(int)")
	@Refinement("open(1,2) == true")
	public int one() {
		return 1;
	}

}
