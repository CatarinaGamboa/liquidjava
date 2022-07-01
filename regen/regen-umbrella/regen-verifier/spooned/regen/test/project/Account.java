package regen.test.project;


@repair.regen.specification.Ghost("int amount")
public class Account {
    int qnt;

    @repair.regen.specification.StateRefinement(to = "amount(this) == amount(old(this)) - x")
    public void withdraw(int x) {
        qnt -= x;
    }

    @repair.regen.specification.StateRefinement(to = "amount(this) == amount(old(this)) + x")
    public void add(int x) {
        qnt += x;
    }
}

