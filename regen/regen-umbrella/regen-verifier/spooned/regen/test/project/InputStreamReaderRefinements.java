package regen.test.project;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@repair.regen.specification.ExternalRefinementsFor("java.io.InputStreamReader")
@repair.regen.specification.StateSet({ "open", "close" })
@repair.regen.specification.StateSet({ "alreadyRead", "nothingRead" })
public interface InputStreamReaderRefinements {
    @repair.regen.specification.StateRefinement(to = "open(this) && nothingRead(this) && close(this)")
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this) && alreadyRead(this)")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read();

    @repair.regen.specification.StateRefinement(from = "close(this)", to = "close(this)")
    @repair.regen.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close();
}

