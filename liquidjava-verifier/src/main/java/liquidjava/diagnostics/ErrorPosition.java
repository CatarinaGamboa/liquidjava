package liquidjava.diagnostics;

import spoon.reflect.cu.SourcePosition;

public class ErrorPosition {

    private int lineStart;
    private int colStart;
    private int lineEnd;
    private int colEnd;

    public ErrorPosition(int lineStart, int colStart, int lineEnd, int colEnd) {
        this.lineStart = lineStart;
        this.colStart = colStart;
        this.lineEnd = lineEnd;
        this.colEnd = colEnd;
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getColStart() {
        return colStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public int getColEnd() {
        return colEnd;
    }

    public static ErrorPosition fromSpoonPosition(SourcePosition pos) {
        if (pos == null || !pos.isValidPosition())
            return null;
        return new ErrorPosition(pos.getLine(), pos.getColumn(), pos.getEndLine(), pos.getEndColumn());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ErrorPosition other = (ErrorPosition) obj;
        return lineStart == other.lineStart 
            && colStart == other.colStart 
            && lineEnd == other.lineEnd 
            && colEnd == other.colEnd;
    }

    @Override
    public int hashCode() {
        int result = lineStart;
        result = 31 * result + colStart;
        result = 31 * result + lineEnd;
        result = 31 * result + colEnd;
        return result;
    }
}
