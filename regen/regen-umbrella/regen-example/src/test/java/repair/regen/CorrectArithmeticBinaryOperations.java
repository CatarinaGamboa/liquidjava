package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectArithmeticBinaryOperations {
	public static void main(String[] args) {
		//Arithmetic Binary Operations
		@Refinement("a == 10")
		int a = 10;
		@Refinement("t > 0")
		int t = a + 1;
		@Refinement("\\v >= 9")
		int k = a - 1;
		@Refinement("\\v >= 5")
		int l = k * t;
		@Refinement("\\v > 0")
		int m = l / 2;
	
	}
}
