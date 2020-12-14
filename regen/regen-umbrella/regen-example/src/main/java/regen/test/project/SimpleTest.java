package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {

	@Refinement("{k >= 0} -> {\\v == 0}")
	public static int untilZero(int k) {
		if(k == 0)
			return 0;
		else
			return untilZero(k-1);
	}
	public static void main(String[] args) {
		@Refinement("b < 3")
		int b = untilZero(5);

	}


}












