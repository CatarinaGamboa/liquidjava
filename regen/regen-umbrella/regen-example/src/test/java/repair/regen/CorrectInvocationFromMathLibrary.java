package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectInvocationFromMathLibrary {
	public static void main(String[] args) {

//		@Refinement("\\v == -6") //TODO REVIEW
//		int e = -Math.abs(-d);

		//Math.random()
		@Refinement("\\v >= 0")
		double c = Math.random();
		
		@Refinement("\\v < 0")
		double f = -Math.random();
		
		//Math.abs(...)
		@Refinement("b > 0")
		int b = Math.abs(6);
		@Refinement("\\v == 6")
		int a = Math.abs(6);
		@Refinement("\\v > 4")
		int d = Math.abs(-6);
		@Refinement("\\v > 0")
		double a1 = Math.abs(15.3);
		@Refinement("\\v > 10")
		long b1 = Math.abs(-13);
		@Refinement("\\v > 10")
		float c1 = Math.abs(-13f);
		
		
		// Constants
		@Refinement("\\v > 3")
		double a2 = Math.PI;
		@Refinement("\\v > 2")
		double b2 = Math.E;
		@Refinement("\\v == 30")
		double radius = 30;
		@Refinement("perimeter > 1")
		double perimeter = 2*Math.PI*radius;
		
		//addExact(...)
		@Refinement("\\v == 11")
		int a3 = Math.addExact(5, 6);
		@Refinement("\\v > 10")
		long b3 = Math.addExact(5l, 6l);

	}
}
