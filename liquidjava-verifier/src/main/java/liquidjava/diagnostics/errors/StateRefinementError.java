package liquidjava.diagnostics.errors;

import java.util.Arrays;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.ast.Expression;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a state refinement transition was violated
 * 
 * @see LJError
 */
public class StateRefinementError extends LJError {

    private final String method;
    private final String[] expected;
    private final String found;

    public StateRefinementError(CtElement element, String method, Expression[] expected, Expression found,
            TranslationTable translationTable) {
        super("State Refinement Error", "State refinement transition violation",
                String.format("Expected: %s\nFound: %s",
                        String.join(", ",
                                Arrays.stream(expected).map(Expression::toSimplifiedString).toArray(String[]::new)),
                        found.toSimplifiedString()),
                element.getPosition(), translationTable);
        this.method = method;
        this.expected = Arrays.stream(expected).map(Expression::toSimplifiedString).toArray(String[]::new);
        this.found = found.toSimplifiedString();
    }

    public String getMethod() {
        return method;
    }

    public String[] getExpected() {
        return expected;
    }

    public String getFound() {
        return found;
    }
}
