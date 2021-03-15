package repair.regen.classes;

import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"open", "close"})
public class MyStreamReader {

	@StateRefinement(from="close(this)", to="open(this)")
	public MyStreamReader() {
		
	}
}
