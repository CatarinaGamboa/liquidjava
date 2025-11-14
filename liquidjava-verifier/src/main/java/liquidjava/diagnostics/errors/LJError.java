package liquidjava.diagnostics.errors;

import liquidjava.diagnostics.LJDiagnostic;
import liquidjava.diagnostics.TranslationTable;
import liquidjava.diagnostics.Colors;
import spoon.reflect.cu.SourcePosition;

/**
 * Base class for all LiquidJava errors
 */
public abstract class LJError extends LJDiagnostic {

    private TranslationTable translationTable;

    public LJError(String title, String message, String details, SourcePosition pos,
            TranslationTable translationTable) {
        super(title, message, details, pos, Colors.BOLD_RED);
        this.translationTable = translationTable != null ? translationTable : new TranslationTable();
    }

    public TranslationTable getTranslationTable() {
        return translationTable;
    }
}
