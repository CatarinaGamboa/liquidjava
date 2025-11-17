package liquidjava.diagnostics;

import spoon.reflect.cu.SourcePosition;

public record ErrorPosition(int lineStart, int colStart, int lineEnd, int colEnd) {

    public static ErrorPosition fromSpoonPosition(SourcePosition pos) {
        if (pos == null || !pos.isValidPosition())
            return null;
        return new ErrorPosition(pos.getLine(), pos.getColumn(), pos.getEndLine(), pos.getEndColumn());
    }
}
