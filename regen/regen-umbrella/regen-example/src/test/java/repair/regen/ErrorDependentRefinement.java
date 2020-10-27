package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorDependentRefinement {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int smaller = 5;
		@Refinement("bigger > 20")
		int bigger = 50;
		@Refinement("\\v > smaller  && \\v < bigger")
		int middle = 21;
	}
}
