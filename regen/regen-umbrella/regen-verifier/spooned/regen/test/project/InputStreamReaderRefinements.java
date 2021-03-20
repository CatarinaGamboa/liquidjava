package regen.test.project;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
// faster
// @StateRefinement(from="open(this)", to="open(this)")
// public int read(char[] cbuf, int offset, int length);
@repair.regen.specification.ExternalRefinementsFor("java.io.InputStreamReader")
@repair.regen.specification.StateSet({ "open", "close" })
public interface InputStreamReaderRefinements {
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int read();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close();
}

