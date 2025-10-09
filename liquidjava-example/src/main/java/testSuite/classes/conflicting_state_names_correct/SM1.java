package testSuite.classes.conflicting_state_names_correct;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"uninitialized", "initialized"})
public class SM1 {

	@StateRefinement(to="uninitialized(this)")
	public SM1() {}
	

	@StateRefinement(from="uninitialized(this)", to="initialized(this)")
	public void initialize() {}
}