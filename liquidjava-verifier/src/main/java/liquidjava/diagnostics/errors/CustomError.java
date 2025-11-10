package liquidjava.diagnostics.errors;

import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

/**
 * Custom error with an arbitrary message
 * 
 * @see LJError
 */
public class CustomError extends LJError {

    public CustomError(String message) {
        super("Found Error", message, null, null, null);
    }

    public CustomError(String message, SourcePosition pos) {
        super("Found Error", message, pos, null, null);
    }

    public CustomError(CtElement element, String message) {
        super("Found Error", message, element.getPosition(), element.toString(), null);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
