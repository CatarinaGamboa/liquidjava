package testSuite.classes.vending_machine_correct;

import liquidjava.specification.Ghost;
import liquidjava.specification.StateRefinement;

@Ghost("double value")
public class VendingMachine {

    @StateRefinement(from = "value(this) >= 0.0", to = "value(this) == value(old(this)) + 0.1")
    void insertTenCents() {}

    @StateRefinement(from = "value(this) >= 0.3")
    void buy() {}
}