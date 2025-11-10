package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a constructor contains a state refinement with a 'from' state, which is not allowed
 * 
 * @see LJError
 */
public class IllegalConstructorTransitionError extends LJError {

    public IllegalConstructorTransitionError(CtElement element) {
        super("Illegal Constructor Transition Error",
                "Found constructor with 'from' state (should only have a 'to' state)", element.getPosition(),
                element.toString(), null);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
