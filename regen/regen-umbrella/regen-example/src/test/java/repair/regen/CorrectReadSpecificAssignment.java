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
	
	@Refinement("{a > 0} -> {true}")
	public static void addZ(int a) {
		@Refinement("\\v > 0")
		int d = a;
		if(d > 5) {
			@Refinement("b > 5")
			int b = d;
		}else {
			@Refinement("\\v <= 5")
			int c = d;
			d = 10;
			@Refinement("b > 9")
			int b = d;
		}
	}
	public static void main(String[] args) {

	}
}
