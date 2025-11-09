package liquidjava.diagnostics.warnings;

import spoon.reflect.declaration.CtElement;

/**
 * Warning indicating that a class referenced in an external refinement was not found
 * 
 * @see LJWarning
 */
public class ExternalClassNotFoundWarning extends LJWarning {

    private String className;

    public ExternalClassNotFoundWarning(CtElement element, String message, String className) {
        super(message, element);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(className);
        return super.toString(sb.toString());
    }
}
