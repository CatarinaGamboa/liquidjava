package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectUnaryOperators {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int v = 3;
		v--;
		@Refinement("\\v >= 10")
		int s = 100;
		s++;
		
		@Refinement("\\v < 0")
		int a = -6;
		@Refinement("b > 0")
		int b = 8;

		a = -3;
		a = -(6+5);
		b = -a;
		b = -(-10);
		b = +3;
		b = +s;
		
		@Refinement("\\v <= 0")
		int c = 5 * (-10);	
		
	}
}
