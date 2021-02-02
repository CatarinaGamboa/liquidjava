package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorAfterIf {
	public static void main(String[] args) {
		@Refinement("y < 100")
		int y = 5;
		if(y > 2)
			y = 3;
		else
			y = 9;

		@Refinement("z < 7")
		int z = y;
	}
}
