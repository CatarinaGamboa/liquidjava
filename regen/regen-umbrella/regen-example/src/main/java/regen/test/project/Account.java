package regen.test.project;

import repair.regen.specification.Ghost;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@Ghost("int amount")
@StateSet({"positive", "negative"})
public class Account {
	int qnt;
	
	@StateRefinement(from="positive(this)",
					 to="(amount(this) == amount(old(this)) - x) && " + 
					    "(amount(this) < 0? negative(this):positive(this))")
//	@StateRefinement(to="(amount(this) == amount(old(this)) - x)")
	public int withdraw(int x) {
		qnt -= x;
		return qnt;
	}

	@StateRefinement(to="amount(this) == amount(old(this)) + x")
	public void add(int x) {
		qnt += x;
	}

}
