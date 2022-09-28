package repair.regen.math.errorMultiplyExact;

import repair.regen.specification.Refinement;

@SuppressWarnings("unused")
public class ErrorMathMultiplyExact {
	public static void main(String[] args) {
		@Refinement("_ == 40")
		int mul = Math.multiplyExact(5, 8);
		@Refinement("_ == -mul")
		int mul1 = Math.multiplyExact(mul, -1);
		@Refinement("_ < 0")
		int mul2 = Math.multiplyExact(mul1, mul1);
	}
}
