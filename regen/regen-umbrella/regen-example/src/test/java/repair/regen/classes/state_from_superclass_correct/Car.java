package repair.regen.classes.state_from_superclass_correct;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;
@StateSet({"open", "close"})
public abstract class Car {

//	@RefinementPredicate("boolean isOpen(Car c)")
	@StateRefinement(from = "close(this)", to="open(this)")
	public abstract void open(); 
	
	@StateRefinement(from = "open(this)", to="close(this)")
	public abstract void close(); 
	

}
