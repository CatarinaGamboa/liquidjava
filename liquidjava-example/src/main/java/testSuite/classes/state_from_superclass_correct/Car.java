package testSuite.classes.state_from_superclass_correct;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "close"})
public abstract class Car {

    //	@RefinementPredicate("boolean isOpen(Car c)")
    @StateRefinement(from = "close(this)", to = "open(this)")
    public abstract void open();

    @StateRefinement(from = "open(this)", to = "close(this)")
    public abstract void close();
}
