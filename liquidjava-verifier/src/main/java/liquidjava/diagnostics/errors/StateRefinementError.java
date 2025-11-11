package liquidjava.diagnostics.errors;

import java.util.Arrays;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.Predicate;
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

    public StateRefinementError(CtElement element, String method, Predicate[] expected, Predicate found,
            TranslationTable translationTable) {
        super("State Refinement Error", "State refinement transition violation", String.format("Expected: %s\nFound: %s",
                String.join(", ", Arrays.stream(expected).map(Predicate::toString).toArray(String[]::new)), found.toString()), element.getPosition(),
                translationTable);
        this.method = method;
        this.expected = Arrays.stream(expected).map(Predicate::toString).toArray(String[]::new);
        this.found = found.toString();
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
