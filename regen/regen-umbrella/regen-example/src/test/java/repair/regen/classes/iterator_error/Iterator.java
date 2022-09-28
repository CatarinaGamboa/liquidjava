package repair.regen.classes.iterator_error;

import repair.regen.specification.StateRefinement;
import repair.regen.specification.StateSet;

@StateSet({"notready", "ready", "finished"})
public class Iterator {

	boolean hn;
	@StateRefinement(from="notready(this)", to="ready(this)")
	boolean hasNext(boolean hn) {return hn;}

	@StateRefinement(from="ready(this)", to="finished(this)")
	int next(boolean hn) {
		int r;
		if (hn)
			r = 1;
		else
			r = -1;
		return r;
	}
}
