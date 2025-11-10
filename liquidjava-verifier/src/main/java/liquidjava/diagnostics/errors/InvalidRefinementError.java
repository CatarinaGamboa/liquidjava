package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a refinement is invalid (e.g., not a boolean expression)
 * 
 * @see LJError
 */
public class InvalidRefinementError extends LJError {

    private String refinement;

    public InvalidRefinementError(CtElement element, String message, String refinement) {
        super("Invalid Refinement", message, element.getPosition(), element.toString(), null);
        this.refinement = refinement;
    }

    public String getRefinement() {
        return refinement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Refinement: ").append(refinement).append("\n");
        return super.toString(sb.toString());
    }
}
