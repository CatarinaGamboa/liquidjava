package regen.test.project;

import java.io.InputStream;

import repair.regen.specification.ExternalRefinementsFor;
import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"open",  "close"})
public class MyInputStreamReader {
	
	@StateRefinement(to="open(this)")
	public MyInputStreamReader() {}

	@StateRefinement(from="open(this)", to="open(this)")
	@Refinement("(_ >= -1) && (_ <= 127)")
	public int read() {return 0;}
	
	@StateRefinement(from="close(this)", to="close(this)")
	@StateRefinement(from="open(this)", to="close(this)")
	public void close() {}
}
