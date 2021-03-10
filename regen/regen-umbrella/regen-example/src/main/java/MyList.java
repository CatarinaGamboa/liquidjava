import java.util.ArrayList;
import java.util.List;

import repair.regen.specification.Refinement;
import repair.regen.specification.RefinementPredicate;

public class MyList {

	int[] arr = new int[20];
	
	@Refinement("lengthA(_) == 0")
	public ArrayList<Integer> createList() {
		return new ArrayList<Integer>();
	}
	
	@Refinement("lengthA(_) == (1 + lengthA(xs))")
	public ArrayList<Integer> append(ArrayList xs, int k){
		return null;
	}
}


