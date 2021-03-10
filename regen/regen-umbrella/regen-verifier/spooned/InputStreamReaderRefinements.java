

// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
// @StateRefinement(from="open(this)", to="open(this)")
// public int ready();
// 
// @StateRefinement(from="open(this)", to="open(this)")
// public String getEncoding();
@repair.regen.specification.ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefinements {
    @repair.regen.specification.RefinementPredicate("boolean open(InputStreamReader i)")
    @repair.regen.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "!open(this)")
    public void close();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @repair.regen.specification.Refinement("_ >= -1")
    public int read(@repair.regen.specification.Refinement("length(cbuf) > 0")
    char[] cbuf, @repair.regen.specification.Refinement("_ >= 0")
    int offset, @repair.regen.specification.Refinement("(_ >= 0) && (_ + offset) <= length(cbuf)")
    int length);
}

