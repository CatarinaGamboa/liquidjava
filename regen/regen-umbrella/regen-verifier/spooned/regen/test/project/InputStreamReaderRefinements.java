package regen.test.project;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
public interface InputStreamReaderRefinements {
    @repair.regen.specification.RefinementPredicate("boolean open(InputStreamReader i)")
    @repair.regen.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int read();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int read(char[] cbuf, int offset, int length);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "!open(this)")
    public void close();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int ready();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public java.lang.String getEncoding();
}

