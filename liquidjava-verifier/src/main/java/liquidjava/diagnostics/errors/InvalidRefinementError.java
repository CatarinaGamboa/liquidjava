package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a refinement is invalid (e.g., not a boolean expression)
 * 
 * @see LJError
 */
public class InvalidRefinementError extends LJError {

    private final String refinement;

    public InvalidRefinementError(CtElement element, String message, String refinement) {
        super("Invalid Refinement", message, "", element.getPosition(), null);
        this.refinement = refinement;
    }

    public String getRefinement() {
        return refinement;
    }
}
