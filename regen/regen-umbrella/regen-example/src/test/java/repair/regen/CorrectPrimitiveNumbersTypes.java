package repair.regen;

import repair.regen.specification.Refinement;

public class CorrectPrimitiveNumbersTypes {
	public static void main(String[] args) {
		@Refinement("\\v > 5")
		int a = 10;
		
		@Refinement("\\v > 5")
		long b = 100;
		
		@Refinement("\\v > 5")
		short c = 10;
		
		@Refinement("\\v > 5")
		float d = 7.4f;
		
		
		@Refinement("\\v > 0")
		double e = fromType(a);
		e = fromType(b);
		e = fromType(c);
		
		@Refinement("f > 0")
		float f = twice(c);
		
	}
	
	@Refinement("{\\v > 0}->{\\v < i && \\v > 0}")
	private static double fromType(int i) {
		return i*0.1;
	}
	
	@Refinement("{\\v > 0}->{\\v < i && \\v > 0}")
	private static double fromType(long i) {
		return i*0.1;
	}
	
	@Refinement("{\\v > 0}->{\\v < i && \\v > 0}")
	private static double fromType(short i) {
		return i*0.1;
	}
	
	@Refinement("{i > 0}->{\\v > i}")
	private static float twice(short i) {
		return i*2f;
	}
}
