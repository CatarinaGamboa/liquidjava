package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.hints.Hint;
import spoon.reflect.cu.SourcePosition;

/**
 * Error indicating that the SMT solver could not decide a verification condition
 */
public class SMTUnknownError extends LJError {
    private SourcePosition declarationPosition;

    public SMTUnknownError(String reason) {
        super("SMT Unknown Error", "Could not prove refinement", null, null);
        String tacticFailure = "smt tactic failed to show goal to be sat/unsat ";
        if (reason.startsWith(tacticFailure)) {
            reason = reason.substring(tacticFailure.length()).replace("(", "").replace(")", "");
        }
        addHint(Hint.general("Reason: " + reason));
    }

    public void setDeclarationPosition(SourcePosition declarationPosition) {
        this.declarationPosition = declarationPosition;
    }

    @Override
    public SourcePosition getDeclarationPosition() {
        return declarationPosition;
    }
}
