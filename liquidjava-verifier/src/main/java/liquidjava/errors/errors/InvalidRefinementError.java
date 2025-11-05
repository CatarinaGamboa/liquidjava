package liquidjava.errors.errors;

import spoon.reflect.declaration.CtElement;

// when a refinement is invalid, e.g. is not a boolean expression
public class InvalidRefinementError extends LJError {

    private String refinement;

    public InvalidRefinementError(String message, CtElement element, String refinement) {
        super("Invalid Refinement", message, element);
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
