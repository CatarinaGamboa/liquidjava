package liquidjava.errors.errors;

import liquidjava.rj_language.Predicate;
import spoon.reflect.declaration.CtElement;

// when two incompatible states are found in a state set
public class StateConflictError extends LJError {

    private Predicate predicate;
    private String className;

    public StateConflictError(CtElement element, Predicate predicate, String className) {
        super("State Conflict Error", "Found multiple disjoint states from a StateSet in refinement", element);
        this.predicate = predicate;
        this.className = className;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(className).append("\n");
        sb.append("Predicate: ").append(predicate.toString());
        return super.toString(sb.toString());
    }
}
