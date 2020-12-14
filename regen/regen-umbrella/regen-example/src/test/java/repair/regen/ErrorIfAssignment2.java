package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorIfAssignment2 {
	public static void main(String[] args) {
		@Refinement("\\v < 10")
		int a = 5;
		if(a < 0)
			a = 100;
	}
}
