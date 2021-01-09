package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectFPArithmetic {
	public static void main(String[] args) {
		@Refinement("\\v > 5.0")
		double a = 5.5;

		@Refinement("\\v == 10.0")
		double b = 10.0;

		@Refinement("\\v != 10.0")
		double c = 5.0;

		@Refinement("t > 0.0")
		double t = a + 1.0;

		@Refinement("\\v >= 3.0")
		double k = a - 1.0;

		@Refinement("\\v > 0.0")
		double l = k * t;
		
		@Refinement("\\v > 0.0")
		double m = l / 2.0;
		
		@Refinement("\\v < 4.0")
		double n = 6.0 % 4.0;

		@Refinement("\\v < 0.0")
		double p = -5.0;
		
		@Refinement("\\v <= 0.0")
		double p1 = -a;
		
		@Refinement("\\v < -1.0")
		double p3 = p;
		
		@Refinement("\\v < -5.5")
		double d = (-a) - 2.0;

	}

}
