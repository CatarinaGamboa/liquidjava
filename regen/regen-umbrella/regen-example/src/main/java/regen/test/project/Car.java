package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementAlias;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;

public abstract class Car {

	@RefinementPredicate("boolean isOpen(Car c)")
	@StateRefinement(from = "!isOpen(this)", to="isOpen(this)")
	public abstract void open(); 
	
	@StateRefinement(from = "isOpen(this)", to="!isOpen(this)")
	public abstract void close(); 
	

}
