package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that an element referenced in a refinement was not found
 * 
 * @see LJError
 */
public class NotFoundError extends LJError {

    public NotFoundError(String message, CtElement element, TranslationTable translationTable) {
        super("Not Found Error", message, element, translationTable);
    }

    @Override
    public String toString() {
        return super.toString(null);
    }
}
