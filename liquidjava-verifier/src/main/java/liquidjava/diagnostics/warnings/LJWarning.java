package liquidjava.diagnostics.warnings;

import liquidjava.diagnostics.Colors;
import liquidjava.diagnostics.LJDiagnostic;
import spoon.reflect.cu.SourcePosition;

/**
 * Base class for all LiquidJava warnings
 */
public abstract class LJWarning extends LJDiagnostic {

    public LJWarning(String message, String details, SourcePosition pos) {
        super("Warning", message, details, pos, Colors.BOLD_YELLOW);
    }
}
