package liquidjava.diagnostics;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import spoon.reflect.cu.SourcePosition;

public class LJDiagnostic extends RuntimeException {

    private String title;
    private String message;
    private String details;
    private String file;
    private ErrorPosition position;
    private String accentColor;

    public LJDiagnostic(String title, String message, String details, SourcePosition pos, String accentColor) {
        this.title = title;
        this.message = message;
        this.details = details;
        this.file = pos != null ? pos.getFile().getPath() : null;
        this.position = ErrorPosition.fromSpoonPosition(pos);
        this.accentColor = accentColor;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public ErrorPosition getPosition() {
        return position;
    }

    public String getFile() {
        return file;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // title
        sb.append("\n").append(accentColor).append(title).append(": ").append(Colors.RESET).append(message)
                .append("\n");

        // snippet
        String snippet = getSnippet();
        if (snippet != null) {
            sb.append(snippet);
        }

        // details
        if (details != null && !details.isEmpty()) {
            sb.append(" --> ").append(String.join("\n     ", details.split("\n"))).append("\n");
        }

        // location
        if (file != null && position != null) {
            sb.append("\n").append(file).append(":").append(position.getLineStart()).append(Colors.RESET).append("\n");
        }

        return sb.toString();
    }

    public String getSnippet() {
        if (file == null || position == null)
            return null;

        Path path = Path.of(file);
        try {
            List<String> lines = Files.readAllLines(path);
            StringBuilder sb = new StringBuilder();

            // before and after lines for context
            int contextBefore = 2;
            int contextAfter = 2;
            int startLine = Math.max(1, position.getLineStart() - contextBefore);
            int endLine = Math.min(lines.size(), position.getLineEnd() + contextAfter);

            // calculate padding for line numbers
            int maxLineNum = endLine;
            int padding = String.valueOf(maxLineNum).length();

            for (int i = startLine; i <= endLine; i++) {
                String lineNumStr = String.format("%" + padding + "d", i);
                String line = lines.get(i - 1);

                // add line
                sb.append(Colors.GREY).append(lineNumStr).append(" | ").append(line).append(Colors.RESET).append("\n");

                // add error markers on the line(s) with the error
                if (i >= position.getLineStart() && i <= position.getLineEnd()) {
                    int colStart = (i == position.getLineStart()) ? position.getColStart() : 1;
                    int colEnd = (i == position.getLineEnd()) ? position.getColEnd() : line.length();

                    if (colStart > 0 && colEnd > 0) {
                        // line number padding + " | " + column offset
                        String indent = " ".repeat(padding) + Colors.GREY + " | " + Colors.RESET
                                + " ".repeat(colStart - 1);
                        String markers = accentColor + "^".repeat(Math.max(1, colEnd - colStart + 1)) + Colors.RESET;
                        sb.append(indent).append(markers).append("\n");
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        LJDiagnostic other = (LJDiagnostic) obj;
        return title.equals(other.title) && message.equals(other.message)
                && ((details == null && other.details == null) || (details != null && details.equals(other.details)))
                && ((file == null && other.file == null) || (file != null && file.equals(other.file)))
                && ((position == null && other.position == null)
                        || (position != null && position.equals(other.position)));
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }
}
