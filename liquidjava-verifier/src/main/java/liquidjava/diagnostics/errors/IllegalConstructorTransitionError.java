package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

// when a constructor contains a @StateRefinement with a from state
public class IllegalConstructorTransitionError extends LJError {

    public IllegalConstructorTransitionError(CtElement element) {
        super("Illegal Constructor Transition Error",
                "Found constructor with 'from' state (should only have a 'to' state)", element);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
