package regen.test.project;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
// faster
// @StateRefinement(from="open(this)", to="open(this)")
// public int read(char[] cbuf, int offset, int length);
@liquidjava.specification.ExternalRefinementsFor("java.io.InputStreamReader")
@liquidjava.specification.StateSet({ "open", "close" })
public interface InputStreamReaderRefinements {
    public void InputStreamReader(java.io.InputStream in);

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int read();

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close();
}

