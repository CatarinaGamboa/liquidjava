package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.ast.Expression;
import spoon.reflect.cu.SourcePosition;

/**
 * Error indicating that a ghost method invocation is invalid (e.g., has wrong arguments)
 * 
 * @see LJError
 */
public class GhostInvocationError extends LJError {

    private String expected;

    public GhostInvocationError(String message, SourcePosition pos, Expression expected,
            TranslationTable translationTable) {
        super("Ghost Invocation Error", message, "", pos, translationTable);
        this.expected = expected.toSimplifiedString();
    }

    public String getExpected() {
        return expected;
    }
}
