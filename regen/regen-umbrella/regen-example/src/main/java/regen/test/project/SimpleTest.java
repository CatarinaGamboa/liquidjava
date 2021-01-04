package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	public static void main(String[] args) {		
		@Refinement("\\v > 5")
		int a = 10;
		@Refinement("\\v > 10")
		int b = a+1;
		if(b < a) {
			a = 3;
		}
		
	}

}

