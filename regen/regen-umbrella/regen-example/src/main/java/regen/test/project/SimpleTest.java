package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	public static void main(String[] args) {
		
		int a = 3;
		@Refinement("b < 10")
		int b = a;
		
	}


}
