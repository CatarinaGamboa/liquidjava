package liquidjava.errors;

public class ErrorPosition {

  private int lineStart;
  private int colStart;

  private int lineEnd;
  private int colEnd;

  public ErrorPosition(int line1, int col1, int line2, int col2) {
    lineStart = line1;
    colStart = col1;
    lineEnd = line2;
    colEnd = col2;
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
}
