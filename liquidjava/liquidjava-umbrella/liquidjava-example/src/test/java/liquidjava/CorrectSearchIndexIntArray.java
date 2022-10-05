package liquidjava;

import liquidjava.specification.Refinement;

public class CorrectSearchIndexIntArray {
	
	public static void searchIndex(@Refinement("length(l) > 0")int[] l, 
			@Refinement("i >= 0") int i) {
		if(i >= l.length)
			return;
		else {
			@Refinement(" _ <= length(l)")
			int p = i+1;
			searchIndex(l, p);		
		}
	}
	
}
