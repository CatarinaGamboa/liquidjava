package liquidjava.smt;

import spoon.reflect.declaration.CtElement;

public class TypeCheckError extends Exception {

    private CtElement location;

    public TypeCheckError(String message) {
        super(message);
    }

    public CtElement getLocation() {
        return location;
    }

    public void setLocation(CtElement location) {
        this.location = location;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
