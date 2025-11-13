package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that an element referenced in a refinement was not found
 * 
 * @see LJError
 */
public class NotFoundError extends LJError {

    public NotFoundError(CtElement element, String message, TranslationTable translationTable) {
        super("Not Found Error", message, element.getPosition(), element.toString(), translationTable);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
