package repair.regen.classes.input_reader_error2;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@repair.regen.specification.ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefinements {
    @repair.regen.specification.RefinementPredicate("boolean open(InputStreamReader i)")
    @repair.regen.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    public int read();

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "!open(this)")
    public void close();
}

