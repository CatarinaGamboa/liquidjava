package regen.test.project;


// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
// @StateSet({"alreadyRead", "nothingRead"})
// open->close
// close->open
// @StateRefinement(from="open(this)",to= "close(this)")
// @StateRefinement(from="close(this)",to="open(this)")
// public void toggle();
// 
// @StateRefinement(from="open(this)", to="open(this)")
// @Refinement("_ >= -1")
// public int read(@Refinement("length(cbuf) > 0") char[] cbuf,
// @Refinement("_ >= 0")int offset,
// @Refinement("(_ >= 0) && (_ + offset) <= length(cbuf)")int length);
// 
// @StateRefinement(from="open(this)", to="open(this)")
// public int ready();
// 
// @StateRefinement(from="open(this)", to="open(this)")
// public String getEncoding();
@repair.regen.specification.ExternalRefinementsFor("java.io.InputStreamReader")
@repair.regen.specification.States({ "open", "close" })
public interface InputStreamReaderRefinements {
    // @RefinementPredicate("boolean open(InputStreamReader i)")
    @repair.regen.specification.StateRefinement(to = "open(this)")
    public void InputStreamReader(java.io.InputStream in);

    @repair.regen.specification.StateRefinement(from = "open(this)", to = "open(this)")
    @repair.regen.specification.Refinement("(_ >= -1) && (_ <= 127)")
    public int read();

    @repair.regen.specification.StateRefinement(from = "close(this)", to = "close(this)")
    @repair.regen.specification.StateRefinement(from = "open(this)", to = "close(this)")
    public void close();
}

