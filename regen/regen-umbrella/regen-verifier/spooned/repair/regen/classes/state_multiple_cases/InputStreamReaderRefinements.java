package repair.regen.classes.state_multiple_cases;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
// @StateSet({"alreadyRead", "nothingRead"})
@repair.regen.specification.ExternalRefinementsFor("java.io.InputStreamReader")
@repair.regen.specification.StateSet({ "open", "close" })
public interface InputStreamReaderRefinements {
    @repair.regen.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "close(this)", to = "close(this)")
    @repair.regen.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close();
}

