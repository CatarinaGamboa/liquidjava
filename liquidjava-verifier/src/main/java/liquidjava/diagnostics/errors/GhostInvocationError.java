package liquidjava.diagnostics.errors;

import liquidjava.rj_language.Predicate;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a ghost method invocation is invalid (e.g., has wrong arguments)
 * 
 * @see LJError
 */
public class GhostInvocationError extends LJError {

    private Predicate expected;

    public GhostInvocationError(CtElement element, Predicate expected) {
        super("Ghost Invocation Error", "Invalid types or number of arguments in ghost invocation", element);
        this.expected = expected;
    }

    public Predicate getExpected() {
        return expected;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expected: ").append(expected.toString()).append("\n");
        return super.toString(sb.toString());
    }
}
