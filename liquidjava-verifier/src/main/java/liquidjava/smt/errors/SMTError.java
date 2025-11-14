package liquidjava.smt.errors;

import spoon.reflect.declaration.CtElement;

public class SMTError extends Exception {
    private CtElement location;

    public SMTError(String message) {
        super(message);
    }

    public CtElement getLocation() {
        return location;
    }

    public void setLocation(CtElement location) {
        this.location = location;
    }
}
