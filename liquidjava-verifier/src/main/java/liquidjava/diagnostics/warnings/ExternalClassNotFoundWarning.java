package liquidjava.diagnostics.warnings;

import spoon.reflect.declaration.CtElement;

// when a class referenced in an external refinement cannot be found
public class ExternalClassNotFoundWarning extends LJWarning {

    private String className;

    public ExternalClassNotFoundWarning(CtElement element, String className) {
        super("Class in external refinement not found", element);
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
