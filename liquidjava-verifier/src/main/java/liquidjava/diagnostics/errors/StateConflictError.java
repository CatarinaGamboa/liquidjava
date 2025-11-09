package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that two disjoint states were found in a state refinement
 * 
 * @see LJError
 */
public class StateConflictError extends LJError {

    private String state;
    private String className;

    public StateConflictError(CtElement element, String state, String className, TranslationTable translationTable) {
        super("State Conflict Error",
                "Found multiple disjoint states in state transition — State transition can only go to one state of each state set",
                element, translationTable);
        this.state = state;
        this.className = className;
    }

    public String getState() {
        return state;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(className).append("\n");
        sb.append("State: ").append(state);
        return super.toString(sb.toString());
    }
}
