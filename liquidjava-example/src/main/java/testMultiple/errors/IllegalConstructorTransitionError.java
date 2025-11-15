package testMultiple.errors;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "closed"})
public class IllegalConstructorTransitionError {

    @StateRefinement(from="open(this)", to="closed(this)")
    public IllegalConstructorTransitionError() {}
}
