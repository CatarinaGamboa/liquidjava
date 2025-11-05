package liquidjava.diagnostics.errors;

import liquidjava.rj_language.Predicate;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;
import liquidjava.utils.Utils;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a refinement constraint either was violated or cannot be proven
 * 
 * @see LJError
 */
public class RefinementError extends LJError {

    private Predicate expected;
    private ValDerivationNode found;

    public RefinementError(CtElement element, Predicate expected, ValDerivationNode found) {
        super("Refinement Error", "Predicate refinement violation", element);
        this.expected = expected;
        this.found = found;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expected: ").append(Utils.stripParens(expected.toString())).append("\n");
        sb.append("Found: ").append(found.getValue());
        return super.toString(sb.toString());
    }
}