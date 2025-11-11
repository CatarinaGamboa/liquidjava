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
        super("Error", message, null, null, null);
    }

    public CustomError(String message, SourcePosition pos) {
        super("Error", message, null, pos, null);
    }

    public CustomError(String message, CtElement element) {
        super("Error", message, null, element.getPosition(), null);
    }
}
