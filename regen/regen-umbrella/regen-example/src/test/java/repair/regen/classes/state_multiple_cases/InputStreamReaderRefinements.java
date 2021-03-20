package repair.regen.classes.state_multiple_cases;
import java.io.InputStream;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

//https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@ExternalRefinementsFor("java.io.InputStreamReader")
@StateSet({"open",  "close"})
@StateSet({"nothingRead", "alreadyRead"})
public interface InputStreamReaderRefinements {
	
	public void InputStreamReader(InputStream in);

	@StateRefinement(from="open(this)", to="open(this) && alreadyRead(this)")
	@Refinement("(_ >= -1) && (_ <= 127)")
	public int read();
	
	@StateRefinement(from="close(this)", to="close(this)")
	@StateRefinement(from="open(this)", to="close(this)")
	public void close();
	
}
