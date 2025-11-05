package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that an element referenced in a refinement was not found
 * 
 * @see LJError
 */
public class NotFoundError extends LJError {

    public NotFoundError(String message, CtElement element) {
        super("Not Found Error", message, element);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
