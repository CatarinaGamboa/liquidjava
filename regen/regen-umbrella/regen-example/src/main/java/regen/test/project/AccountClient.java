package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"active", "inactive"})
public class AccountClient {
	
	@StateRefinement(from="active(this)", to="active(this)")
	public boolean transfer(Account from, Account to, @Refinement("amount(from) >= value") int value) {
		from.withdraw(value);
		to.add(value);
		return true;
	}

}
