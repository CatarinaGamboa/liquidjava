package liquidjava.classes;

import liquidjava.specification.StateRefinement;
import liquidjava.specification.StateSet;

@StateSet({"open", "close"})
public class MyStreamReader {

	@StateRefinement(from="close(this)", to="open(this)")
	public MyStreamReader() {
		
	}
}
