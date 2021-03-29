package regen.test.project;

import repair.regen.specification.Ghost;
import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;

@Ghost("int sum")
public class Account {
	
	@Refinement("balance >= 0")
	private int balance;
	
	public Account() {
		balance = 0;
	}
	
	@StateRefinement(to = "sum(this) == v")
	public Account(@Refinement("v >= 0")int v) {
		balance = v;
	}
	
	@StateRefinement(to="(sum(old(this)) > v)? (sum(this) == (sum(old(this)) - v)) : (sum(this) == 0)")
	public void withdraw(int v) {
		if(v > balance)
			balance = 0;
		else
			balance = balance - v;
	}

	@StateRefinement(to="sum(this) == (sum(old(this)) + v)")
	public void deposit(int v) {
		balance += v;
	}

	
//	@StateRefinement(from="(amount <= sum(this)) && (sum(this) == sum(old(this)))")
//	@Refinement("sum(_) == (sum(old(_)) + amount)")
//	public Account transferTo(Account other, int amount) {
//		this.withdraw(amount);
//		other.deposit(amount);
//		return other;
//	}



}
