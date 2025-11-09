package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.Predicate;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a ghost method invocation is invalid (e.g., has wrong arguments)
 * 
 * @see LJError
 */
public class GhostInvocationError extends LJError {

    private String expected;

    public GhostInvocationError(CtElement element, Predicate expected, TranslationTable translationTable) {
        super("Ghost Invocation Error", "Invalid types or number of arguments in ghost invocation", element,
                translationTable);
        this.expected = expected.toString();
    }

    public String getExpected() {
        return expected;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expected: ").append(expected).append("\n");
        return super.toString(sb.toString());
    }
}
