package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorNoRefinementsInVar {
	public static void main(String[] args) {
		int a = 11;
		@Refinement("b < 10")
		int b = a;
	}
}
