package repair.regen;

import repair.regen.specification.Refinement;

public class SimpleTest {
	public static void main(String[] args) {
	//Arithmetic Binary Operations
    	@Refinement("a >= 10")
		int a = 10;
		@Refinement("t > 0")
    	int t = a + 1;


	}
}
