package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;
import liquidjava.utils.Utils;
import spoon.reflect.declaration.CtElement;

/**
 * Error indicating that a refinement constraint either was violated or cannot be proven
 * 
 * @see LJError
 */
public class RefinementError extends LJError {

    private String expected;
    private ValDerivationNode found;

    public RefinementError(CtElement element, String expected, ValDerivationNode found,
            TranslationTable translationTable) {
        super("Refinement Error", String.format("%s is not a subtype of %s", found.getValue(), expected), element,
                translationTable);
        this.expected = expected;
        this.found = found;
    }

    public String getExpected() {
        return expected;
    }

    public ValDerivationNode getFound() {
        return found;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Expected: ").append(Utils.stripParens(expected)).append("\n");
        sb.append("Found: ").append(found.getValue());
        return super.toString(sb.toString());
    }
}