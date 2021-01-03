package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectReadSpecificAssignment {
	public void testAssignements() {
		@Refinement("\\v > 10")
		int a = 15;

		@Refinement("\\v > 14")
		int b = a;
		
		a = 12;
		
		@Refinement("\\v >= 15")
		int c = b;
		b = 16;
		
		@Refinement("\\v > 14")
		int d = c;
	}
	
	public void testIfs() {
		@Refinement("\\v > 10")
		int a = 15;
		if(a > 14) {
			@Refinement("\\v > 14")
			int b = a;
			a = 12;
			@Refinement("\\v < 14")
			int c = a;
		}
	}
	public static void main(String[] args) {

	}
}
