package liquidjava.errors.errors;

import spoon.reflect.declaration.CtElement;

// when the syntax of a refinement is invalid
public class SyntaxError extends LJError {

    private String refinement;

    public SyntaxError(String message, String refinement) {
        this(message, null, refinement);
    }

    public SyntaxError(String message, CtElement element, String refinement) {
        super("Syntax Error", message, element);
        this.refinement = refinement;
    }

    public String getRefinement() {
        return refinement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid syntax in refinement: ").append(refinement);
        return super.toString(sb.toString());
    }
}
