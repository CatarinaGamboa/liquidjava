package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	public static void main(String[] args) {
		@Refinement("b > 0")
		int b = 8;
		b = -b;
	}
}












