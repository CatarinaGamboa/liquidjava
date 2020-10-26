package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	@Refinement("{a > 20}->{ \\v == a + 1}")
	private static int addOne(int a) {
		return a+1;
	}
	
	public static void main(String[] args) {
		@Refinement("\\v > 0")
		int a = 6;
		@Refinement("\\v > 0")
		int b = addOne(50) + a;

	}
}
