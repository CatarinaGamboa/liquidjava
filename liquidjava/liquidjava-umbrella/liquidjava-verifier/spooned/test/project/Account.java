package test.project;


@liquidjava.specification.Ghost("int amount")
@liquidjava.specification.StateSet({ "positive", "negative" })
public class Account {
    int qnt;

    // @StateRefinement(to="(amount(this) == amount(old(this)) - x)")
    @liquidjava.specification.StateRefinement(from = "positive(this)", to = "(amount(this) == amount(old(this)) - x) && " + "(amount(this) < 0? negative(this):positive(this))")
    public int withdraw(int x) {
        qnt -= x;
        return qnt;
    }

    @liquidjava.specification.StateRefinement(to = "amount(this) == amount(old(this)) + x")
    public void add(int x) {
        qnt += x;
    }
}

