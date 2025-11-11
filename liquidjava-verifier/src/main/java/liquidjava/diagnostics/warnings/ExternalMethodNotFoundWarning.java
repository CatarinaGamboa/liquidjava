package liquidjava.diagnostics.warnings;

import spoon.reflect.declaration.CtElement;

/**
 * Warning indicating that a method referenced in an external refinement was not found
 * 
 * @see LJWarning
 */
public class ExternalMethodNotFoundWarning extends LJWarning {

    private String methodName;
    private String className;

    public ExternalMethodNotFoundWarning(CtElement element, String message, String details, String methodName, String className) {
        super(message, details, element.getPosition());
        this.methodName = methodName;
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }
}
