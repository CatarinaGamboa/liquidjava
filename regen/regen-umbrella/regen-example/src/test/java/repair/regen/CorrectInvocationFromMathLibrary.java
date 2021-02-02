package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectInvocationFromMathLibrary {
	public static void main(String[] args) {


		//Math.abs(...)
		@Refinement("b > 0")
		int b = Math.abs(6);
		@Refinement("\\v == 6")
		int a = Math.abs(6);
		@Refinement("\\v > 4")
		int d = Math.abs(-6);
		@Refinement("\\v == -6")
		int e = -Math.abs(-d);
		
		
		@Refinement("\\v > 4")
		int d1 = Math.abs(-6);
		@Refinement("\\v == -6")
		int e1 = -Math.abs(-d1);
		@Refinement("\\v == -6")
		int f1 = -Math.abs(e1);
		@Refinement("\\v == -6")
		int f2 = -Math.abs(f1);
				
		//addExact(...)
		@Refinement("\\v == 11")
		int a3 = Math.addExact(5, 6);

		//decrementExact
		@Refinement("\\v > 5")
		int a7 = 10;
		@Refinement("\\v > 4")
		int a8 = Math.decrementExact(a7);
		@Refinement("\\v == 9")
		int a9 = Math.decrementExact(a7);


		//incrementExact
		@Refinement("\\v > 6")
		int a12 = Math.incrementExact(a7);
		@Refinement("\\v == 11")
		int a13 = Math.incrementExact(a7);
		
		@Refinement("\\v > 5")
		int prim = 10;
		@Refinement("\\v > 6")
		int seg = Math.incrementExact(prim);
		@Refinement("\\v == 12")
		int ter = Math.incrementExact(seg);
		
		
		//max
		@Refinement("\\v == 5")
		int m1 = Math.max(4, 5);
		@Refinement("\\v > 5")
		int m2 = Math.max(100, m1);
		@Refinement("\\v == 100")
		int m3 = Math.max(100, m2);
		@Refinement("\\v == -100")
		int m4 = Math.max(-1000, -m2);
		
		//min
		@Refinement("\\v == 4")
		int m5 = Math.min(4, 5);
		@Refinement("\\v < 5")
		int m6 = Math.min(100, m5);
		@Refinement("\\v == 4")
		int m7 = Math.min(100, m6);
		@Refinement("\\v == -1000")
		int m8 = Math.min(-1000, -m6);
		
		//multiplyExact
		@Refinement("\\v == 40")
		int mul = Math.multiplyExact(5, 8);
		@Refinement("\\v == -mul")
		int mul1 = Math.multiplyExact(mul, -1);
		@Refinement("\\v > mul")
		int mul2 = Math.multiplyExact(mul1, mul1);
		
		//negateExact
		@Refinement("\\v == 40")
		int negE = Math.negateExact(-40);
		@Refinement("\\v < 0")
		int negEx = Math.negateExact(negE);
		@Refinement("\\v > 39")
		int negExa = Math.negateExact(negEx);

		//subtractExact
		@Refinement("\\v < -40")
		int subE = Math.subtractExact(-40, 5);
		@Refinement("\\v > 0")
		int subEx = Math.subtractExact(0, subE);
		@Refinement("\\v == 0")
		int subExa = Math.subtractExact(subEx, subEx);
		
	}
	
	private void notIntMethods() {
		//working but taking lot of time
//		//Math.random()
//		@Refinement("\\v >= 0")
//		double c = Math.random();
//		@Refinement("\\v < 0")
//		double f = -Math.random();
//		@Refinement("true")
//		double r1 = Math.random();		
//		@Refinement("\\v > 0")
//		double r2 = r1*5;
//
//		
//		@Refinement("\\v > 0")
//		double a1 = Math.abs(15.3);
//		@Refinement("\\v > 10")
//		long b1 = Math.abs(-13);
//		@Refinement("\\v > 10")
//		float c1 = Math.abs(-13f);
//		
//		@Refinement("\\v > 10")
//		long b3 = Math.addExact(5l, 6l);
//		
//		
//		@Refinement("\\v < 4")
//		double a5 = Math.acos(0.5);
//		@Refinement("\\v < 2")
//		double a6 = Math.asin(a5);
//		
//		
//		@Refinement("\\v > 5")
//		int a7 = 10;
//		@Refinement("\\v > 4")
//		long a10 = Math.decrementExact(a7);
//		@Refinement("\\v == 9")
//		long a11 = Math.decrementExact(a7);
//		
		
		// Constants
		@Refinement("\\v > 3")
		double a2 = Math.PI;
		@Refinement("\\v > 2")
		double b2 = Math.E;
		@Refinement("\\v == 30")
		double radius = 30;
		@Refinement("perimeter > 1")
		double perimeter = 2*Math.PI*radius;
		
	}
}
