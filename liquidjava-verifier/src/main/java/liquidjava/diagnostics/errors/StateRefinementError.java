package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.ast.Expression;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a state refinement transition was violated
 * 
 * @see LJError
 */
public class StateRefinementError extends LJError {

    private final String expected;
    private final String found;

    public StateRefinementError(CtElement element, Expression expected, Expression found,
            TranslationTable translationTable) {
        super("State Refinement Error", String.format("Expected state '%s' but found '%s'", expected.toSimplifiedString(), found.toSimplifiedString()), null,
                element.getPosition(), translationTable);
        this.expected = expected.toSimplifiedString();
        this.found = found.toSimplifiedString();
    }

    public String getExpected() {
        return expected;
    }

    public String getFound() {
        return found;
    }
}
