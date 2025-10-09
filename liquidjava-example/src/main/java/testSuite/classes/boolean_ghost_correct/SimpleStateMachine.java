package testSuite.classes.boolean_ghost_correct;

import liquidjava.specification.Ghost;
import liquidjava.specification.StateRefinement;

@Ghost("boolean open")
public class SimpleStateMachine {

	@StateRefinement(from = "!open(this)", to = "open(this)")
	void open() {}

	@StateRefinement(from = "open(this)")
	void execute() {}

	@StateRefinement(from = "open(this)", to = "!open(this)")
	void close() {}
}