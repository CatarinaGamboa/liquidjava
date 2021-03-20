package regen.test.project;

import java.io.InputStream;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

//https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@ExternalRefinementsFor("java.io.InputStreamReader")
@StateSet({"open", "close"})
public interface InputStreamReaderRefinements {
	
	public void InputStreamReader(InputStream in);

	@StateRefinement(from="open(this)", to="open(this)")
	public int read();
	
	@StateRefinement(from="open(this)", to="close(this)")
	public void close();

//faster
//	@StateRefinement(from="open(this)", to="open(this)")
//	public int read(char[] cbuf, int offset, int length);

}
