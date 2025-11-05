package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.ErrorPosition;
import liquidjava.utils.Utils;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;

// base class for all LiquidJava errors
public abstract class LJError extends Exception {

    private String title;
    private String message;
    private CtElement element;
    private ErrorPosition position;
    private SourcePosition location;

    public LJError(String title, String message, CtElement element) {
        super(message);
        this.title = title;
        this.message = message;
        this.element = element;
        try {
            this.location = element.getPosition();
            this.position = ErrorPosition.fromSpoonPosition(element.getPosition());
        } catch (Exception e) {
            this.location = null;
            this.position = null;
        }
    }

    public String getTitle() {
        return title;
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
        sb.append(title).append(" at: \n").append(element.toString().replace("@liquidjava.specification.", "@"))
                .append("\n\n");
        sb.append(message).append("\n");
        if (extra != null)
            sb.append(extra).append("\n");
        sb.append("Location: ").append(location != null ? Utils.stripParens(location.toString()) : "<unknown>")
                .append("\n");
        return sb.toString();
    }
}
