package test.project;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"uninitialized", "initialized"})
public class SM2 {
	
	
	@StateRefinement(to="uninitialized(this)")
	public SM2() {}
	

	@StateRefinement(from="uninitialized(this)", to="initialized(this)")
	public void initialize() {}


}
