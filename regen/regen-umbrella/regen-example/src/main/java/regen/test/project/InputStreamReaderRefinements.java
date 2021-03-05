package regen.test.project;


import java.io.InputStream;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;
import repair.regen.specification.StateRefinement;

//https://docs.oracle.com/javase/7/docs/api/java/io/InputStreamReader.html
@ExternalRefinementsFor("java.io.InputStreamReader")
public interface InputStreamReaderRefinements {
	
	@RefinementPredicate("boolean open(InputStreamReader i)")
	@StateRefinement(to="open(this)")
	public void InputStreamReader(InputStream in);

	@StateRefinement(from="open(this)", to="open(this)")
	@Refinement("(_ >= -1) && (_ <= 127)")
	public int read();
	
	@StateRefinement(from="open(this)", to="!open(this)")
	public void close();
	
	
	@StateRefinement(from="open(this)", to="open(this)")
	@Refinement("_ >= -1")
	public int read(char[] cbuf, @Refinement("_ >= 0")int offset, @Refinement("_ >= 0")int length);
//	
//	@StateRefinement(from="open(this)", to="open(this)")
//	public int ready();
//	
//	@StateRefinement(from="open(this)", to="open(this)")
//	public String getEncoding();
	
	
	
}
