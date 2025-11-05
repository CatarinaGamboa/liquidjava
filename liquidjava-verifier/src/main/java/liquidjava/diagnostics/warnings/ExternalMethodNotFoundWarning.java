package liquidjava.diagnostics.warnings;

import spoon.reflect.declaration.CtElement;

// when a method referenced in an external refinement cannot be found
public class ExternalMethodNotFoundWarning extends LJWarning {

    private String methodName;
    private String className;

    public ExternalMethodNotFoundWarning(CtElement element, String methodName, String className) {
        super("Method in external refinement not found", element);
        this.methodName = methodName;
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(className).append("\n");
        sb.append("Method: ").append(methodName);
        return super.toString(sb.toString());
    }
}
