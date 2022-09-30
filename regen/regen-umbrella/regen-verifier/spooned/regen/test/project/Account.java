package regen.test.project;


@repair.regen.specification.Ghost("int amount")
@repair.regen.specification.StateSet({ "positive", "negative" })
public class Account {
    int qnt;

    // @StateRefinement(to="(amount(this) == amount(old(this)) - x)")
    @repair.regen.specification.StateRefinement(from = "positive(this)", to = "(amount(this) == amount(old(this)) - x) && " + "(amount(this) < 0? negative(this):positive(this))")
    public int withdraw(int x) {
        qnt -= x;
        return qnt;
    }

    @repair.regen.specification.StateRefinement(to = "amount(this) == amount(old(this)) + x")
    public void add(int x) {
        qnt += x;
    }
}

