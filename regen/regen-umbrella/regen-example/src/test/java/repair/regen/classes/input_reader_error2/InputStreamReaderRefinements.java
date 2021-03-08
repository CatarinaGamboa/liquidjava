package repair.regen.classes.input_reader_error2;

import java.io.InputStream;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;

//https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefinements {
	@RefinementPredicate("boolean open(InputStreamReader i)")
	@StateRefinement(to="open(this)")
	public void InputStreamReader(InputStream in);

	@StateRefinement(from="open(this)", to="open(this)")
	public int read();
	
	@StateRefinement(from="open(this)", to="!open(this)")
	public void close();

//faster
//	@StateRefinement(from="open(this)", to="open(this)")
//	public int read(char[] cbuf, int offset, int length);
	
//	@StateRefinement(from="open(this)", to="open(this)")
//	public int ready();

	
}
