package regen.test.project;


@repair.regen.specification.StateSet({ "active", "inactive" })
public class AccountClient {
    @repair.regen.specification.StateRefinement(from = "active(this)", to = "active(this)")
    public boolean transfer(regen.test.project.Account from, regen.test.project.Account to, @repair.regen.specification.Refinement("amount(from) >= value")
    int value) {
        from.withdraw(value);
        to.add(value);
        return true;
    }
}

