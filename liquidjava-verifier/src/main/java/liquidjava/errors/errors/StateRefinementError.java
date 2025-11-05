package liquidjava.errors.errors;

import java.util.Arrays;

import spoon.reflect.declaration.CtElement;

// when a @StateRefinement is violated
public class StateRefinementError extends LJError {

    private final String method;
    private final String[] expected;
    private final String found;

    public StateRefinementError(CtElement element, String method, String[] expected, String found) {
        super("State Refinement Error", "State refinement transition violation", element);
        this.method = method;
        this.expected = expected;
        this.found = found;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Method: ").append(method).append("\n");
        sb.append("Expected: ");
        Arrays.stream(expected).forEach(s -> sb.append(s).append(", "));
        sb.append("\n");
        sb.append("Found: ").append(found);
        return super.toString(sb.toString());
    }
}
