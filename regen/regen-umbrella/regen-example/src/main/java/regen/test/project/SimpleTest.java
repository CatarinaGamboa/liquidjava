package regen.test.project;

import repair.regen.specification.Refinement;

public class SimpleTest {
	
	@Refinement("{true}->{ \\v == (n > 10) }")
	public static boolean greaterThanTen(int n) {
		return n > 10;
	}

	
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;

		@Refinement("\\v == true")
		boolean k = (a < 11);

		@Refinement("\\v == true")
		boolean o = !(a == 12);

		@Refinement("\\v == false")
		boolean m = greaterThanTen(a);
		
	}

}

