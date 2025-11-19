package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.ast.Expression;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that two disjoint states were found in a state refinement
 * 
 * @see LJError
 */
public class StateConflictError extends LJError {

    private final String state;;

    public StateConflictError(CtElement element, Expression state,
            TranslationTable translationTable) {
        super("State Conflict Error", "Found multiple disjoint states in state transition",
                "State transition can only go to one state of each state set", element.getPosition(), translationTable);
        this.state = state.toSimplifiedString();
    }

    public String getState() {
        return state;
    }
}
