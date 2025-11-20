package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.TranslationTable;
import liquidjava.rj_language.ast.Expression;
import liquidjava.rj_language.opt.derivation_node.ValDerivationNode;
import spoon.reflect.cu.SourcePosition;

/**
 * Error indicating that a refinement constraint either was violated or cannot be proven
 * 
 * @see LJError
 */
public class RefinementError extends LJError {

    private final String expected;
    private final ValDerivationNode found;

    public RefinementError(SourcePosition position, Expression expected, ValDerivationNode found,
            TranslationTable translationTable) {
        super("Refinement Error",
                String.format("%s is not a subtype of %s", found.getValue(), expected.toSimplifiedString()),
                position, translationTable);
        this.expected = expected.toSimplifiedString();
        this.found = found;
    }

    public String getExpected() {
        return expected;
    }

    public ValDerivationNode getFound() {
        return found;
    }
}