package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that the syntax of a refinement is invalid
 * 
 * @see LJError
 */
public class SyntaxError extends LJError {

    private String refinement;

    public SyntaxError(String message, String refinement) {
        this(message, null, refinement);
    }

    public SyntaxError(String message, CtElement element, String refinement) {
        super("Syntax Error", message, "", element.getPosition(), null);
        this.refinement = refinement;
    }

    public String getRefinement() {
        return refinement;
    }
}
