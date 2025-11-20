package liquidjava.diagnostics.warnings;

import spoon.reflect.declaration.CtElement;

/**
 * Warning indicating that a class referenced in an external refinement was not found
 * 
 * @see LJWarning
 */
public class ExternalClassNotFoundWarning extends LJWarning {

    private final String className;

    public ExternalClassNotFoundWarning(CtElement element, String message, String className) {
        super(message, null, element.getPosition());
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
