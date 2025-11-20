package liquidjava.diagnostics.warnings;

import spoon.reflect.declaration.CtElement;

/**
 * Warning indicating that a method referenced in an external refinement was not found
 * 
 * @see LJWarning
 */
public class ExternalMethodNotFoundWarning extends LJWarning {

    private final String methodName;
    private final String className;
    private final String[] overloads;

    public ExternalMethodNotFoundWarning(CtElement element, String message, String details, String methodName,
            String className, String[] overloads) {
        super(message, details, element.getPosition());
        this.methodName = methodName;
        this.className = className;
        this.overloads = overloads;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    public String[] getOverloads() {
        return overloads;
    }
}
