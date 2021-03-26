package regen.test.project;


@repair.regen.specification.Ghost("int sum")
public class Account {
    @repair.regen.specification.Refinement("balance >= 0")
    private int balance;

    public Account() {
        balance = 0;
    }

    @repair.regen.specification.StateRefinement(to = "sum(this) == v")
    public Account(@repair.regen.specification.Refinement("v >= 0")
    int v) {
        balance = v;
    }

    @repair.regen.specification.StateRefinement(to = "(sum(old(this)) > v)? (sum(this) == (sum(old(this)) - v)) : (sum(this) == 0)")
    public void withdraw(int v) {
        if (v > (balance))
            balance = 0;
        else
            balance = (balance) - v;

    }

    @repair.regen.specification.StateRefinement(to = "sum(this) == (sum(old(this)) + v)")
    public void deposit(int v) {
        balance += v;
    }

    @repair.regen.specification.StateRefinement(from = "(amount <= sum(this)) && (sum(this) == sum(old(this)))")
    public void transferTo(regen.test.project.Account other, int amount) {
        this.withdraw(amount);
        other.deposit(amount);
    }
}

