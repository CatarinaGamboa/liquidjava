package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

/**
 * Custom error with an arbitrary message
 * 
 * @see LJError
 */
public class CustomError extends LJError {

    public CustomError(CtElement element, String message) {
        super("Found Error", message, element, null);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
