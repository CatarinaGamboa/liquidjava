package liquidjava.classes.state_multiple_error;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@liquidjava.specification.ExternalRefinementsFor("java.io.InputStreamReader")
@liquidjava.specification.StateSet({ "open", "close" })
@liquidjava.specification.StateSet({ "alreadyRead", "nothingRead" })
public interface InputStreamReaderRefinements {
    @liquidjava.specification.StateRefinement(to = "open(this) && close(this)")
    public void InputStreamReader(java.io.InputStream in);

    @liquidjava.specification.StateRefinement(from = "open(this)", to = "open(this) && alreadyRead(this)")
    @liquidjava.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read();

    @liquidjava.specification.StateRefinement(from = "close(this)", to = "close(this)")
    @liquidjava.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close();
}

