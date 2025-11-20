package liquidjava.diagnostics.errors;

import spoon.reflect.cu.SourcePosition;

/**
 * Error indicating that the syntax of a refinement is invalid
 * 
 * @see LJError
 */
public class SyntaxError extends LJError {

    private final String refinement;

    public SyntaxError(String message, String refinement) {
        this(message, null, refinement);
    }

    public SyntaxError(String message, SourcePosition pos, String refinement) {
        super("Syntax Error", message, pos, null);
        this.refinement = refinement;
    }

    public String getRefinement() {
        return refinement;
    }
}
