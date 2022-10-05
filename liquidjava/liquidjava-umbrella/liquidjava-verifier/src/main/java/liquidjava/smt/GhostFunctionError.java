package liquidjava.smt;

import spoon.reflect.declaration.CtElement;

public class GhostFunctionError extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private CtElement location;

    public GhostFunctionError(String message) {
        super(message);
    }

    public CtElement getLocation() {
        return location;
    }

    public void setLocation(CtElement location) {
        this.location = location;
    }

}
