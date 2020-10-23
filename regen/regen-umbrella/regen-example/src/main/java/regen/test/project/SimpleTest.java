package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {
		@Refinement("\\v < 0")
		int a = -6;
		@Refinement("b > 0")
		int b = 8;

		a = -3;
		a = -(6+5);
		b = -(-10);
		//b = -b;
		//b = -b;

	}
}
