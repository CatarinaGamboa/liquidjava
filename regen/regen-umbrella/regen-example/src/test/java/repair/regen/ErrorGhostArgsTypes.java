package repair.regen;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;

public class ErrorGhostArgsTypes {
	@RefinementPredicate("ghost boolean open(int)")
	@Refinement("open(4.5) == true")
	public int one() {
		return 1;
	}
}
