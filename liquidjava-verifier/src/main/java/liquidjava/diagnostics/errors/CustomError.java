package liquidjava.diagnostics.errors;

import spoon.reflect.cu.SourcePosition;

/**
 * Custom error with an arbitrary message
 * 
 * @see LJError
 */
public class CustomError extends LJError {

    public CustomError(String message) {
        super("Error", message, null, null);
    }

    public CustomError(String message, SourcePosition position) {
        super("Error", message, position, null);
    }
}
