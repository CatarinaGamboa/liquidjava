package liquidjava.diagnostics.errors;

import java.util.HashMap;

import liquidjava.diagnostics.ErrorPosition;
import liquidjava.diagnostics.TranslationTable;
import liquidjava.utils.Utils;
import spoon.reflect.cu.SourcePosition;

/**
 * Base class for all LiquidJava errors
 */
public abstract class LJError {

    private String title;
    private String message;
    private String snippet;
    private ErrorPosition position;
    private SourcePosition location;
    private TranslationTable translationTable;

    public LJError(String title, String message, SourcePosition pos, String snippet,
            TranslationTable translationTable) {
        this.title = title;
        this.message = message;
        this.snippet = snippet;
        this.translationTable = translationTable != null ? translationTable : new TranslationTable();
        this.location = pos;
        this.position = ErrorPosition.fromSpoonPosition(pos);
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getSnippet() {
        return snippet;
    }

    public ErrorPosition getPosition() {
        return position;
    }

    public SourcePosition getLocation() {
        return location;
    }

    public TranslationTable getTranslationTable() {
        return translationTable;
    }

    @Override
    public abstract String toString();

    public String toString(String extra) {
        StringBuilder sb = new StringBuilder();
        sb.append(title);

        if (snippet != null)
            sb.append(" at: \n").append(snippet.replace("@liquidjava.specification.", "@"));

        sb.append("\n");
        sb.append(message).append("\n");
        if (extra != null)
            sb.append(extra).append("\n");
        sb.append("Location: ").append(location != null ? Utils.stripParens(location.toString()) : "<unknown>")
                .append("\n");
        return sb.toString();
    }
}
