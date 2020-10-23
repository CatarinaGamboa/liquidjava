package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorUnaryOpMinus {
	public static void main(String[] args) {
		@Refinement("b > 0")
		int b = 8;
		b = -b;
	}
}
