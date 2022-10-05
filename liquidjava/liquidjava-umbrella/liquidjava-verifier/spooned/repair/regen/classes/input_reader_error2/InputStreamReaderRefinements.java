package liquidjava.classes.input_reader_error2;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@liquidjava.specification.ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefinements {
    @liquidjava.specification.RefinementPredicate("boolean open(InputStreamReader i)")
    @liquidjava.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int read();

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "!open(this)")
    public void close();
}

