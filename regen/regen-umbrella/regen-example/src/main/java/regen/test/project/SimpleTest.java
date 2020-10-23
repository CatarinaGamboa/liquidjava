package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {
		@Refinement("b != 5")
		int b = 5;
	}


}
