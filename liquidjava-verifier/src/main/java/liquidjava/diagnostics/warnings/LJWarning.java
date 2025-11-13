package liquidjava.diagnostics.warnings;

import liquidjava.diagnostics.ErrorPosition;
import liquidjava.utils.Utils;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

/**
 * Base class for all LiquidJava warnings
 */
public abstract class LJWarning {

    private String message;
    private CtElement element;
    private ErrorPosition position;
    private SourcePosition location;

    public LJWarning(String message, CtElement element) {
        this.message = message;
        this.element = element;
        try {
            this.location = element.getPosition();
            this.position = ErrorPosition.fromSpoonPosition(element.getPosition());
        } catch (Exception e) {
            // This warning is from a generated part of the source code, so no precise position is provided
            this.location = null;
            this.position = null;
        }
    }

    public String getMessage() {
        return message;
    }

    public CtElement getElement() {
        return element;
    }

    public ErrorPosition getPosition() {
        return position;
    }

    public SourcePosition getLocation() {
        return location;
    }

    @Override
    public abstract String toString();

    public String toString(String extra) {
        StringBuilder sb = new StringBuilder();
        sb.append(message);

        if (element != null)
            sb.append(" at: \n").append(element.toString().replace("@liquidjava.specification.", "@"));

        if (extra != null)
            sb.append("\n").append(extra);

        sb.append("\n");
        sb.append("Location: ").append(location != null ? Utils.stripParens(location.toString()) : "<unknown>")
                .append("\n");
        return sb.toString();
    }
}
