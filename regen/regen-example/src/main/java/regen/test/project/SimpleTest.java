package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
    @SuppressWarnings("unused")
	public static void main(String[] args) {
    	int a = 1;
    	
		@Refinement("b > 3")
    	int b = 2; // should emit error
		
		@Refinement("c == 2 || c == 3 || c == 4")
    	int c = 2; // should be OK
		
		{
			int d = 2;
		}
		
		if ( a == 1) {
			c = 3;
		} else {
			c = 4;
		}
		
		@Refinement("d > 2")
		int d = c;
    }
}
