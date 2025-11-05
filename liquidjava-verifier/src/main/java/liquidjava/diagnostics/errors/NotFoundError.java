package liquidjava.diagnostics.errors;

import spoon.reflect.declaration.CtElement;

// e.g. when a variable used in a refinement is not found
public class NotFoundError extends LJError {

    public NotFoundError(String message, CtElement element) {
        super("Not Found Error", message, element);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
