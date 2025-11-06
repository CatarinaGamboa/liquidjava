package testSuite.classes.input_reader_correct_default_this;

import java.io.InputStream;
import liquidjava.specification.ExternalRefinementsFor;
import liquidjava.specification.Ghost;
import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementPredicate;
import liquidjava.specification.StateRefinement;

// https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefs {

    @RefinementPredicate("boolean open(InputStreamReader i)")
    @StateRefinement(to = "open()")
    public void InputStreamReader(InputStream in);

    @StateRefinement(from = "open()", to = "open()")
    @Refinement("(_ >= -1) && (_ <= 127)")
    public int read();

    @StateRefinement(from = "open()", to = "!open()")
    public void close();

    @StateRefinement(from = "open()", to = "open()")
    @Refinement("_ >= -1")
    public int read(
            @Refinement("length(cbuf) > 0") char[] cbuf,
            @Refinement("_ >= 0") int offset,
            @Refinement("(_ >= 0) && (_ + offset) <= length(cbuf)") int length);
}
