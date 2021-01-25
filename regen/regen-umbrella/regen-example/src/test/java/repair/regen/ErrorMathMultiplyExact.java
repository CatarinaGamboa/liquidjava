package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorMathMultiplyExact {
	public static void main(String[] args) {
		@Refinement("\\v == 40")
		int mul = Math.multiplyExact(5, 8);
		@Refinement("\\v == -mul")
		int mul1 = Math.multiplyExact(mul, -1);
		@Refinement("\\v < 0")
		int mul2 = Math.multiplyExact(mul1, mul1);
	}
}
