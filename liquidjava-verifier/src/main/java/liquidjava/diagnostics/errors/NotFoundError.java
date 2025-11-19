package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.utils.Utils;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that an element referenced in a refinement was not found
 * 
 * @see LJError
 */
public class NotFoundError extends LJError {
    
    private final String name;

    public NotFoundError(CtElement element, String message, String name, TranslationTable translationTable) {
        super("Not Found Error", message, "", element.getPosition(), translationTable);
        this.name = Utils.getSimpleName(name);
    }

    public String getName() {
        return name;
    }
}
