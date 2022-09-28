package repair.regen;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class CorrectReadSpecificAssignment {
	public void testAssignements() {
		@Refinement("_ > 10")
		int a = 15;

		@Refinement("_ > 14")
		int b = a;
		
		a = 12;
		
		@Refinement("_ >= 15")
		int c = b;
		b = 16;
		
		@Refinement("_ > 14")
		int d = c;
	}
	
	public void testIfs() {
		@Refinement("_ > 10")
		int a = 15;
		if(a > 14) {
			@Refinement("_ > 14")
			int b = a;
			a = 12;
			@Refinement("_ < 14")
			int c = a;
		}
	}
	
	public static void addZ(@Refinement("a > 0")int a) {
		@Refinement("_ > 0")
		int d = a;
		if(d > 5) {
			@Refinement("b > 5")
			int b = d;
		}else {
			@Refinement("_ <= 5")
			int c = d;
			d = 10;
			@Refinement("b > 9")
			int b = d;
		}
	}
	
	public void testWithBinOperations() {
		@Refinement("_ > 5")
		int a = 10;
		@Refinement("_ > 10")
		int b = a+1;
		a = 6;
		b = a*2;
		@Refinement("_ > 20")
		int c = b*2;
	}
	
	public static void main(String[] args) {

	}
}
