package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	public static void main(String[] args) {
		@Refinement("\\v > 10")
		int a = 15;
		if(a > 14) {
			a = 9;
			@Refinement("\\v < 11")
			int c = a;
		}

	}

}

