package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectSearchIndexIntArray {
	
	public static void searchIndex(int[] l, @Refinement("i >= 0") int i) {
		if(i >= l.length)
			return;
		else {
			@Refinement(" _ <= length(l)")
			int i2 = i+1;
			searchIndex(l, i2);		
		}
	}


}
