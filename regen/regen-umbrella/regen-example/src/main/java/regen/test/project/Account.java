package regen.test.project;

import repair.regen.specification.Ghost;
import repair.regen.specification.StateRefinement;

@Ghost("int amount")
public class Account {
	int qnt;
	
	@StateRefinement(to="amount(this) == amount(old(this)) - x")
	public void withdraw(int x) {
		qnt -= x;
	}

	@StateRefinement(to="amount(this) == amount(old(this)) + x")
	public void add(int x) {
		qnt += x;
	}

}
