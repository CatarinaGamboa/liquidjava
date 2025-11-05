package liquidjava.errors.errors;

import liquidjava.rj_language.Predicate;
import spoon.reflect.declaration.CtElement;

// when a ghost call has wrong types or number of arguments
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
