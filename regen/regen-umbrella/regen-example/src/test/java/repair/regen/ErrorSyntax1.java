package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorSyntax1 {
	public static void main(String[] args) {
		@Refinement("_ < 100 +")
		int value = 90 + 4;
				
	}

}