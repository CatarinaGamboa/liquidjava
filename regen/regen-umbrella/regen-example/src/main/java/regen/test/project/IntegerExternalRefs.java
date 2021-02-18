package regen.test.project;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;

@ExternalRefinementsFor("java.lang.Integer")
public interface IntegerExternalRefs {
	
	@Refinement("_ == 2147483647")
	public int MAX_VALUE = 0;

}
