package regen.test.project;

import repair.regen.specification.Refinement;
import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"empty",  "filled"})
public class MyBufferedInputReader extends MyInputStreamReader{
	
	@StateRefinement(to="empty(this) && open(this)")
	public MyBufferedInputReader() {
		// TODO Auto-generated constructor stub
	}
	
//	@StateRefinement(from="open(this) && filled(this)", to="open(this)")
	@StateRefinement(from="open(this)", to="open(this)")
	@Refinement("(_ >= 0) && (_ <= 127)")
	public int read() {return 0;}

}
