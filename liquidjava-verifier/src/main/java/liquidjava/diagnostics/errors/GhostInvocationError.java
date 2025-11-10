package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.Predicate;
import spoon.reflect.cu.SourcePosition;

/**
 * Error indicating that a ghost method invocation is invalid (e.g., has wrong arguments)
 * 
 * @see LJError
 */
public class GhostInvocationError extends LJError {

    private String expected;

    public GhostInvocationError(String message, SourcePosition pos, Predicate expected,
            TranslationTable translationTable) {
        super("Ghost Invocation Error", message, pos, null, translationTable);
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
