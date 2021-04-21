package repair.regen.classes;

import repair.regen.specification.Ghost;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"empty","addingItems", "checkout", "closed"})
@Ghost("int totalPrice(int x)")//Should have no parameters
public class ErrorGhostState {
	
	@StateRefinement(to = "(totalPrice(this) == 0) && empty(this)")
	public ErrorGhostState() {}

}
