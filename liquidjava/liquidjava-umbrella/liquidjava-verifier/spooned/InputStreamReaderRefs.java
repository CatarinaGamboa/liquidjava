

// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
// @StateRefinement(from="open(this)", to="open(this)")
// public int ready();
// 
// @StateRefinement(from="open(this)", to="open(this)")
// public String getEncoding();
@liquidjava.specification.ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefs {
    @liquidjava.specification.RefinementPredicate("boolean open(InputStreamReader i)")
    @liquidjava.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @liquidjava.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read();

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "!open(this)")
    public void close();
}

