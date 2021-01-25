package repair.regen;

import repair.regen.specification.Refinement;

public class ErrorRandom {
	public static void main(String[] args) {
		@Refinement("true")
		double m1 = Math.random();
		
		@Refinement("m2 <= 0")
		double m2 = m1*5;
	}

}
